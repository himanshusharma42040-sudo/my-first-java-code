package com.example.lms.service;

import com.example.lms.db.Database;
import com.example.lms.model.UserSession;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class LibraryService {
    public List<Object[]> listBooks() throws SQLException {
        String sql = """
            SELECT id, book_code, title, author, total_copies, available_copies
            FROM books ORDER BY title
            """;
        List<Object[]> rows=new ArrayList<>();
        try(Connection c=Database.getConnection();
            PreparedStatement ps=c.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()) {
            while(rs.next()) {
                rows.add(new Object[]{
                    rs.getLong("id"),rs.getString("book_code"),rs.getString("title"),
                    rs.getString("author"),rs.getInt("total_copies"),rs.getInt("available_copies")
                });
            }
        }
        return rows;
    }

    public void addBook(String code,String title,String author,int copies) throws SQLException {
        if(code==null||code.isBlank()) throw new IllegalArgumentException("Book code is required.");
        if(title==null||title.isBlank()) throw new IllegalArgumentException("Book title is required.");
        if(author==null||author.isBlank()) throw new IllegalArgumentException("Author is required.");
        if(copies<1) throw new IllegalArgumentException("Copies must be at least 1.");

        String sql = """
            INSERT INTO books(book_code,title,author,total_copies,available_copies)
            VALUES(?,?,?,?,?)
            """;
        try(Connection c=Database.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,code.trim().toUpperCase());
            ps.setString(2,title.trim());
            ps.setString(3,author.trim());
            ps.setInt(4,copies);
            ps.setInt(5,copies);
            ps.executeUpdate();
        }
    }

    public long issueBook(String code,String borrower,LocalDate due,UserSession librarian) throws SQLException {
        if(borrower==null||borrower.isBlank()) throw new IllegalArgumentException("Borrower name is required.");
        if(due==null||due.isBefore(LocalDate.now())) throw new IllegalArgumentException("Due date cannot be in the past.");

        String lock="SELECT id,available_copies FROM books WHERE book_code=? FOR UPDATE";
        String insert = """
            INSERT INTO issued_books(book_id,issued_to,issued_by,due_at,status)
            VALUES(?,?,?,?,'ISSUED')
            """;
        String dec="UPDATE books SET available_copies=available_copies-1 WHERE id=? AND available_copies>0";

        try(Connection c=Database.getConnection()) {
            c.setAutoCommit(false);
            try {
                long bookId;
                int available;
                try(PreparedStatement ps=c.prepareStatement(lock)) {
                    ps.setString(1,code.trim().toUpperCase());
                    try(ResultSet rs=ps.executeQuery()) {
                        if(!rs.next()) throw new SQLException("Book code not found.");
                        bookId=rs.getLong("id");
                        available=rs.getInt("available_copies");
                    }
                }
                if(available<1) throw new SQLException("No available copy for this book.");

                long issueId;
                try(PreparedStatement ps=c.prepareStatement(insert,Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1,bookId);
                    ps.setString(2,borrower.trim());
                    ps.setLong(3,librarian.id());
                    ps.setDate(4,java.sql.Date.valueOf(due));
                    ps.executeUpdate();
                    try(ResultSet rs=ps.getGeneratedKeys()) {
                        if(!rs.next()) throw new SQLException("Issue ID was not generated.");
                        issueId=rs.getLong(1);
                    }
                }

                try(PreparedStatement ps=c.prepareStatement(dec)) {
                    ps.setLong(1,bookId);
                    if(ps.executeUpdate()!=1) throw new SQLException("Could not reserve copy.");
                }

                c.commit();
                return issueId;
            } catch(Exception e) {
                c.rollback();
                if(e instanceof SQLException se) throw se;
                throw new SQLException(e.getMessage(),e);
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public void returnBook(long issueId) throws SQLException {
        String lock="SELECT book_id,status FROM issued_books WHERE id=? FOR UPDATE";
        String update = """
            UPDATE issued_books
            SET status='RETURNED', returned_at=CURRENT_TIMESTAMP
            WHERE id=? AND status='ISSUED'
            """;
        String inc = """
            UPDATE books
            SET available_copies=LEAST(total_copies,available_copies+1)
            WHERE id=?
            """;

        try(Connection c=Database.getConnection()) {
            c.setAutoCommit(false);
            try {
                long bookId;
                String status;
                try(PreparedStatement ps=c.prepareStatement(lock)) {
                    ps.setLong(1,issueId);
                    try(ResultSet rs=ps.executeQuery()) {
                        if(!rs.next()) throw new SQLException("Issue record not found.");
                        bookId=rs.getLong("book_id");
                        status=rs.getString("status");
                    }
                }
                if(!"ISSUED".equals(status)) throw new SQLException("Book is already returned.");

                try(PreparedStatement ps=c.prepareStatement(update)) {
                    ps.setLong(1,issueId);
                    if(ps.executeUpdate()!=1) throw new SQLException("Could not mark return.");
                }
                try(PreparedStatement ps=c.prepareStatement(inc)) {
                    ps.setLong(1,bookId);
                    if(ps.executeUpdate()!=1) throw new SQLException("Could not update stock.");
                }
                c.commit();
            } catch(Exception e) {
                c.rollback();
                if(e instanceof SQLException se) throw se;
                throw new SQLException(e.getMessage(),e);
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<Object[]> listIssues() throws SQLException {
        String sql = """
            SELECT i.id,b.book_code,b.title,i.issued_to,u.full_name,
                   i.issued_at,i.due_at,i.status,i.returned_at
            FROM issued_books i
            JOIN books b ON b.id=i.book_id
            JOIN users u ON u.id=i.issued_by
            ORDER BY i.issued_at DESC
            """;
        List<Object[]> rows=new ArrayList<>();
        try(Connection c=Database.getConnection();
            PreparedStatement ps=c.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()) {
            while(rs.next()) {
                rows.add(new Object[]{
                    rs.getLong("id"),rs.getString("book_code"),rs.getString("title"),
                    rs.getString("issued_to"),rs.getString("full_name"),
                    rs.getTimestamp("issued_at"),rs.getDate("due_at"),
                    rs.getString("status"),rs.getTimestamp("returned_at")
                });
            }
        }
        return rows;
    }
}
