package com.example.lms.service;

import com.example.lms.db.Database;
import com.example.lms.model.DashboardStats;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CollegeService {

    public DashboardStats dashboardStats() throws SQLException {
        String sql = """
            SELECT
              (SELECT COUNT(*) FROM students) total_students,
              (SELECT COUNT(*) FROM students WHERE status='ACTIVE') active_students,
              (SELECT COUNT(*) FROM departments WHERE active=TRUE) departments,
              (SELECT COUNT(*) FROM programs WHERE active=TRUE) programs,
              (SELECT COUNT(*) FROM attendance WHERE attendance_date=CURRENT_DATE) attendance_today,
              COALESCE((SELECT SUM(amount) FROM fee_invoices),0) fees_billed,
              COALESCE((SELECT SUM(amount) FROM fee_payments),0) fees_collected
            """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            BigDecimal billed = rs.getBigDecimal("fees_billed");
            BigDecimal collected = rs.getBigDecimal("fees_collected");
            return new DashboardStats(
                    rs.getLong("total_students"),
                    rs.getLong("active_students"),
                    rs.getLong("departments"),
                    rs.getLong("programs"),
                    rs.getLong("attendance_today"),
                    billed,
                    collected,
                    billed.subtract(collected)
            );
        }
    }

    public List<Object[]> listDepartments() throws SQLException {
        String sql = """
            SELECT id, code, name, active, created_at
            FROM departments ORDER BY name
            """;
        return rows(sql, rs -> new Object[]{
                rs.getLong("id"), rs.getString("code"), rs.getString("name"),
                rs.getBoolean("active") ? "Active" : "Inactive", rs.getTimestamp("created_at")
        });
    }

    public void addDepartment(String code, String name) throws SQLException {
        require(code, "Department code");
        require(name, "Department name");
        String sql = "INSERT INTO departments(code,name,active) VALUES(?,?,TRUE)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, code.trim().toUpperCase());
            ps.setString(2, name.trim());
            ps.executeUpdate();
        }
    }

    public List<Object[]> listPrograms() throws SQLException {
        String sql = """
            SELECT p.id, p.code, p.name, d.name department, p.duration_semesters, p.active
            FROM programs p JOIN departments d ON d.id=p.department_id
            ORDER BY p.name
            """;
        return rows(sql, rs -> new Object[]{
                rs.getLong("id"), rs.getString("code"), rs.getString("name"),
                rs.getString("department"), rs.getInt("duration_semesters"),
                rs.getBoolean("active") ? "Active" : "Inactive"
        });
    }

    public List<Object[]> departmentChoices() throws SQLException {
        String sql = "SELECT id, code, name FROM departments WHERE active=TRUE ORDER BY name";
        return rows(sql, rs -> new Object[]{
                rs.getLong("id"), rs.getString("code") + " - " + rs.getString("name")
        });
    }

    public void addProgram(long departmentId, String code, String name, int semesters) throws SQLException {
        require(code, "Program code");
        require(name, "Program name");
        if (semesters < 1 || semesters > 16) {
            throw new IllegalArgumentException("Duration must be between 1 and 16 semesters.");
        }
        String sql = """
            INSERT INTO programs(department_id,code,name,duration_semesters,active)
            VALUES(?,?,?,?,TRUE)
            """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, departmentId);
            ps.setString(2, code.trim().toUpperCase());
            ps.setString(3, name.trim());
            ps.setInt(4, semesters);
            ps.executeUpdate();
        }
    }

    public List<Object[]> listStudents() throws SQLException {
        String sql = """
            SELECT s.id, s.enrollment_no, s.first_name, s.last_name,
                   p.code program, s.current_semester, s.email, s.phone, s.status
            FROM students s
            JOIN programs p ON p.id=s.program_id
            ORDER BY s.created_at DESC
            """;
        return rows(sql, rs -> new Object[]{
                rs.getLong("id"), rs.getString("enrollment_no"),
                rs.getString("first_name") + " " + rs.getString("last_name"),
                rs.getString("program"), rs.getInt("current_semester"),
                rs.getString("email"), rs.getString("phone"), rs.getString("status")
        });
    }

    public List<Object[]> programChoices() throws SQLException {
        String sql = """
            SELECT p.id, p.code, p.name, d.code department_code
            FROM programs p JOIN departments d ON d.id=p.department_id
            WHERE p.active=TRUE ORDER BY p.name
            """;
        return rows(sql, rs -> new Object[]{
                rs.getLong("id"),
                rs.getString("code") + " - " + rs.getString("name") +
                        " (" + rs.getString("department_code") + ")"
        });
    }

    public void addStudent(String enrollmentNo, String firstName, String lastName,
                           long programId, int semester, String email, String phone) throws SQLException {
        require(enrollmentNo, "Enrollment number");
        require(firstName, "First name");
        if (semester < 1 || semester > 16) {
            throw new IllegalArgumentException("Semester must be between 1 and 16.");
        }
        String sql = """
            INSERT INTO students(enrollment_no,first_name,last_name,program_id,current_semester,
                                 email,phone,status,admission_date)
            VALUES(?,?,?,?,?,?,?,'ACTIVE',CURRENT_DATE)
            """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, enrollmentNo.trim().toUpperCase());
            ps.setString(2, firstName.trim());
            ps.setString(3, lastName == null ? "" : lastName.trim());
            ps.setLong(4, programId);
            ps.setInt(5, semester);
            ps.setString(6, blankToNull(email));
            ps.setString(7, blankToNull(phone));
            ps.executeUpdate();
        }
    }

    public List<Object[]> studentChoices() throws SQLException {
        String sql = """
            SELECT id, enrollment_no, first_name, last_name
            FROM students WHERE status='ACTIVE'
            ORDER BY enrollment_no
            """;
        return rows(sql, rs -> new Object[]{
                rs.getLong("id"),
                rs.getString("enrollment_no") + " - " +
                        rs.getString("first_name") + " " + rs.getString("last_name")
        });
    }

    public void markAttendance(long studentId, LocalDate date, String status, String remarks) throws SQLException {
        if (date == null) throw new IllegalArgumentException("Attendance date is required.");
        if (!List.of("PRESENT", "ABSENT", "LATE", "LEAVE").contains(status)) {
            throw new IllegalArgumentException("Invalid attendance status.");
        }
        String sql = """
            INSERT INTO attendance(student_id,attendance_date,status,remarks)
            VALUES(?,?,?,?)
            ON DUPLICATE KEY UPDATE status=VALUES(status), remarks=VALUES(remarks),
                                    updated_at=CURRENT_TIMESTAMP
            """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, studentId);
            ps.setDate(2, java.sql.Date.valueOf(date));
            ps.setString(3, status);
            ps.setString(4, blankToNull(remarks));
            ps.executeUpdate();
        }
    }

    public List<Object[]> listRecentAttendance() throws SQLException {
        String sql = """
            SELECT a.id, a.attendance_date, s.enrollment_no,
                   CONCAT(s.first_name,' ',s.last_name) student,
                   a.status, a.remarks
            FROM attendance a
            JOIN students s ON s.id=a.student_id
            ORDER BY a.attendance_date DESC, a.updated_at DESC
            LIMIT 200
            """;
        return rows(sql, rs -> new Object[]{
                rs.getLong("id"), rs.getDate("attendance_date"),
                rs.getString("enrollment_no"), rs.getString("student"),
                rs.getString("status"), rs.getString("remarks")
        });
    }

    public void createFeeInvoice(long studentId, String title, BigDecimal amount, LocalDate dueDate)
            throws SQLException {
        require(title, "Fee title");
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Fee amount must be greater than zero.");
        }
        if (dueDate == null) throw new IllegalArgumentException("Due date is required.");
        String sql = """
            INSERT INTO fee_invoices(student_id,title,amount,due_date,status)
            VALUES(?,?,?,?,'UNPAID')
            """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, studentId);
            ps.setString(2, title.trim());
            ps.setBigDecimal(3, amount);
            ps.setDate(4, java.sql.Date.valueOf(dueDate));
            ps.executeUpdate();
        }
    }

    public void recordFeePayment(long invoiceId, BigDecimal amount, String mode, String reference)
            throws SQLException {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }
        require(mode, "Payment mode");

        String lockInvoice = "SELECT amount FROM fee_invoices WHERE id=? FOR UPDATE";
        String paidSql = "SELECT COALESCE(SUM(amount),0) paid FROM fee_payments WHERE invoice_id=?";
        String insertPayment = """
            INSERT INTO fee_payments(invoice_id,amount,payment_mode,reference_no)
            VALUES(?,?,?,?)
            """;
        String updateStatus = "UPDATE fee_invoices SET status=? WHERE id=?";

        try (Connection c = Database.getConnection()) {
            c.setAutoCommit(false);
            try {
                BigDecimal invoiceAmount;
                try (PreparedStatement ps = c.prepareStatement(lockInvoice)) {
                    ps.setLong(1, invoiceId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Fee invoice not found.");
                        invoiceAmount = rs.getBigDecimal("amount");
                    }
                }

                BigDecimal paid;
                try (PreparedStatement ps = c.prepareStatement(paidSql)) {
                    ps.setLong(1, invoiceId);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        paid = rs.getBigDecimal("paid");
                    }
                }

                BigDecimal remaining = invoiceAmount.subtract(paid);
                if (amount.compareTo(remaining) > 0) {
                    throw new IllegalArgumentException("Payment exceeds outstanding amount: " + remaining);
                }

                try (PreparedStatement ps = c.prepareStatement(insertPayment)) {
                    ps.setLong(1, invoiceId);
                    ps.setBigDecimal(2, amount);
                    ps.setString(3, mode.trim().toUpperCase());
                    ps.setString(4, blankToNull(reference));
                    ps.executeUpdate();
                }

                BigDecimal newPaid = paid.add(amount);
                String newStatus = newPaid.compareTo(invoiceAmount) >= 0 ? "PAID" : "PARTIAL";
                try (PreparedStatement ps = c.prepareStatement(updateStatus)) {
                    ps.setString(1, newStatus);
                    ps.setLong(2, invoiceId);
                    ps.executeUpdate();
                }

                c.commit();
            } catch (Exception e) {
                c.rollback();
                if (e instanceof SQLException se) throw se;
                if (e instanceof IllegalArgumentException iae) throw iae;
                throw new SQLException(e.getMessage(), e);
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<Object[]> listFees() throws SQLException {
        String sql = """
            SELECT f.id, s.enrollment_no,
                   CONCAT(s.first_name,' ',s.last_name) student,
                   f.title, f.amount,
                   COALESCE(SUM(p.amount),0) paid,
                   f.amount-COALESCE(SUM(p.amount),0) balance,
                   f.due_date, f.status
            FROM fee_invoices f
            JOIN students s ON s.id=f.student_id
            LEFT JOIN fee_payments p ON p.invoice_id=f.id
            GROUP BY f.id,s.enrollment_no,s.first_name,s.last_name,f.title,
                     f.amount,f.due_date,f.status
            ORDER BY f.created_at DESC
            """;
        return rows(sql, rs -> new Object[]{
                rs.getLong("id"), rs.getString("enrollment_no"), rs.getString("student"),
                rs.getString("title"), rs.getBigDecimal("amount"), rs.getBigDecimal("paid"),
                rs.getBigDecimal("balance"), rs.getDate("due_date"), rs.getString("status")
        });
    }

    private List<Object[]> rows(String sql, RowMapper mapper) throws SQLException {
        List<Object[]> result = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(mapper.map(rs));
        }
        return result;
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " is required.");
        }
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    @FunctionalInterface
    private interface RowMapper {
        Object[] map(ResultSet rs) throws SQLException;
    }
}