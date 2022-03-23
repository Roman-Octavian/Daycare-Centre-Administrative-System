USE daycare;

CREATE TABLE IF NOT EXISTS telephone (
    telephone_ID INT(10) AUTO_INCREMENT PRIMARY KEY,
    telephone_number VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS guardian (
     guardian_ID INT(10) AUTO_INCREMENT PRIMARY KEY,
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
    telephone_ID INT(10),
    FOREIGN KEY (telephone_ID) REFERENCES telephone(telephone_ID)
);

CREATE TABLE IF NOT EXISTS child (
    child_ID INT(10) AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30),
    image VARCHAR(255) DEFAULT 'file:src/main/resources/com/main/daycare_administrative_system/assets/placeholder.png',
    cpr INT(10),
    date_of_birth DATE,
    gender ENUM('M', 'F', 'N', 'D') NOT NULL,
    guardian_ID INT(10),
    FOREIGN KEY (guardian_ID) REFERENCES guardian(guardian_ID)
);

CREATE TABLE IF NOT EXISTS user (
  user_ID INT(10) AUTO_INCREMENT PRIMARY KEY,
  user_name VARCHAR(16),
  password VARCHAR(20),
  admin BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS staff (
    staff_ID INT(10) AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30),
    image VARCHAR(255) DEFAULT 'file:src/main/resources/com/main/daycare_administrative_system/assets/placeholder.png',
    cpr INT(10) NOT NULL,
    company_role VARCHAR(40),
    date_of_birth DATE,
    gender ENUM('M', 'F', 'N', 'D') NOT NULL,
    telephone_ID INT(10),
    user_ID INT(10),
    FOREIGN KEY (telephone_ID) REFERENCES telephone(telephone_ID),
    FOREIGN KEY (user_ID) REFERENCES user(user_ID)
);

CREATE TABLE IF NOT EXISTS workschedule (
    schedule_ID int(10) AUTO_INCREMENT PRIMARY KEY,
    schedule_date date NOT NULL,
    shift_start time NOT NULL,
    shift_end time NOT NULL,
    staff_ID INT(10) NOT NULL,
    FOREIGN KEY(staff_ID) REFERENCES staff(staff_ID)
);

ALTER TABLE user ADD COLUMN admin BOOLEAN NOT NULL;