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

            ConsoleApp consoleApp = new ConsoleApp(conn);
            consoleApp.run();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
