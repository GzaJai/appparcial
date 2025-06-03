package com.utn.appparcial;

import com.utn.appparcial.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {

        try (Connection conn = DatabaseUtil.getConnection()) {
            Statement stmt = conn.createStatement();
            DatabaseUtil.startWebConsole();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
