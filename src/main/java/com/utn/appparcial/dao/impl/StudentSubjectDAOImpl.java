package com.utn.appparcial.dao.impl;

import com.utn.appparcial.dao.StudentSubjectDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentSubjectDAOImpl implements StudentSubjectDAO {
    private final Connection conn;

    public StudentSubjectDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // metodo para agregar alumno a una materia
    @Override
    public void enrollStudentInSubject(long studentId, long subjectId) {
        String sql = "INSERT INTO StudentSubject (student_id, subject_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, studentId);
            stmt.setLong(2, subjectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error agregando alumno a la materia", e);
        }
    }

    // metodo para quitar alumno de una materia
    @Override
    public void unenrollStudentInSubject(long studentId, long subjectId) {
        String sql = "DELETE FROM StudentSubject WHERE student_id = ? AND subject_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, studentId);
            stmt.setLong(2, subjectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al quitar al alumno de la materia", e);
        }
    }

    // metodo para buscar materias en donde esta el alumno
    @Override
    public List<Long> findSubjectIdsByStudent(long studentId) {
        List<Long> subjects = new ArrayList<>();
        String sql = "SELECT subject_id FROM StudentSubject WHERE student_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    subjects.add(rs.getLong("subject_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving student's subjects", e);
        }
        return subjects;
    }

    // metodo para comprobar si un alumno esta en una materia
    @Override
    public boolean isStudentEnrolledInSubject(long studentId, long subjectId) {
        String sql = "SELECT COUNT(*) FROM StudentSubject WHERE student_id = ? AND subject_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, studentId);
            stmt.setLong(2, subjectId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error comprobando si el alumno está en la materia", e);
        }
    }

    @Override
    public void assignStudentToSubject(Long studentId, long subjectId) {
        String sql = "INSERT INTO StudentSubject (student_id, subject_id) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, studentId);
            stmt.setLong(2, subjectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al asignar alumno a la materia", e);
        }
    }
}
