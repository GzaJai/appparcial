package com.utn.appparcial.model;

import java.time.LocalDate;

public class Exam {
    private Long id;
    private String title;
    private LocalDate date;
    private Long subjectId; // FK manual

    public Exam() {}

    public Exam(Long id, String title, LocalDate date, Long subjectId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.subjectId = subjectId;
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
}
