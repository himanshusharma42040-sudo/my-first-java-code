package com.example.lms.db;

import com.example.lms.config.DbConfig;
import java.sql.*;

public final class Database {
    private Database() {}
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DbConfig.url(), DbConfig.user(), DbConfig.password());
    }
    public static void testConnection() throws SQLException {
        try(Connection c=getConnection()) {
            if(!c.isValid(3)) throw new SQLException("MySQL connection is not valid.");
        }
    }
}
