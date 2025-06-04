package com.utn.appparcial.dao.impl;

import com.utn.appparcial.dao.GradeDAO;
import com.utn.appparcial.model.Grade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAOImpl implements GradeDAO {

    private final Connection conn;

    public GradeDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Grade grade) {
        String sql = "INSERT INTO grade (value, student_id, exam_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, grade.getValue());
            stmt.setLong(2, grade.getStudentId());
            stmt.setLong(3, grade.getExamId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                grade.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Grade findById(Long id) {
        String sql = "SELECT * FROM grade WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Grade(
                        rs.getLong("id"),
                        rs.getDouble("value"),
                        rs.getLong("student_id"),
                        rs.getLong("exam_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Grade> findAll() {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM grade";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                grades.add(new Grade(
                        rs.getLong("id"),
                        rs.getDouble("value"),
                        rs.getLong("student_id"),
                        rs.getLong("exam_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public List<Grade> findByStudentId(Long studentId) {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM grade WHERE student_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                grades.add(new Grade(
                        rs.getLong("id"),
                        rs.getDouble("value"),
                        rs.getLong("student_id"),
                        rs.getLong("exam_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public List<Grade> findByExamId(Long examId) {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM grade WHERE exam_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, examId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                grades.add(new Grade(
                        rs.getLong("id"),
                        rs.getDouble("value"),
                        rs.getLong("student_id"),
                        rs.getLong("exam_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public void update(Grade grade) {
        String sql = "UPDATE grade SET value = ?, student_id = ?, exam_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, grade.getValue());
            stmt.setLong(2, grade.getStudentId());
            stmt.setLong(3, grade.getExamId());
            stmt.setLong(4, grade.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM grade WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

