package com.utn.appparcial.model;

public class StudentSubject {
    private long studentId;
    private long subjectId;

    public StudentSubject(long studentId, long subjectId) {
        this.studentId = studentId;
        this.subjectId = subjectId;
    }

    // getters
    public long getStudentId() {
        return studentId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    // setters
    public void setStudentId(long professorId) {
        this.studentId = professorId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }
}
