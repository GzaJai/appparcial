package com.utn.appparcial.model;

public class Professor extends User {

    public Professor() {
    }

    public Professor(Long id, String name, String lastName, String email, String password) {
        super(id, name, lastName, email, password);
    }

    // methods
    public String toString() {
        return "Profesor{nombre" + this.getName() + ", apellido=" + this.getLastName() + ", email= " + this.getEmail() +"}";
    }
}
