package com.utn.appparcial.dao;

import java.util.List;

public interface ProfessorSubjectDAO {
    void assignSubjectToProfessor(long professorId, long subjectId);
    void removeSubjectFromProfessor(long professorId, long subjectId);
    List<Long> findSubjectIdsByProfessor(long professorId);
    boolean isSubjectAssignedToProfessor(long professorId, long subjectId);
}
