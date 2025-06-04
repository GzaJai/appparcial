package com.utn.appparcial.model;

public class Grade {
    private Long id;
    private Long examId;
    private Long studentId;
    private Double grade;
    private String observations;

    public Grade() {}

    public Grade(Long id, Long examId, Long studentId, Double grade, String observations) {
        this.id = id;
        this.examId = examId;
        this.studentId = studentId;
        this.grade = grade;
        this.observations = observations;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { this.grade = grade; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
}
