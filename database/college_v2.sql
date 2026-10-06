-- College Desk v2 migration
-- Run AFTER database/lms_schema.sql on existing installations.

USE lms_db;

ALTER TABLE users
  MODIFY role ENUM('ADMIN','LIBRARIAN','FACULTY','ACCOUNTANT','OFFICE') NOT NULL;

CREATE TABLE IF NOT EXISTS college_profile (
  id TINYINT UNSIGNED NOT NULL,
  college_code VARCHAR(40) NOT NULL,
  college_name VARCHAR(190) NOT NULL,
  academic_year VARCHAR(20) NOT NULL,
  phone VARCHAR(30) NULL,
  email VARCHAR(190) NULL,
  address VARCHAR(500) NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_college_code (college_code)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS departments (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  code VARCHAR(30) NOT NULL,
  name VARCHAR(150) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_department_code (code),
  UNIQUE KEY uq_department_name (name)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS programs (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  department_id BIGINT UNSIGNED NOT NULL,
  code VARCHAR(30) NOT NULL,
  name VARCHAR(180) NOT NULL,
  duration_semesters TINYINT UNSIGNED NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_program_code (code),
  KEY idx_program_department (department_id),
  CONSTRAINT fk_program_department FOREIGN KEY (department_id) REFERENCES departments(id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT chk_program_semesters CHECK (duration_semesters BETWEEN 1 AND 16)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS students (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  enrollment_no VARCHAR(50) NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL DEFAULT '',
  program_id BIGINT UNSIGNED NOT NULL,
  current_semester TINYINT UNSIGNED NOT NULL DEFAULT 1,
  email VARCHAR(190) NULL,
  phone VARCHAR(30) NULL,
  admission_date DATE NOT NULL,
  status ENUM('ACTIVE','GRADUATED','DROPPED','SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_student_enrollment (enrollment_no),
  KEY idx_students_program_status (program_id,status),
  KEY idx_students_name (first_name,last_name),
  CONSTRAINT fk_student_program FOREIGN KEY (program_id) REFERENCES programs(id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT chk_student_semester CHECK (current_semester BETWEEN 1 AND 16)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS attendance (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  student_id BIGINT UNSIGNED NOT NULL,
  attendance_date DATE NOT NULL,
  status ENUM('PRESENT','ABSENT','LATE','LEAVE') NOT NULL,
  remarks VARCHAR(255) NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_attendance_student_date (student_id,attendance_date),
  KEY idx_attendance_date_status (attendance_date,status),
  CONSTRAINT fk_attendance_student FOREIGN KEY (student_id) REFERENCES students(id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS fee_invoices (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  student_id BIGINT UNSIGNED NOT NULL,
  title VARCHAR(180) NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  due_date DATE NOT NULL,
  status ENUM('UNPAID','PARTIAL','PAID','WAIVED') NOT NULL DEFAULT 'UNPAID',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_fee_student_status (student_id,status),
  KEY idx_fee_due_status (due_date,status),
  CONSTRAINT fk_fee_student FOREIGN KEY (student_id) REFERENCES students(id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT chk_fee_amount CHECK (amount > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS fee_payments (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  invoice_id BIGINT UNSIGNED NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  payment_mode VARCHAR(40) NOT NULL,
  reference_no VARCHAR(100) NULL,
  paid_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_payment_invoice (invoice_id),
  KEY idx_payment_paid_at (paid_at),
  CONSTRAINT fk_payment_invoice FOREIGN KEY (invoice_id) REFERENCES fee_invoices(id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT chk_payment_amount CHECK (amount > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS audit_log (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id BIGINT UNSIGNED NULL,
  action VARCHAR(80) NOT NULL,
  entity_type VARCHAR(80) NOT NULL,
  entity_id VARCHAR(80) NULL,
  details VARCHAR(1000) NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_audit_user_created (user_id,created_at),
  KEY idx_audit_entity (entity_type,entity_id),
  CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES users(id)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

INSERT INTO college_profile(id,college_code,college_name,academic_year,phone,email,address)
VALUES(1,'DEMO-COLLEGE','Demo College','2026-27','0141-0000000','info@demo-college.local','Jaipur, Rajasthan')
ON DUPLICATE KEY UPDATE
  college_name=VALUES(college_name),
  academic_year=VALUES(academic_year);

INSERT INTO departments(code,name,active) VALUES
('CSE','Computer Science & Engineering',TRUE),
('ECE','Electronics & Communication Engineering',TRUE),
('MGT','Management Studies',TRUE)
ON DUPLICATE KEY UPDATE name=VALUES(name),active=VALUES(active);

INSERT INTO programs(department_id,code,name,duration_semesters,active)
SELECT d.id,'BTECH-CSE','B.Tech Computer Science',8,TRUE FROM departments d WHERE d.code='CSE'
ON DUPLICATE KEY UPDATE name=VALUES(name),duration_semesters=VALUES(duration_semesters),active=TRUE;

INSERT INTO programs(department_id,code,name,duration_semesters,active)
SELECT d.id,'BCA','Bachelor of Computer Applications',6,TRUE FROM departments d WHERE d.code='CSE'
ON DUPLICATE KEY UPDATE name=VALUES(name),duration_semesters=VALUES(duration_semesters),active=TRUE;

INSERT INTO programs(department_id,code,name,duration_semesters,active)
SELECT d.id,'BBA','Bachelor of Business Administration',6,TRUE FROM departments d WHERE d.code='MGT'
ON DUPLICATE KEY UPDATE name=VALUES(name),duration_semesters=VALUES(duration_semesters),active=TRUE;

INSERT INTO students(enrollment_no,first_name,last_name,program_id,current_semester,email,phone,status,admission_date)
SELECT '2026CSE001','Aarav','Sharma',p.id,1,'aarav@example.local','9000000001','ACTIVE','2026-08-01'
FROM programs p WHERE p.code='BTECH-CSE'
ON DUPLICATE KEY UPDATE first_name=VALUES(first_name),last_name=VALUES(last_name);

INSERT INTO students(enrollment_no,first_name,last_name,program_id,current_semester,email,phone,status,admission_date)
SELECT '2026BCA001','Ishita','Mehta',p.id,1,'ishita@example.local','9000000002','ACTIVE','2026-08-01'
FROM programs p WHERE p.code='BCA'
ON DUPLICATE KEY UPDATE first_name=VALUES(first_name),last_name=VALUES(last_name);

INSERT INTO students(enrollment_no,first_name,last_name,program_id,current_semester,email,phone,status,admission_date)
SELECT '2026BBA001','Vivaan','Jain',p.id,1,'vivaan@example.local','9000000003','ACTIVE','2026-08-01'
FROM programs p WHERE p.code='BBA'
ON DUPLICATE KEY UPDATE first_name=VALUES(first_name),last_name=VALUES(last_name);

INSERT INTO fee_invoices(student_id,title,amount,due_date,status)
SELECT s.id,'Semester 1 Tuition Fee',45000.00,'2026-10-31','UNPAID'
FROM students s
WHERE s.enrollment_no='2026CSE001'
AND NOT EXISTS (
  SELECT 1 FROM fee_invoices f WHERE f.student_id=s.id AND f.title='Semester 1 Tuition Fee'
);

INSERT INTO fee_invoices(student_id,title,amount,due_date,status)
SELECT s.id,'Semester 1 Tuition Fee',30000.00,'2026-10-31','UNPAID'
FROM students s
WHERE s.enrollment_no='2026BCA001'
AND NOT EXISTS (
  SELECT 1 FROM fee_invoices f WHERE f.student_id=s.id AND f.title='Semester 1 Tuition Fee'
);