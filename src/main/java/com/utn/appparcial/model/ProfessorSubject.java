package com.utn.appparcial.model;

public class ProfessorSubject {
    private long professorId;
    private long subjectId;

    public ProfessorSubject(long professorId, long subjectId) {
        this.professorId = professorId;
        this.subjectId = subjectId;
    }

    // getters
    public long getProfessorId() {
        return professorId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    // setters
    public void setProfessorId(long professorId) {
        this.professorId = professorId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }
}
