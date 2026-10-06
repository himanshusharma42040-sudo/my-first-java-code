package com.example.lms.service;

import com.example.lms.db.Database;
import com.example.lms.model.UserSession;
import com.example.lms.security.PasswordUtil;
import java.sql.*;
import java.util.Arrays;
import java.util.Optional;

public class AuthService {
    public Optional<UserSession> login(String email, char[] password) throws Exception {
        String sql = """
            SELECT id, full_name, email, password_hash, password_salt, role
            FROM users
            WHERE email = ? AND active = TRUE
            LIMIT 1
            """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email.trim().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                if (!PasswordUtil.verify(password, rs.getString("password_salt"), rs.getString("password_hash"))) {
                    return Optional.empty();
                }
                return Optional.of(new UserSession(
                    rs.getLong("id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    UserSession.Role.valueOf(rs.getString("role"))
                ));
            }
        } finally {
            Arrays.fill(password, '\0');
        }
    }
}
