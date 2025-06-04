package com.utn.appparcial.dao;

import com.utn.appparcial.model.Subject;

import java.util.List;

public interface SubjectDAO {
    void save(Subject subject);
    Subject findById(Long id);
    List<Subject> findAll();
    void update(Subject subject);
    void delete(Long id);
}