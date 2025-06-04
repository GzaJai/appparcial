package com.utn.appparcial.dao;

import com.utn.appparcial.model.Student;
import java.util.List;

public interface StudentDAO {
    void save(Student student);
    Student findById(Long id);
    List<Student> findAll();
    void update(Student student);
    void delete(Long id);
}
