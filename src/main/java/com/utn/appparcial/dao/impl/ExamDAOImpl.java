package com.utn.appparcial.dao.impl;

import com.utn.appparcial.dao.ExamDAO;
import com.utn.appparcial.model.Exam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamDAOImpl implements ExamDAO {

    private final Connection conn;

    public ExamDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Exam exam) {
        String sql = "INSERT INTO exam (title, date, subject_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, exam.getTitle());
            stmt.setDate(2, Date.valueOf(exam.getDate()));
            stmt.setLong(3, exam.getSubjectId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                exam.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Exam findById(Long id) {
        String sql = "SELECT * FROM exam WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Exam(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getDate("date").toLocalDate(),
                        rs.getLong("subject_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Exam> findAll() {
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT * FROM exam";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                exams.add(new Exam(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getDate("date").toLocalDate(),
                        rs.getLong("subject_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

    @Override
    public List<Exam> findBySubjectId(Long subjectId) {
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT * FROM exam WHERE subject_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, subjectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                exams.add(new Exam(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getDate("date").toLocalDate(),
                        rs.getLong("subject_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

    @Override
    public void update(Exam exam) {
        String sql = "UPDATE exam SET title = ?, date = ?, subject_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, exam.getTitle());
            stmt.setDate(2, Date.valueOf(exam.getDate()));
            stmt.setLong(3, exam.getSubjectId());
            stmt.setLong(4, exam.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM exam WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
