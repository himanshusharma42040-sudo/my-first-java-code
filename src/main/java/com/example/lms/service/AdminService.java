package com.example.lms.service;

import com.example.lms.db.Database;
import com.example.lms.security.PasswordUtil;
import java.sql.*;
import java.util.*;

public class AdminService {
    public List<Object[]> listLibrarians() throws SQLException {
        String sql = """
            SELECT id, full_name, email, active, created_at
            FROM users
            WHERE role='LIBRARIAN'
            ORDER BY created_at DESC
            """;
        List<Object[]> rows = new ArrayList<>();
        try (Connection c=Database.getConnection();
             PreparedStatement ps=c.prepareStatement(sql);
             ResultSet rs=ps.executeQuery()) {
            while(rs.next()) {
                rows.add(new Object[]{
                    rs.getLong("id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getBoolean("active") ? "Active" : "Inactive",
                    rs.getTimestamp("created_at")
                });
            }
        }
        return rows;
    }

    public void addLibrarian(String name,String email,char[] password) throws Exception {
        if(name==null||name.isBlank()) throw new IllegalArgumentException("Full name is required.");
        if(email==null||!email.contains("@")) throw new IllegalArgumentException("Valid email is required.");
        if(password==null||password.length<8) throw new IllegalArgumentException("Password must be at least 8 characters.");

        PasswordUtil.PasswordData data=PasswordUtil.hash(password);
        Arrays.fill(password,'\0');

        String sql = """
            INSERT INTO users(full_name,email,password_hash,password_salt,role,active)
            VALUES(?,?,?,?,'LIBRARIAN',TRUE)
            """;
        try(Connection c=Database.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,name.trim());
            ps.setString(2,email.trim().toLowerCase());
            ps.setString(3,data.hashBase64());
            ps.setString(4,data.saltBase64());
            ps.executeUpdate();
        }
    }

    public void setLibrarianActive(long id,boolean active) throws SQLException {
        String sql="UPDATE users SET active=? WHERE id=? AND role='LIBRARIAN'";
        try(Connection c=Database.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setBoolean(1,active);
            ps.setLong(2,id);
            if(ps.executeUpdate()!=1) throw new SQLException("Librarian not found.");
        }
    }
}
