# College Desk - Lightweight Swing College ERP

This repository now contains a lightweight Java Swing + MySQL desktop foundation designed to grow from a library app into a sellable college-management product.

## Current modules

- Secure role-based login
- College dashboard
- Departments
- Programs
- Student master
- Attendance
- Fee invoices and payments
- Library management
- Librarian account management
- Dashboard KPIs
- Audit-log database foundation

## Roles

- ADMIN
- LIBRARIAN
- FACULTY
- ACCOUNTANT
- OFFICE

## Demo login

Base schema creates:

| Role | Email | Password |
|---|---|---|
| Admin | `admin@library.local` | `Admin@123` |
| Librarian | `librarian@library.local` | `Library@123` |

Demo credentials are for local development only.

## Requirements

- JDK 17+
- MySQL 8+
- Maven 3.9+ recommended

The application intentionally avoids heavy server frameworks.

## Database setup

Fresh install:

```bash
mysql -u root -p < database/lms_schema.sql
mysql -u root -p < database/college_v2.sql
```

The second script upgrades the library database with college ERP tables and additional roles.

## Database connection

Windows PowerShell:

```powershell
$env:LMS_DB_USER="root"
$env:LMS_DB_PASSWORD="YOUR_MYSQL_PASSWORD"
$env:LMS_DB_URL="jdbc:mysql://localhost:3306/lms_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
```

## Run

With Maven:

```bash
mvn clean compile
mvn exec:java
```

## Lightweight architecture

```text
Swing UI
  -> small service classes
      -> JDBC PreparedStatement
          -> MySQL
```

No Spring Boot application server and no ORM is required. Database calls use short-lived connections and transaction boundaries where consistency matters.

## Product roadmap

See `docs/PRODUCT_ROADMAP.md`.

The roadmap covers exams/results, timetable, admissions, payroll, hostel, transport, reports, backup/restore, installer, licensing and optional connected services.