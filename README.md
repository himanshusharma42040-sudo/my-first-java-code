# Swing Library Management System

Polished Java Swing + MySQL desktop app based on the original LMS learning code.

## Features

- Secure Admin / Librarian login
- PBKDF2-HMAC-SHA256 password hashing with per-user salt
- Role-based dashboards
- Admin: view, add, activate/deactivate librarians
- Librarian: view/add books, issue/return books, view issue history
- PreparedStatement + try-with-resources
- Transactions and row locks for issue/return operations
- Runtime DB configuration; no hard-coded MySQL password
- Working logout
- MySQL schema + dummy users + dummy books

## Demo users

| Role | Email | Password |
|---|---|---|
| Admin | `admin@library.local` | `Admin@123` |
| Librarian | `librarian@library.local` | `Library@123` |

## Requirements

- JDK 17+
- Maven 3.9+
- MySQL 8+

## Setup

```bash
mysql -u root -p < database/lms_schema.sql
```

Windows PowerShell:

```powershell
$env:LMS_DB_USER="root"
$env:LMS_DB_PASSWORD="YOUR_MYSQL_PASSWORD"
$env:LMS_DB_URL="jdbc:mysql://localhost:3306/lms_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
```

Run:

```bash
mvn clean compile
mvn exec:java
```
