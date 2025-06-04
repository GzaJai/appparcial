package com.utn.appparcial;

import com.utn.appparcial.app.ConsoleApp;
import com.utn.appparcial.dao.ProfessorDAO;
import com.utn.appparcial.dao.StudentDAO;
import com.utn.appparcial.dao.impl.ProfessorDAOImpl;
import com.utn.appparcial.dao.impl.StudentDAOImpl;
import com.utn.appparcial.model.Professor;
import com.utn.appparcial.model.Student;
import com.utn.appparcial.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {

        try (Connection conn = DatabaseUtil.getConnection()) {
            DatabaseUtil.initialize(conn);
            //initializeAdminProfessor(conn);
            // createDefaultStudents(conn);

            ConsoleApp consoleApp = new ConsoleApp(conn);
            consoleApp.run();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initializeAdminProfessor(Connection connection) {
        ProfessorDAO professorDAO = new ProfessorDAOImpl(connection);

        Professor existing = professorDAO.findByEmailAndPassword("admin@utn.com", "admin123");

        if (existing == null) {
            Professor defaultProfessor = new Professor(null, "Admin", "UTN", "admin@utn.com", "admin123");
            professorDAO.save(defaultProfessor);
            System.out.println("\n\nprofesor guardaro: " + defaultProfessor.getEmail() + " " + defaultProfessor.getPassword());
            System.out.println(">>> Profesor administrador creado por defecto <<<");
        }
    }

    private static void createDefaultStudents(Connection connection) {
        StudentDAO studentDAO = new StudentDAOImpl(connection);

        List<Student> existing = studentDAO.findAll();
        if (existing.isEmpty()) {
            Student s1 = new Student(null, "Juan", "Pérez", "juan.perez@utn.com", "1234");
            Student s2 = new Student(null, "Lucía", "Gómez", "lucia.gomez@utn.com", "1234");
            studentDAO.save(s1);
            studentDAO.save(s2);

            System.out.println("\n>>> Alumnos de prueba creados por defecto <<<\n\n\n\n\n\n\n\n\n");

        }
    }

}
