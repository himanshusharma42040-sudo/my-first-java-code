# College Desk Product Roadmap

## Product direction

Turn the original Library Management System into a lightweight, offline-friendly college ERP desktop product that can run on ordinary Windows PCs without a heavy application server.

### Non-negotiable engineering goals

- Java Swing + JDBC, no Spring Boot runtime.
- Fast startup and low RAM use.
- MySQL for campus-wide shared installations.
- Prepared statements, transactions and indexes by default.
- Modular services and screens so unused modules do not load at startup.
- Runtime configuration; no real credentials in Git.
- Upgrade migrations instead of destructive database rebuilds.
- Keyboard-friendly data entry for office staff.
- Export/report support added without making core screens dependent on reporting libraries.

## Phase 1 - Core college foundation

- College profile and academic year
- Departments
- Programs
- Student master
- Attendance
- Fee invoices and payments
- Existing library module
- Role foundation: Admin, Librarian, Faculty, Accountant, Office
- Dashboard KPIs
- Audit-log table foundation

## Phase 2 - Academic operations

- Subjects and curriculum
- Faculty master
- Class/section/batch
- Timetable
- Bulk attendance
- Internal assessment
- Exam schedule
- Marks and result processing
- Grade rules
- Student promotion to next semester

## Phase 3 - College operations

- Admission enquiry and application pipeline
- Document checklist
- ID cards
- Fee plans, discounts, scholarships and receipts
- Expense tracking
- Staff payroll
- Hostel
- Transport
- Inventory/assets
- Certificates and bonafide/TC workflows

## Phase 4 - Commercial readiness

- Installer
- First-run setup wizard
- Backup/restore
- Database health check
- College branding/logo
- License key / installation identity
- User permissions per module/action
- CSV import/export
- PDF/Excel reports
- Activity audit viewer
- Automatic schema migration runner
- Error log viewer
- Demo database reset option

## Phase 5 - Optional connected features

Keep these optional so the desktop app stays lightweight:

- Student/parent web portal
- WhatsApp/SMS gateway
- Email notifications
- Cloud backup
- Multi-campus sync
- Payment gateway integration

## Performance budget

- Keep application dependencies minimal.
- Do not preload large tables; paginate/search instead.
- Do not hold database connections globally.
- Use background workers for database calls that may block the Swing UI.
- Add indexes for every recurring search/filter/report path.
- Prefer simple DTO/record classes over heavy ORM frameworks.