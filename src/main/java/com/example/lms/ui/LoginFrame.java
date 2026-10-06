package com.example.lms.ui;

import com.example.lms.db.Database;
import com.example.lms.model.UserSession;
import com.example.lms.service.AuthService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField email=new JTextField("admin@library.local");
    private final JPasswordField password=new JPasswordField();
    private final JButton login=Ui.primaryButton("Sign In");
    private final AuthService auth=new AuthService();

    public LoginFrame() {
        super("Library Management System - Login");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(520,520);
        setMinimumSize(new Dimension(480,480));
        setLocationRelativeTo(null);

        JPanel outer=new JPanel(new GridBagLayout());
        outer.setBackground(Ui.BG);

        JPanel card=new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(36,42,36,42));
        card.setPreferredSize(new Dimension(400,390));

        JLabel title=new JLabel("Library Desk");
        title.setFont(new Font("SansSerif",Font.BOLD,30));
        title.setForeground(Ui.PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub=new JLabel("Secure Admin & Librarian Login");
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        email.setMaximumSize(new Dimension(Integer.MAX_VALUE,38));
        password.setMaximumSize(new Dimension(Integer.MAX_VALUE,38));
        login.setMaximumSize(new Dimension(Integer.MAX_VALUE,42));
        login.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(sub);
        card.add(Box.createVerticalStrut(26));
        card.add(label("Email"));
        card.add(Box.createVerticalStrut(5));
        card.add(email);
        card.add(Box.createVerticalStrut(14));
        card.add(label("Password"));
        card.add(Box.createVerticalStrut(5));
        card.add(password);
        card.add(Box.createVerticalStrut(22));
        card.add(login);

        outer.add(card);
        setContentPane(outer);

        login.addActionListener(e->doLogin());
        password.addActionListener(e->doLogin());
    }

    private JLabel label(String t) {
        JLabel l=new JLabel(t);
        l.setFont(new Font("SansSerif",Font.BOLD,12));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void doLogin() {
        if(email.getText().isBlank()||password.getPassword().length==0) {
            Ui.info(this,"Enter email and password.");
            return;
        }
        login.setEnabled(false);
        char[] pwd=password.getPassword();

        SwingWorker<UserSession,Void> worker=new SwingWorker<>() {
            @Override protected UserSession doInBackground() throws Exception {
                Database.testConnection();
                return auth.login(email.getText(),pwd).orElse(null);
            }
            @Override protected void done() {
                login.setEnabled(true);
                try {
                    UserSession s=get();
                    if(s==null) {
                        Ui.info(LoginFrame.this,"Invalid email or password.");
                        password.setText("");
                        return;
                    }
                    dispose();
                    if(s.role()==UserSession.Role.ADMIN) new AdminDashboard(s).setVisible(true);
                    else new LibrarianDashboard(s).setVisible(true);
                } catch(Exception ex) {
                    Throwable c=ex.getCause()==null ? ex : ex.getCause();
                    Ui.error(LoginFrame.this,c instanceof Exception e ? e : new RuntimeException(c));
                }
            }
        };
        worker.execute();
    }
}
