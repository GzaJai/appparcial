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
        String sql = "INSERT INTO grade (grade, student_id, exam_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, grade.getGrade());
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
                        rs.getLong("exam_id"),
                        rs.getLong("student_id"),
                        rs.getDouble("grade"),
                        rs.getString("observations")
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
                        rs.getLong("exam_id"),
                        rs.getLong("student_id"),
                        rs.getDouble("grade"),
                        rs.getString("observations")
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
                        rs.getLong("exam_id"),
                        rs.getLong("student_id"),
                        rs.getDouble("grade"),
                        rs.getString("observations")
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
                        rs.getLong("exam_id"),
                        rs.getLong("student_id"),
                        rs.getDouble("grade"),
                        rs.getString("observations")
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
            stmt.setDouble(1, grade.getGrade());
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

    @Override
    public List<Grade> findByExamIdAndStudentId(Long examId, long studentId) {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM grade WHERE exam_id = ? AND student_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, examId);
            stmt.setLong(2, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    grades.add(new Grade(
                            rs.getLong("id"),
                            rs.getLong("exam_id"),
                            rs.getLong("student_id"),
                            rs.getDouble("grade"),
                            rs.getString("observations")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error consultando las notas del alumno para el examen", e);
        }
        return grades;
    }

    @Override
    public void printGradesWithDetailsByStudent(long studentId) {
        String sql = """
            SELECT s.name AS subject_name, e.title AS exam_name, g.grade
            FROM grade g
            JOIN exam e ON g.exam_id = e.id
            JOIN subject s ON e.subject_id = s.id
            WHERE g.student_id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("\n--- Notas del alumno ---");
                while (rs.next()) {
                    String subjectName = rs.getString("subject_name");
                    String examName = rs.getString("exam_name");
                    double gradeValue = rs.getDouble("grade");

                    System.out.println("Materia: " + subjectName +
                            " | Examen: " + examName +
                            " | Nota: " + gradeValue);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving grades with details", e);
        }
    }

    public void printSubjectsByStudent(long studentId) {
        String sql = """
            SELECT s.name AS subject_name
            FROM studentSubject ss
            JOIN subject s ON ss.subject_id = s.id
            WHERE ss.student_id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("\n--- Materias del alumno ---");
                int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.println(count + ". " + rs.getString("subject_name"));
                }
                if (count == 0) {
                    System.out.println("No se encontraron materias asignadas.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving subjects for student", e);
        }
    }
}
