package com.utn.appparcial.dao;

import com.utn.appparcial.model.Exam;

import java.util.List;

public interface ExamDAO {
    void save(Exam exam);
    Exam findById(Long id);
    List<Exam> findAll();
    List<Exam> findBySubjectId(Long subjectId);
    void update(Exam exam);
    void delete(Long id);
    List<Exam> findByProfessorId(long professorId);
}
