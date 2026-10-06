package com.example.lms.ui;

import com.example.lms.model.UserSession;
import com.example.lms.service.LibraryService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class LibrarianDashboard extends JFrame {
    private final UserSession session;
    private final LibraryService service=new LibraryService();
    private final DefaultTableModel books=Ui.model("ID","Code","Title","Author","Total","Available");
    private final DefaultTableModel issues=Ui.model("Issue ID","Code","Title","Borrower","Issued By","Issued At","Due","Status","Returned At");

    public LibrarianDashboard(UserSession session) {
        super("Library Management System - Librarian");
        this.session=session;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1120,720);
        setLocationRelativeTo(null);
        setJMenuBar(menu());

        JTabbedPane tabs=new JTabbedPane();
        tabs.addTab("Books",booksPanel());
        tabs.addTab("Add Book",addBookPanel());
        tabs.addTab("Issue Book",issuePanel());
        tabs.addTab("Issued Books",issuesPanel());
        tabs.addTab("Return Book",returnPanel());

        setContentPane(Ui.page("Librarian Dashboard",tabs));
        refreshBooks();
        refreshIssues();
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

    private JPanel booksPanel() {
        JPanel p=new JPanel(new BorderLayout(8,8));
        JTable t=new JTable(books);
        t.setRowHeight(28);
        t.setAutoCreateRowSorter(true);
        JButton r=Ui.primaryButton("Refresh");
        r.addActionListener(e->refreshBooks());
        JPanel top=new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(r);
        p.add(top,BorderLayout.NORTH);
        p.add(new JScrollPane(t),BorderLayout.CENTER);
        return p;
    }

    private JPanel addBookPanel() {
        JPanel p=new JPanel(new GridBagLayout());
        GridBagConstraints c=gbc();
        JTextField code=new JTextField(20);
        JTextField title=new JTextField(20);
        JTextField author=new JTextField(20);
        JSpinner copies=new JSpinner(new SpinnerNumberModel(1,1,999,1));
        JButton add=Ui.primaryButton("Add Book");

        row(p,c,0,"Book Code",code);
        row(p,c,1,"Title",title);
        row(p,c,2,"Author",author);
        row(p,c,3,"Copies",copies);
        c.gridx=1; c.gridy=4; p.add(add,c);

        add.addActionListener(e->{
            try {
                service.addBook(code.getText(),title.getText(),author.getText(),(Integer)copies.getValue());
                code.setText(""); title.setText(""); author.setText(""); copies.setValue(1);
                refreshBooks();
                Ui.info(this,"Book added.");
            } catch(Exception ex) { Ui.error(this,ex); }
        });
        return p;
    }

    private JPanel issuePanel() {
        JPanel p=new JPanel(new GridBagLayout());
        GridBagConstraints c=gbc();
        JTextField code=new JTextField(20);
        JTextField borrower=new JTextField(20);
        JTextField due=new JTextField(LocalDate.now().plusDays(14).toString(),20);
        JButton issue=Ui.primaryButton("Issue Book");

        row(p,c,0,"Book Code",code);
        row(p,c,1,"Borrower Name",borrower);
        row(p,c,2,"Due Date (YYYY-MM-DD)",due);
        c.gridx=1; c.gridy=3; p.add(issue,c);

        issue.addActionListener(e->{
            try {
                long id=service.issueBook(code.getText(),borrower.getText(),
                    LocalDate.parse(due.getText().trim()),session);
                code.setText(""); borrower.setText("");
                due.setText(LocalDate.now().plusDays(14).toString());
                refreshBooks(); refreshIssues();
                Ui.info(this,"Book issued. Issue ID: "+id);
            } catch(Exception ex) { Ui.error(this,ex); }
        });
        return p;
    }

    private JPanel issuesPanel() {
        JPanel p=new JPanel(new BorderLayout(8,8));
        JTable t=new JTable(issues);
        t.setRowHeight(28);
        t.setAutoCreateRowSorter(true);
        JButton r=Ui.primaryButton("Refresh");
        r.addActionListener(e->refreshIssues());
        JPanel top=new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(r);
        p.add(top,BorderLayout.NORTH);
        p.add(new JScrollPane(t),BorderLayout.CENTER);
        return p;
    }

    private JPanel returnPanel() {
        JPanel p=new JPanel(new GridBagLayout());
        GridBagConstraints c=gbc();
        JSpinner id=new JSpinner(new SpinnerNumberModel(1L,1L,Long.MAX_VALUE,1L));
        JButton ret=Ui.primaryButton("Return Book");
        row(p,c,0,"Issue ID",id);
        c.gridx=1; c.gridy=1; p.add(ret,c);

        ret.addActionListener(e->{
            try {
                service.returnBook(((Number)id.getValue()).longValue());
                refreshBooks(); refreshIssues();
                Ui.info(this,"Book returned.");
            } catch(Exception ex) { Ui.error(this,ex); }
        });
        return p;
    }

    private void refreshBooks() {
        try { Ui.fill(books,service.listBooks()); }
        catch(Exception ex) { Ui.error(this,ex); }
    }

    private void refreshIssues() {
        try { Ui.fill(issues,service.listIssues()); }
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
