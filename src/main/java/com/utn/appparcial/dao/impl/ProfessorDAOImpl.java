package com.utn.appparcial.dao.impl;

import com.utn.appparcial.dao.ProfessorDAO;
import com.utn.appparcial.model.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAOImpl implements ProfessorDAO {

    private final Connection connection;

    public ProfessorDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Professor professor) {
        try {
            connection.setAutoCommit(false);

            String userSql = "INSERT INTO app_user (name, last_name, email, password) VALUES (?, ?, ?, ?)";
            try (PreparedStatement userStmt = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, professor.getName());
                userStmt.setString(2, professor.getLastName());
                userStmt.setString(3, professor.getEmail());
                userStmt.setString(4, professor.getPassword());
                userStmt.executeUpdate();

                ResultSet rs = userStmt.getGeneratedKeys();
                if (rs.next()) {
                    professor.setId(rs.getLong(1));
                }
            }

            String profSql = "INSERT INTO professor (id) VALUES (?)";
            try (PreparedStatement stmt = connection.prepareStatement(profSql)) {
                stmt.setLong(1, professor.getId());
                stmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {}
        }
    }

    @Override
    public Professor findById(Long id) {
        String sql = "SELECT u.* FROM app_user u JOIN professor p ON u.id = p.id WHERE u.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Professor(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Professor> findAll() {
        List<Professor> professors = new ArrayList<>();
        String sql = "SELECT u.* FROM app_user u JOIN professor p ON u.id = p.id";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                professors.add(new Professor(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return professors;
    }

    @Override
    public void update(Professor professor) {
        String sql = "UPDATE app_user SET name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, professor.getName());
            stmt.setString(2, professor.getLastName());
            stmt.setString(3, professor.getEmail());
            stmt.setString(4, professor.getPassword());
            stmt.setLong(5, professor.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        try {
            connection.setAutoCommit(false);

            String profSql = "DELETE FROM professor WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(profSql)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }

            String userSql = "DELETE FROM app_user WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(userSql)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {}
        }
    }

    @Override
    public Professor findByEmailAndPassword(String email, String password) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT u.id, u.name, u.last_name, u.email, u.password " +
                        "FROM app_user u JOIN professor s ON u.id = s.id " +
                        "WHERE u.email = ? AND u.password = ?")) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Professor(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

