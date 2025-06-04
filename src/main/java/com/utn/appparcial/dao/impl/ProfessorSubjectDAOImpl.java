package com.utn.appparcial.dao.impl;

import com.utn.appparcial.dao.ProfessorSubjectDAO;
import com.utn.appparcial.model.Professor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfessorSubjectDAOImpl implements ProfessorSubjectDAO {
    private final Connection conn;

    public ProfessorSubjectDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // metodo para asignar materias a un profesor con su id
    @Override
    public void assignSubjectToProfessor(long professorId, long subjectId) {
        String sql = "INSERT INTO ProfessorSubject (professor_id, subject_id) VALUES (?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, professorId);
            stmt.setLong(2, subjectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error asignando la materia al profesor" ,e);
        }
    }

    // metodo para remover materias a un profesor
    @Override
    public void removeSubjectFromProfessor(long professorId, long subjectId) {
        String sql = "DELETE FROM ProfessorSubject WHERE professor_id = ? AND subject_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, professorId);
            stmt.setLong(2, subjectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removiendo la materia al profesor" ,e);
        }
    }

    // metodo para buscar las materias que tiene asignadas un profesor
    @Override
    public List<Long> findSubjectIdsByProfessor(long professorId) {
        List<Long> subjects = new ArrayList<>();
        String sql = "SELECT * FROM professorSubject WHERE professor_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, professorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    subjects.add(rs.getLong("subject_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error consultando las materias del profesor" ,e);
        }
        return subjects;
    }

    // metodo para comprobar si el profesor esta asignado a una materia
    @Override
    public boolean isSubjectAssignedToProfessor(long professorId, long subjectId) {
        String sql = "SELECT COUNT(*) FROM ProfessorSubject WHERE professor_id = ? AND subject_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setLong(1, professorId);
          stmt.setLong(2, subjectId);
          try (ResultSet rs = stmt.executeQuery()) {
              return rs.next() && rs.getInt(1) > 0;
          }
        } catch (SQLException e) {
            throw new RuntimeException("Error comprobando materia asignada", e);
        }
    }

}
