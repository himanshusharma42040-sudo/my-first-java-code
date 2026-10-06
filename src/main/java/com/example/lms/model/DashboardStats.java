package com.example.lms.model;

import java.math.BigDecimal;

public record DashboardStats(
        long totalStudents,
        long activeStudents,
        long departments,
        long programs,
        long attendanceToday,
        BigDecimal feesBilled,
        BigDecimal feesCollected,
        BigDecimal feesOutstanding
) {}