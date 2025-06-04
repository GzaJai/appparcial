package com.utn.appparcial.dao;

import com.utn.appparcial.model.Professor;
import java.util.List;

public interface ProfessorDAO {
    void save(Professor professor);
    Professor findById(Long id);
    List<Professor> findAll();
    void update(Professor professor);
    void delete(Long id);
}
