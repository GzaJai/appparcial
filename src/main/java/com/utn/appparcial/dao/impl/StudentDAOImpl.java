package com.utn.appparcial.dao.impl;

import com.utn.appparcial.dao.StudentDAO;
import com.utn.appparcial.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    private final Connection connection;

    public StudentDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Student student) {
        try {
            connection.setAutoCommit(false);

            String userSql = "INSERT INTO user (name, last_name, email, password) VALUES (?, ?, ?, ?)";
            try (PreparedStatement userStmt = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, student.getName());
                userStmt.setString(2, student.getLastName());
                userStmt.setString(3, student.getEmail());
                userStmt.setString(4, student.getPassword());
                userStmt.executeUpdate();

                ResultSet rs = userStmt.getGeneratedKeys();
                if (rs.next()) {
                    student.setId(rs.getLong(1));
                }
            }

            String studentSql = "INSERT INTO student (id) VALUES (?)";
            try (PreparedStatement studentStmt = connection.prepareStatement(studentSql)) {
                studentStmt.setLong(1, student.getId());
                studentStmt.executeUpdate();
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
    public Student findById(Long id) {
        String sql = "SELECT u.* FROM user u JOIN student s ON u.id = s.id WHERE u.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(
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
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT u.* FROM user u JOIN student s ON u.id = s.id";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                students.add(new Student(
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
        return students;
    }

    @Override
    public void update(Student student) {
        String sql = "UPDATE user SET name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getPassword());
            stmt.setLong(5, student.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        try {
            connection.setAutoCommit(false);

            String studentSql = "DELETE FROM student WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(studentSql)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }

            String userSql = "DELETE FROM user WHERE id = ?";
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
}
