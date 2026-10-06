package com.example.lms.ui;

import com.example.lms.model.DashboardStats;
import com.example.lms.model.UserSession;
import com.example.lms.service.CollegeService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CollegeDashboard extends JFrame {
    private final UserSession session;
    private final CollegeService service = new CollegeService();

    private final DefaultTableModel studentsModel =
            Ui.model("ID", "Enrollment", "Student", "Program", "Semester", "Email", "Phone", "Status");
    private final DefaultTableModel departmentsModel =
            Ui.model("ID", "Code", "Department", "Status", "Created");
    private final DefaultTableModel programsModel =
            Ui.model("ID", "Code", "Program", "Department", "Semesters", "Status");
    private final DefaultTableModel attendanceModel =
            Ui.model("ID", "Date", "Enrollment", "Student", "Status", "Remarks");
    private final DefaultTableModel feesModel =
            Ui.model("Invoice", "Enrollment", "Student", "Title", "Amount", "Paid", "Balance", "Due", "Status");

    private final JLabel totalStudentsValue = metricValue();
    private final JLabel activeStudentsValue = metricValue();
    private final JLabel departmentsValue = metricValue();
    private final JLabel programsValue = metricValue();
    private final JLabel attendanceValue = metricValue();
    private final JLabel billedValue = metricValue();
    private final JLabel collectedValue = metricValue();
    private final JLabel outstandingValue = metricValue();

    private JComboBox<ChoiceItem> studentProgramCombo;
    private JComboBox<ChoiceItem> programDepartmentCombo;
    private JComboBox<ChoiceItem> attendanceStudentCombo;
    private JComboBox<ChoiceItem> feeStudentCombo;

    public CollegeDashboard(UserSession session) {
        super("College Desk - " + session.role());
        this.session = session;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1240, 780);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);
        setJMenuBar(buildMenu());

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Overview", overviewPanel());

        if (canManageStudents()) {
            tabs.addTab("Students", studentsPanel());
            tabs.addTab("Departments", departmentsPanel());
            tabs.addTab("Programs", programsPanel());
        }

        if (canManageAttendance()) {
            tabs.addTab("Attendance", attendancePanel());
        }

        if (canManageFees()) {
            tabs.addTab("Fees", feesPanel());
        }

        if (session.role() == UserSession.Role.ADMIN) {
            tabs.addTab("Modules", modulePanel());
        }

        setContentPane(Ui.page("College Desk", tabs));
        refreshAll();
    }

    private JMenuBar buildMenu() {
        JMenuBar bar = new JMenuBar();

        JMenu account = new JMenu("Account");
        JMenuItem signedIn = new JMenuItem("Signed in: " + session.fullName() + " (" + session.role() + ")");
        signedIn.setEnabled(false);

        JMenuItem refresh = new JMenuItem("Refresh all");
        refresh.addActionListener(e -> refreshAll());

        JMenuItem logout = new JMenuItem("Logout");
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        account.add(signedIn);
        account.add(refresh);
        account.addSeparator();
        account.add(logout);
        bar.add(account);

        return bar;
    }

    private JPanel overviewPanel() {
        JPanel grid = new JPanel(new GridLayout(2, 4, 14, 14));
        grid.setBorder(new EmptyBorder(18, 0, 18, 0));
        grid.add(metricCard("Total Students", totalStudentsValue));
        grid.add(metricCard("Active Students", activeStudentsValue));
        grid.add(metricCard("Departments", departmentsValue));
        grid.add(metricCard("Programs", programsValue));
        grid.add(metricCard("Attendance Today", attendanceValue));
        grid.add(metricCard("Fees Billed", billedValue));
        grid.add(metricCard("Fees Collected", collectedValue));
        grid.add(metricCard("Outstanding", outstandingValue));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(grid, BorderLayout.NORTH);

        JTextArea note = new JTextArea("""
                College Desk is running in lightweight desktop mode.
                Core modules share MySQL over JDBC; screens load data only when refreshed.
                Future modules such as exams, timetable, payroll, hostel and transport can be added without a heavy server framework.
                """);
        note.setEditable(false);
        note.setLineWrap(true);
        note.setWrapStyleWord(true);
        note.setOpaque(false);
        note.setFont(new Font("SansSerif", Font.PLAIN, 14));
        note.setBorder(new EmptyBorder(20, 8, 8, 8));
        wrapper.add(note, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel studentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JTextField enrollment = new JTextField(12);
        JTextField firstName = new JTextField(12);
        JTextField lastName = new JTextField(12);
        studentProgramCombo = new JComboBox<>();
        JSpinner semester = new JSpinner(new SpinnerNumberModel(1, 1, 16, 1));
        JTextField email = new JTextField(14);
        JTextField phone = new JTextField(11);
        JButton add = Ui.primaryButton("Add Student");
        JButton refresh = Ui.primaryButton("Refresh");

        JPanel form = compactForm(
                labeled("Enrollment", enrollment),
                labeled("First Name", firstName),
                labeled("Last Name", lastName),
                labeled("Program", studentProgramCombo),
                labeled("Semester", semester),
                labeled("Email", email),
                labeled("Phone", phone),
                add,
                refresh
        );

        JTable table = table(studentsModel);

        add.addActionListener(e -> {
            ChoiceItem program = selected(studentProgramCombo, "program");
            if (program == null) return;
            try {
                service.addStudent(
                        enrollment.getText(),
                        firstName.getText(),
                        lastName.getText(),
                        program.id(),
                        (Integer) semester.getValue(),
                        email.getText(),
                        phone.getText()
                );
                enrollment.setText("");
                firstName.setText("");
                lastName.setText("");
                email.setText("");
                phone.setText("");
                semester.setValue(1);
                refreshStudents();
                refreshChoices();
                refreshStats();
                Ui.info(this, "Student added.");
            } catch (Exception ex) {
                Ui.error(this, ex);
            }
        });

        refresh.addActionListener(e -> {
            refreshStudents();
            refreshChoices();
            refreshStats();
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel departmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JTextField code = new JTextField(10);
        JTextField name = new JTextField(22);
        JButton add = Ui.primaryButton("Add Department");
        JButton refresh = Ui.primaryButton("Refresh");

        JPanel form = compactForm(
                labeled("Code", code),
                labeled("Department Name", name),
                add,
                refresh
        );

        add.addActionListener(e -> {
            try {
                service.addDepartment(code.getText(), name.getText());
                code.setText("");
                name.setText("");
                refreshDepartments();
                refreshChoices();
                refreshStats();
                Ui.info(this, "Department added.");
            } catch (Exception ex) {
                Ui.error(this, ex);
            }
        });

        refresh.addActionListener(e -> {
            refreshDepartments();
            refreshChoices();
            refreshStats();
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(table(departmentsModel)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel programsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        programDepartmentCombo = new JComboBox<>();
        JTextField code = new JTextField(10);
        JTextField name = new JTextField(20);
        JSpinner semesters = new JSpinner(new SpinnerNumberModel(6, 1, 16, 1));
        JButton add = Ui.primaryButton("Add Program");
        JButton refresh = Ui.primaryButton("Refresh");

        JPanel form = compactForm(
                labeled("Department", programDepartmentCombo),
                labeled("Code", code),
                labeled("Program Name", name),
                labeled("Semesters", semesters),
                add,
                refresh
        );

        add.addActionListener(e -> {
            ChoiceItem department = selected(programDepartmentCombo, "department");
            if (department == null) return;
            try {
                service.addProgram(
                        department.id(),
                        code.getText(),
                        name.getText(),
                        (Integer) semesters.getValue()
                );
                code.setText("");
                name.setText("");
                refreshPrograms();
                refreshChoices();
                refreshStats();
                Ui.info(this, "Program added.");
            } catch (Exception ex) {
                Ui.error(this, ex);
            }
        });

        refresh.addActionListener(e -> {
            refreshPrograms();
            refreshChoices();
            refreshStats();
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(table(programsModel)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel attendancePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        attendanceStudentCombo = new JComboBox<>();
        JTextField date = new JTextField(LocalDate.now().toString(), 10);
        JComboBox<String> status = new JComboBox<>(new String[]{"PRESENT", "ABSENT", "LATE", "LEAVE"});
        JTextField remarks = new JTextField(18);
        JButton save = Ui.primaryButton("Save Attendance");
        JButton refresh = Ui.primaryButton("Refresh");

        JPanel form = compactForm(
                labeled("Student", attendanceStudentCombo),
                labeled("Date", date),
                labeled("Status", status),
                labeled("Remarks", remarks),
                save,
                refresh
        );

        save.addActionListener(e -> {
            ChoiceItem student = selected(attendanceStudentCombo, "student");
            if (student == null) return;
            try {
                service.markAttendance(
                        student.id(),
                        LocalDate.parse(date.getText().trim()),
                        String.valueOf(status.getSelectedItem()),
                        remarks.getText()
                );
                remarks.setText("");
                refreshAttendance();
                refreshStats();
                Ui.info(this, "Attendance saved.");
            } catch (Exception ex) {
                Ui.error(this, ex);
            }
        });

        refresh.addActionListener(e -> {
            refreshAttendance();
            refreshChoices();
            refreshStats();
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(table(attendanceModel)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel feesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        feeStudentCombo = new JComboBox<>();
        JTextField title = new JTextField("Semester Fee", 14);
        JTextField amount = new JTextField(10);
        JTextField due = new JTextField(LocalDate.now().plusDays(30).toString(), 10);
        JButton invoice = Ui.primaryButton("Create Invoice");

        JSpinner invoiceId = new JSpinner(new SpinnerNumberModel(1L, 1L, Long.MAX_VALUE, 1L));
        JTextField paymentAmount = new JTextField(9);
        JComboBox<String> mode = new JComboBox<>(new String[]{"CASH", "UPI", "CARD", "BANK"});
        JTextField reference = new JTextField(10);
        JButton payment = Ui.primaryButton("Record Payment");
        JButton refresh = Ui.primaryButton("Refresh");

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(compactForm(
                labeled("Student", feeStudentCombo),
                labeled("Fee Title", title),
                labeled("Amount", amount),
                labeled("Due Date", due),
                invoice
        ));
        top.add(compactForm(
                labeled("Invoice ID", invoiceId),
                labeled("Payment", paymentAmount),
                labeled("Mode", mode),
                labeled("Reference", reference),
                payment,
                refresh
        ));

        invoice.addActionListener(e -> {
            ChoiceItem student = selected(feeStudentCombo, "student");
            if (student == null) return;
            try {
                service.createFeeInvoice(
                        student.id(),
                        title.getText(),
                        new BigDecimal(amount.getText().trim()),
                        LocalDate.parse(due.getText().trim())
                );
                amount.setText("");
                refreshFees();
                refreshStats();
                Ui.info(this, "Fee invoice created.");
            } catch (Exception ex) {
                Ui.error(this, ex);
            }
        });

        payment.addActionListener(e -> {
            try {
                service.recordFeePayment(
                        ((Number) invoiceId.getValue()).longValue(),
                        new BigDecimal(paymentAmount.getText().trim()),
                        String.valueOf(mode.getSelectedItem()),
                        reference.getText()
                );
                paymentAmount.setText("");
                reference.setText("");
                refreshFees();
                refreshStats();
                Ui.info(this, "Payment recorded.");
            } catch (Exception ex) {
                Ui.error(this, ex);
            }
        });

        refresh.addActionListener(e -> {
            refreshFees();
            refreshChoices();
            refreshStats();
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(table(feesModel)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel modulePanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 14, 14));
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        panel.add(moduleCard(
                "Library Operations",
                "Books, issue/return and issued-book history.",
                () -> new LibrarianDashboard(session).setVisible(true)
        ));

        panel.add(moduleCard(
                "Library Staff",
                "Create and activate/deactivate librarian accounts.",
                () -> new AdminDashboard(session).setVisible(true)
        ));

        panel.add(comingSoon("Exams & Results", "Marks, grading, result processing and transcripts."));
        panel.add(comingSoon("Timetable", "Classes, rooms, faculty allocation and timetable."));
        panel.add(comingSoon("Admissions", "Enquiries, applications, document checklist and admission."));
        panel.add(comingSoon("Payroll", "Staff salary structures and payroll."));
        panel.add(comingSoon("Hostel", "Rooms, allocations and hostel fees."));
        panel.add(comingSoon("Transport", "Routes, vehicles, stops and transport fees."));

        return panel;
    }

    private JPanel moduleCard(String title, String description, Runnable action) {
        JPanel card = basicCard();
        JLabel heading = new JLabel(title);
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        JTextArea body = smallText(description);
        JButton open = Ui.primaryButton("Open Module");
        open.addActionListener(e -> action.run());
        card.add(heading, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        card.add(open, BorderLayout.SOUTH);
        return card;
    }

    private JPanel comingSoon(String title, String description) {
        JPanel card = basicCard();
        JLabel heading = new JLabel(title + "  •  R&D");
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        card.add(heading, BorderLayout.NORTH);
        card.add(smallText(description), BorderLayout.CENTER);
        return card;
    }

    private JPanel basicCard() {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 232)),
                new EmptyBorder(16, 16, 16, 16)
        ));
        card.setBackground(Color.WHITE);
        return card;
    }

    private JTextArea smallText(String text) {
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        return area;
    }

    private JPanel metricCard(String title, JLabel value) {
        JPanel card = basicCard();
        JLabel label = new JLabel(title);
        label.setForeground(Color.DARK_GRAY);
        card.add(label, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        return card;
    }

    private static JLabel metricValue() {
        JLabel label = new JLabel("—");
        label.setFont(new Font("SansSerif", Font.BOLD, 25));
        label.setForeground(Ui.PRIMARY);
        return label;
    }

    private JTable table(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(27);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        return table;
    }

    private JPanel compactForm(Component... components) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        for (Component component : components) panel.add(component);
        return panel;
    }

    private JPanel labeled(String label, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(l);
        panel.add(Box.createVerticalStrut(3));
        panel.add(field);
        return panel;
    }

    private ChoiceItem selected(JComboBox<ChoiceItem> combo, String label) {
        ChoiceItem item = (ChoiceItem) combo.getSelectedItem();
        if (item == null) {
            Ui.info(this, "No " + label + " available. Refresh or create one first.");
        }
        return item;
    }

    private void refreshAll() {
        refreshStats();
        if (canManageStudents()) {
            refreshStudents();
            refreshDepartments();
            refreshPrograms();
        }
        if (canManageAttendance()) refreshAttendance();
        if (canManageFees()) refreshFees();
        refreshChoices();
    }

    private void refreshStats() {
        try {
            DashboardStats stats = service.dashboardStats();
            totalStudentsValue.setText(String.valueOf(stats.totalStudents()));
            activeStudentsValue.setText(String.valueOf(stats.activeStudents()));
            departmentsValue.setText(String.valueOf(stats.departments()));
            programsValue.setText(String.valueOf(stats.programs()));
            attendanceValue.setText(String.valueOf(stats.attendanceToday()));
            billedValue.setText(money(stats.feesBilled()));
            collectedValue.setText(money(stats.feesCollected()));
            outstandingValue.setText(money(stats.feesOutstanding()));
        } catch (Exception ex) {
            totalStudentsValue.setText("DB v2 required");
        }
    }

    private String money(BigDecimal amount) {
        return "₹ " + amount.setScale(2);
    }

    private void refreshStudents() {
        try { Ui.fill(studentsModel, service.listStudents()); }
        catch (Exception ex) { Ui.error(this, ex); }
    }

    private void refreshDepartments() {
        try { Ui.fill(departmentsModel, service.listDepartments()); }
        catch (Exception ex) { Ui.error(this, ex); }
    }

    private void refreshPrograms() {
        try { Ui.fill(programsModel, service.listPrograms()); }
        catch (Exception ex) { Ui.error(this, ex); }
    }

    private void refreshAttendance() {
        try { Ui.fill(attendanceModel, service.listRecentAttendance()); }
        catch (Exception ex) { Ui.error(this, ex); }
    }

    private void refreshFees() {
        try { Ui.fill(feesModel, service.listFees()); }
        catch (Exception ex) { Ui.error(this, ex); }
    }

    private void refreshChoices() {
        if (programDepartmentCombo != null) {
            fillChoices(programDepartmentCombo, safeDepartmentChoices());
        }
        if (studentProgramCombo != null) {
            fillChoices(studentProgramCombo, safeProgramChoices());
        }
        if (attendanceStudentCombo != null) {
            fillChoices(attendanceStudentCombo, safeStudentChoices());
        }
        if (feeStudentCombo != null) {
            fillChoices(feeStudentCombo, safeStudentChoices());
        }
    }

    private List<Object[]> safeDepartmentChoices() {
        try { return service.departmentChoices(); }
        catch (Exception ignored) { return List.of(); }
    }

    private List<Object[]> safeProgramChoices() {
        try { return service.programChoices(); }
        catch (Exception ignored) { return List.of(); }
    }

    private List<Object[]> safeStudentChoices() {
        try { return service.studentChoices(); }
        catch (Exception ignored) { return List.of(); }
    }

    private void fillChoices(JComboBox<ChoiceItem> combo, List<Object[]> rows) {
        Object previous = combo.getSelectedItem();
        combo.removeAllItems();
        for (Object[] row : rows) {
            combo.addItem(new ChoiceItem(((Number) row[0]).longValue(), String.valueOf(row[1])));
        }
        if (previous != null) combo.setSelectedItem(previous);
    }

    private boolean canManageStudents() {
        return session.role() == UserSession.Role.ADMIN || session.role() == UserSession.Role.OFFICE;
    }

    private boolean canManageAttendance() {
        return session.role() == UserSession.Role.ADMIN ||
                session.role() == UserSession.Role.OFFICE ||
                session.role() == UserSession.Role.FACULTY;
    }

    private boolean canManageFees() {
        return session.role() == UserSession.Role.ADMIN ||
                session.role() == UserSession.Role.ACCOUNTANT ||
                session.role() == UserSession.Role.OFFICE;
    }

    private record ChoiceItem(long id, String label) {
        @Override public String toString() { return label; }
    }
}