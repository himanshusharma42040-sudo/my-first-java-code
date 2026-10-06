package com.example.lms.ui;

import com.example.lms.model.UserSession;
import com.example.lms.service.AdminService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private final UserSession session;
    private final AdminService service=new AdminService();
    private final DefaultTableModel model=Ui.model("ID","Name","Email","Status","Created");

    public AdminDashboard(UserSession session) {
        super("Library Management System - Admin");
        this.session=session;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(980,650);
        setLocationRelativeTo(null);
        setJMenuBar(menu());

        JTabbedPane tabs=new JTabbedPane();
        tabs.addTab("Librarians",listPanel());
        tabs.addTab("Add Librarian",addPanel());
        setContentPane(Ui.page("Admin Dashboard",tabs));
        refresh();
    }

    private JMenuBar menu() {
        JMenuBar b=new JMenuBar();
        JMenu a=new JMenu("Account");
        JMenuItem who=new JMenuItem("Signed in: "+session.fullName());
        who.setEnabled(false);
        JMenuItem logout=new JMenuItem("Logout");
        logout.addActionListener(e->{ dispose(); new LoginFrame().setVisible(true); });
        a.add(who); a.addSeparator(); a.add(logout); b.add(a);
        return b;
    }

    private JPanel listPanel() {
        JPanel p=new JPanel(new BorderLayout(8,8));
        JTable table=new JTable(model);
        table.setRowHeight(28);
        table.setAutoCreateRowSorter(true);

        JButton refresh=Ui.primaryButton("Refresh");
        JButton toggle=Ui.primaryButton("Activate / Deactivate");
        refresh.addActionListener(e->refresh());
        toggle.addActionListener(e->{
            int row=table.getSelectedRow();
            if(row<0) { Ui.info(this,"Select a librarian."); return; }
            row=table.convertRowIndexToModel(row);
            long id=((Number)model.getValueAt(row,0)).longValue();
            boolean active="Active".equals(model.getValueAt(row,3));
            try {
                service.setLibrarianActive(id,!active);
                refresh();
            } catch(Exception ex) { Ui.error(this,ex); }
        });

        JPanel top=new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(refresh); top.add(toggle);
        p.add(top,BorderLayout.NORTH);
        p.add(new JScrollPane(table),BorderLayout.CENTER);
        return p;
    }

    private JPanel addPanel() {
        JPanel p=new JPanel(new GridBagLayout());
        GridBagConstraints c=gbc();
        JTextField name=new JTextField(24);
        JTextField email=new JTextField(24);
        JPasswordField password=new JPasswordField(24);
        JButton add=Ui.primaryButton("Create Librarian");

        row(p,c,0,"Full Name",name);
        row(p,c,1,"Email",email);
        row(p,c,2,"Temporary Password",password);
        c.gridx=1; c.gridy=3; p.add(add,c);

        add.addActionListener(e->{
            try {
                service.addLibrarian(name.getText(),email.getText(),password.getPassword());
                name.setText(""); email.setText(""); password.setText("");
                refresh();
                Ui.info(this,"Librarian created.");
            } catch(Exception ex) { Ui.error(this,ex); }
        });
        return p;
    }

    private void refresh() {
        try { Ui.fill(model,service.listLibrarians()); }
        catch(Exception ex) { Ui.error(this,ex); }
    }

    private static GridBagConstraints gbc() {
        GridBagConstraints c=new GridBagConstraints();
        c.insets=new Insets(8,8,8,8);
        c.fill=GridBagConstraints.HORIZONTAL;
        return c;
    }

    private static void row(JPanel p,GridBagConstraints c,int y,String label,JComponent field) {
        c.gridx=0; c.gridy=y; c.weightx=0; p.add(new JLabel(label),c);
        c.gridx=1; c.weightx=1; p.add(field,c);
    }
}
