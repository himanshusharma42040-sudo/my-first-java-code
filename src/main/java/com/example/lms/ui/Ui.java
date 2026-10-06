package com.example.lms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

final class Ui {
    static final Color PRIMARY=new Color(37,78,140);
    static final Color BG=new Color(244,247,251);
    private Ui(){}

    static JPanel page(String title,JComponent content) {
        JPanel p=new JPanel(new BorderLayout(0,16));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(18,18,18,18));
        JLabel h=new JLabel(title);
        h.setFont(new Font("SansSerif",Font.BOLD,26));
        h.setForeground(PRIMARY);
        p.add(h,BorderLayout.NORTH);
        p.add(content,BorderLayout.CENTER);
        return p;
    }

    static JButton primaryButton(String text) {
        JButton b=new JButton(text);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif",Font.BOLD,13));
        return b;
    }

    static DefaultTableModel model(String... columns) {
        return new DefaultTableModel(columns,0) {
            @Override public boolean isCellEditable(int r,int c){ return false; }
        };
    }

    static void fill(DefaultTableModel m,List<Object[]> rows) {
        m.setRowCount(0);
        for(Object[] row:rows) m.addRow(row);
    }

    static void info(Component parent,String msg) {
        JOptionPane.showMessageDialog(parent,msg,"Library Management System",JOptionPane.INFORMATION_MESSAGE);
    }

    static void error(Component parent,Exception e) {
        JOptionPane.showMessageDialog(parent,
            e.getMessage()==null ? e.toString() : e.getMessage(),
            "Error",JOptionPane.ERROR_MESSAGE);
    }
}
