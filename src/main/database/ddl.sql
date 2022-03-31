USE daycare;

CREATE TABLE IF NOT EXISTS staff (
     staff_ID INT AUTO_INCREMENT PRIMARY KEY,
     first_name VARCHAR(30) NOT NULL,
     last_name VARCHAR(30),
     image VARCHAR(255) DEFAULT 'file:src/main/resources/com/main/daycare_administrative_system/assets/placeholder.png',
     cpr INT(10) NOT NULL,
     company_role VARCHAR(40),
     date_of_birth DATE,
     gender ENUM('M', 'F', 'N', 'D') NOT NULL
);

CREATE TABLE IF NOT EXISTS child (
    child_ID INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30),
    image VARCHAR(255) DEFAULT 'file:src/main/resources/com/main/daycare_administrative_system/assets/placeholder.png' NOT NULL,
    cpr INT(10),
    date_of_birth DATE,
    gender ENUM('M', 'F', 'N', 'D') NOT NULL
);

CREATE TABLE IF NOT EXISTS guardian (
     guardian_ID INT AUTO_INCREMENT PRIMARY KEY,
     first_name VARCHAR(30) NOT NULL,
     last_name VARCHAR(30),
     image VARCHAR(255) DEFAULT 'file:src/main/resources/com/main/daycare_administrative_system/assets/placeholder.png',
     cpr INT(10) NOT NULL,
     date_of_birth DATE,
     gender ENUM('M', 'F', 'N', 'D') NOT NULL
    /*
    M = Male
    F = Female
    N = Non-Binary
    D = Decline to state
    */,
    address VARCHAR(100),
    child_ID INT NOT NULL,
    FOREIGN KEY (child_ID) REFERENCES child(child_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user (
  user_ID INT AUTO_INCREMENT PRIMARY KEY,
  staff_ID INT,
  user_name VARCHAR(16) UNIQUE,
  password VARCHAR(20),
  admin BOOLEAN NOT NULL,
  FOREIGN KEY (staff_ID) REFERENCES staff(staff_ID) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS workschedule (
    schedule_ID int AUTO_INCREMENT PRIMARY KEY,
    schedule_date date NOT NULL,
    shift_start time NOT NULL,
    shift_end time NOT NULL,
    staff_ID INT(10) NOT NULL,
    FOREIGN KEY(staff_ID) REFERENCES staff(staff_ID)
);

CREATE TABLE IF NOT EXISTS telephone (
 telephone_ID INT AUTO_INCREMENT PRIMARY KEY,
 guardian_ID INT,
 staff_ID INT,
 telephone_number VARCHAR(20),
 FOREIGN KEY (guardian_ID) REFERENCES guardian(guardian_ID) ON DELETE CASCADE,
 FOREIGN KEY (staff_ID) REFERENCES staff(staff_ID) ON DELETE CASCADE
);