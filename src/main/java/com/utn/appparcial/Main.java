package com.utn.appparcial;

import com.utn.appparcial.app.ConsoleApp;
import com.utn.appparcial.dao.ProfessorDAO;
import com.utn.appparcial.dao.impl.ProfessorDAOImpl;
import com.utn.appparcial.model.Professor;
import com.utn.appparcial.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        try (Connection conn = DatabaseUtil.getConnection()) {
            DatabaseUtil.initialize(conn);
            initializeAdminProfessor(conn);

            ConsoleApp consoleApp = new ConsoleApp(conn);
            consoleApp.run();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initializeAdminProfessor(Connection connection) {
        ProfessorDAO professorDAO = new ProfessorDAOImpl(connection);

        // Verificamos si ya existe un profesor con email conocido
        Professor existing = professorDAO.findByEmailAndPassword("admin@utn.com", "admin123");

        if (existing == null) {
            Professor defaultProfessor = new Professor(null, "Admin", "UTN", "admin@utn.com", "admin123");
            professorDAO.save(defaultProfessor);
            System.out.println("\n\nprofesor guardaro: " + defaultProfessor.getEmail() + " " + defaultProfessor.getPassword());
            System.out.println(">>> Profesor administrador creado por defecto <<<\n\n\n\n\n\n\n\n\n");
        }
    }
}
