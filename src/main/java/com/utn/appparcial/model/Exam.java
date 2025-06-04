package com.utn.appparcial.model;

import java.time.LocalDate;

public class Exam {
    private Long id;
    private String title;
    private LocalDate date;
    private Long subjectId;
    private Long professorId;

    public Exam() {}

    public Exam(Long id, String title, LocalDate date, Long subjectId, Long professorId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.subjectId = subjectId;
        this.professorId = professorId;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public Long getProfessorId() {return professorId;}
    public void setProfessorId(Long professorId) {this.professorId = professorId;}
}
