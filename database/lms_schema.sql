-- Swing Library Management System - MySQL 8.x
-- Demo logins:
-- Admin:     admin@library.local / Admin@123
-- Librarian: librarian@library.local / Library@123

CREATE DATABASE IF NOT EXISTS lms_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE lms_db;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  full_name VARCHAR(120) NOT NULL,
  email VARCHAR(190) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  password_salt VARCHAR(255) NOT NULL,
  role ENUM('ADMIN','LIBRARIAN') NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_users_email (email),
  KEY idx_users_role_active (role, active)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS books (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  book_code VARCHAR(40) NOT NULL,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(180) NOT NULL,
  total_copies INT UNSIGNED NOT NULL DEFAULT 1,
  available_copies INT UNSIGNED NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_books_code (book_code),
  KEY idx_books_title (title),
  CONSTRAINT chk_books_total CHECK (total_copies >= 1),
  CONSTRAINT chk_books_available CHECK (available_copies <= total_copies)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS issued_books (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  book_id BIGINT UNSIGNED NOT NULL,
  issued_to VARCHAR(150) NOT NULL,
  issued_by BIGINT UNSIGNED NOT NULL,
  issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  due_at DATE NOT NULL,
  returned_at TIMESTAMP NULL DEFAULT NULL,
  status ENUM('ISSUED','RETURNED') NOT NULL DEFAULT 'ISSUED',
  PRIMARY KEY (id),
  KEY idx_issued_status_due (status, due_at),
  CONSTRAINT fk_issued_book FOREIGN KEY (book_id) REFERENCES books(id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_issued_user FOREIGN KEY (issued_by) REFERENCES users(id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

INSERT INTO users(full_name,email,password_hash,password_salt,role,active)
VALUES
('Demo Administrator','admin@library.local','xQ2UbWFLMtexEP1O5/XAGW7MVukHeqr/DLH8rbpIyDs=','bw+NXyseejHp8G4uqfUVmQ==','ADMIN',TRUE),
('Demo Librarian','librarian@library.local','6mvmrGM+iNzSUndxnRGWRsYkHIU21QRJKuinJ/xRh4o=','CoJrn7FjhLGdn8YthdiHZQ==','LIBRARIAN',TRUE)
ON DUPLICATE KEY UPDATE
  full_name=VALUES(full_name),
  password_hash=VALUES(password_hash),
  password_salt=VALUES(password_salt),
  role=VALUES(role),
  active=VALUES(active);

INSERT INTO books(book_code,title,author,total_copies,available_copies)
VALUES
('BK-001','Effective Java','Joshua Bloch',3,3),
('BK-002','Clean Code','Robert C. Martin',2,2),
('BK-003','Head First Java','Kathy Sierra and Bert Bates',4,4),
('BK-004','Design Patterns','Erich Gamma et al.',2,2),
('BK-005','Java Concurrency in Practice','Brian Goetz',2,2),
('BK-006','The Pragmatic Programmer','Andrew Hunt and David Thomas',3,3),
('BK-007','Refactoring','Martin Fowler',2,2),
('BK-008','SQL Antipatterns','Bill Karwin',2,2)
ON DUPLICATE KEY UPDATE
  title=VALUES(title),
  author=VALUES(author),
  total_copies=VALUES(total_copies),
  available_copies=LEAST(available_copies, VALUES(total_copies));
