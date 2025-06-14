package com.utn.appparcial.dao;

import com.utn.appparcial.model.StudentSubject;
import com.utn.appparcial.model.Subject;

import java.util.List;

public interface StudentSubjectDAO {
    void enrollStudentInSubject(long studentId, long subjectId);
    void unenrollStudentInSubject(long studentId, long subjectId);
    List<Subject> findSubjectsByStudentId(Long studentId);
    boolean isStudentEnrolledInSubject(long studentId, long subjectId);
    void assignStudentToSubject(Long id, long subjectId);
}
