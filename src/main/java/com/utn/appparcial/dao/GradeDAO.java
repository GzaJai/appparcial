package com.utn.appparcial.dao;

import com.utn.appparcial.model.Grade;

import java.util.List;

public interface GradeDAO {
    void save(Grade grade);
    Grade findById(Long id);
    List<Grade> findAll();
    List<Grade> findByStudentId(Long studentId);
    List<Grade> findByExamId(Long examId);
    void update(Grade grade);
    void delete(Long id);
    List<Grade> findByExamIdAndStudentId(Long id, long studentId);
    void printGradesWithDetailsByStudent(long studentId);
    void printSubjectsByStudent(long studentId);
}
