# Student Management System (Java + JDBC + MySQL)

## Project overview

A beginner-to-intermediate **console-based CRUD application** to manage student records using **Java (JDK 25+)**, **JDBC**, and **MySQL**.

This project is structured to be **beginner friendly**, **interview ready**, and **resume worthy**:
- Clean OOP design: `Student` + `StudentDAO` + `DBConnection` + `Main`
- Uses **PreparedStatement everywhere** (safe against SQL injection)
- Uses **try-with-resources** (no connection leaks)
- Input validation in the menu layer

## Features

1. Add Student
2. View All Students
3. Search Student by ID
4. Update Student
5. Delete Student
6. Exit

Each student has:
- `id` (Primary Key)
- `name`
- `department`
- `cgpa`

## Tech stack

- Java: JDK 25+
- Build: Maven
- Database: MySQL
- Data access: JDBC (`PreparedStatement`, `ResultSet`)

---

## Folder structure

```
StudentManagementSystem/
├─ pom.xml
├─ .env                # local-only secrets (ignored by git)
├─ sql/
│  └─ schema.sql       # DB + table creation
└─ src/main/java/com/studentmanagement/
   ├─ Main.java
   ├─ Student.java
   ├─ StudentDAO.java
   └─ DBConnection.java
```

---

## Database setup (MySQL)

1) Open MySQL Workbench / CLI.

2) Run the SQL in `sql/schema.sql`.

Database name: `student_management`  
Table name: `students`

---

## Configure DB connection

Edit `.env` in the project root:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

Example `DB_URL`:

`jdbc:mysql://localhost:3306/student_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`

---

## Screenshots

Add screenshots to make the repo look professional:

- Menu screen
- CRUD operation examples (add/update/delete)
- MySQL table records (`SELECT * FROM students;`)

Recommended path:

- `docs/screenshots/`

You can embed images like this after you add them:

<!--
![Menu](docs/screenshots/menu.png)
![CRUD example](docs/screenshots/crud.png)
![DB records](docs/screenshots/db-records.png)
-->

---

## How to run

### Option A: Run with Maven (recommended)

From the project folder:
- `mvn -q clean compile exec:java`

### Option B: Run via VS Code Run button

Open `src/main/java/com/studentmanagement/Main.java` and click **Run**.

---

## Expected output (sample)

When the application starts, you’ll see:

- Menu options 1-6
- After each operation, a success/error message
- Loop continues until you choose Exit

---

## Resume description (copy/paste)

**Student Management System | Java, JDBC, MySQL**
- Built a console-based CRUD application to manage student records (add/view/search/update/delete) using Java 25, JDBC, and MySQL.
- Implemented a DAO-based architecture with separation of concerns and reusable DB connection handling.
- Ensured secure database operations using PreparedStatements, input validation, and robust exception handling.

---

## Interview questions you should be ready for

1. Why do we use `PreparedStatement` instead of `Statement`?
2. What is the purpose of the DAO pattern?
3. How does try-with-resources prevent resource leaks?
4. What happens if you try to insert a duplicate primary key?
5. How would you paginate the “view all students” feature?
6. How would you add unit tests for DAO methods?
7. How would you migrate this app to Spring Boot?
8. What is the difference between `executeQuery()` and `executeUpdate()`?

---

## Future improvements

- Add unit/integration tests (Testcontainers for MySQL)
- Add logging (SLF4J + Logback)
- Add validation rules (e.g., CGPA range per university)
- Add search by name/department
- Add pagination and sorting
- Create a REST API layer (Spring Boot)
- Add connection pooling (HikariCP)

---

## GitHub upload commands

```
git init
git add .
git commit -m "Initial commit: Student Management System"
git branch -M main
git remote add origin <YOUR_REPO_URL>
git push -u origin main
```

> Note: `.env` is ignored by git so credentials are not pushed.
