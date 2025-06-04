package com.utn.appparcial.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.tools.Server;

public class DatabaseUtil {
    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void startWebConsole() {
        try {
            Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initialize(Connection connection) {
        try (Statement stmt = connection.createStatement()) {

            String createUserTable = """
                CREATE TABLE IF NOT EXISTS app_user (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    last_name VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NOT NULL UNIQUE,
                    password VARCHAR(100) NOT NULL
                );
            """;

            String createStudentTable = """
                CREATE TABLE IF NOT EXISTS student (
                    id BIGINT PRIMARY KEY,
                    FOREIGN KEY (id) REFERENCES app_user(id)
                );
            """;

            String createProfessorTable = """
                CREATE TABLE IF NOT EXISTS professor (
                    id BIGINT PRIMARY KEY,
                    FOREIGN KEY (id) REFERENCES app_user(id)
                );
            """;

            String createSubjectTable = """
                CREATE TABLE IF NOT EXISTS subject (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL
                );
            """;

            String createStudentSubjectTable = """
                CREATE TABLE IF NOT EXISTS studentSubject (
                    student_id BIGINT,
                    subject_id BIGINT,
                    PRIMARY KEY (student_id, subject_id),
                    FOREIGN KEY (student_id) REFERENCES student(id),
                    FOREIGN KEY (subject_id) REFERENCES subject(id)
                );
            """;

            String createProfessorSubjectTable = """
                CREATE TABLE IF NOT EXISTS professorSubject (
                    professor_id BIGINT,
                    subject_id BIGINT,
                    PRIMARY KEY (professor_id, subject_id),
                    FOREIGN KEY (professor_id) REFERENCES professor(id),
                    FOREIGN KEY (subject_id) REFERENCES subject(id)
                );
            """;

            String createExamTable = """
                CREATE TABLE IF NOT EXISTS exam (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    subject_id BIGINT NOT NULL,
                    professor_id BIGINT NOT NULL,
                    title VARCHAR(100),
                    date DATE,
                    FOREIGN KEY (subject_id) REFERENCES subject(id),
                    FOREIGN KEY (professor_id) REFERENCES professor(id)
                );
            """;

            String createGradeTable = """
                CREATE TABLE IF NOT EXISTS grade (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    exam_id BIGINT NOT NULL,
                    student_id BIGINT NOT NULL,
                    grade DECIMAL(5,2),
                    observations TEXT,
                    FOREIGN KEY (exam_id) REFERENCES exam(id),
                    FOREIGN KEY (student_id) REFERENCES student(id)
                );
            """;

            String addSubjects = """
                INSERT INTO subject (id, name) VALUES
                          (1, 'Matemáticas'),
                          (2, 'Historia'),
                          (3, 'Programación'),
                          (4, 'Física');        
            """;

            stmt.execute(createUserTable);
            stmt.execute(createStudentTable);
            stmt.execute(createProfessorTable);
            stmt.execute(createSubjectTable);
            stmt.execute(createStudentSubjectTable);
            stmt.execute(createProfessorSubjectTable);
            stmt.execute(createExamTable);
            stmt.execute(createGradeTable);
            stmt.execute(addSubjects);

        } catch (SQLException e) {
            throw new RuntimeException("Error initializing database schema", e);
        }
    }
}
