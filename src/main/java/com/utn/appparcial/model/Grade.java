package com.utn.appparcial.model;

public class Grade {
    private Long id;
    private Double value;
    private Long studentId; // FK
    private Long examId;    // FK

    public Grade() {}

    public Grade(Long id, Double value, Long studentId, Long examId) {
        this.id = id;
        this.value = value;
        this.studentId = studentId;
        this.examId = examId;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }
}
