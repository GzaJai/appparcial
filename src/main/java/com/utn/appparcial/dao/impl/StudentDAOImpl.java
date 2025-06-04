package com.utn.appparcial.dao.impl;

import com.utn.appparcial.dao.StudentDAO;
import com.utn.appparcial.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    private final Connection conn;

    public StudentDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Student student) {
        try {
            conn.setAutoCommit(false);

            String userSql = "INSERT INTO app_user (name, last_name, email, password) VALUES (?, ?, ?, ?)";
            try (PreparedStatement userStmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
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
            try (PreparedStatement studentStmt = conn.prepareStatement(studentSql)) {
                studentStmt.setLong(1, student.getId());
                studentStmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ignore) {}
        }
    }

    @Override
    public Student findById(Long id) {
        String sql = "SELECT u.* FROM app_user u JOIN student s ON u.id = s.id WHERE u.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        String sql = "SELECT u.* FROM app_user u JOIN student s ON u.id = s.id";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
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
        String sql = "UPDATE app_user SET name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
            conn.setAutoCommit(false);

            String studentSql = "DELETE FROM student WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(studentSql)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }

            String userSql = "DELETE FROM app_user WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(userSql)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ignore) {}
        }
    }

    @Override
    public Student findByEmailAndPassword(String email, String password) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT u.id, u.name, u.last_name, u.email, u.password " +
                        "FROM app_user u INNER JOIN student s ON u.id = s.id " +
                        "WHERE u.email = ? AND u.password = ?")) {

            stmt.setString(1, email);
            stmt.setString(2, password);

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
    public List<Student> findBySubjectId(Long subjectId) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.id, u.name, u.last_name, u.email, u.password " +
                "FROM student s " +
                "JOIN app_user u ON s.id = u.id " +
                "JOIN studentSubject ss ON s.id = ss.student_id " +
                "WHERE ss.subject_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, subjectId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(new Student(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("password")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando alumnos por materia", e);
        }
        return students;
    }

}
