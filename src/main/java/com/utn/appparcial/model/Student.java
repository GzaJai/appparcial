package com.utn.appparcial.model;

public class Student extends User {

    public Student() {
    }

    public Student(Long id, String name, String lastName, String email, String password) {
        super(id, name, lastName, email, password);
    }

    // methods
    public String toString() {
        return "Alumno{nombre" + this.getName() + ", apellido=" + this.getLastName() + ", email= " + this.getEmail() +"}";
    }
}
