package com.utn.appparcial.app;

import com.utn.appparcial.dao.*;
import com.utn.appparcial.dao.impl.*;
import com.utn.appparcial.model.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {

    private final Connection connection;
    private final StudentDAO studentDAO;
    private final ProfessorDAO professorDAO;
    private final SubjectDAO subjectDAO;
    private final GradeDAO gradeDAO;
    private final Scanner scanner;

    public ConsoleApp(Connection connection) {
        this.connection = connection;
        this.studentDAO = new StudentDAOImpl(connection);
        this.professorDAO = new ProfessorDAOImpl(connection);
        this.subjectDAO = new SubjectDAOImpl(connection);
        this.gradeDAO = new GradeDAOImpl(connection);
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("=== Bienvenido al sistema de gestión de exámenes ===");
        System.out.println("1. Ingresar como Profesor");
        System.out.println("2. Ingresar como Alumno");
        int option = promptInt("Elegí una opción: ");

        switch (option) {
            case 1 -> loginAsProfessor();
            case 2 -> loginAsStudent();
            default -> System.out.println("Opción inválida.");
        }
    }

    private void loginAsProfessor() {
        System.out.println("\n-- Login Profesor --");
        String email = promptNonEmptyString("Email: ");
        String password = promptNonEmptyString("Contraseña: ");

        Professor professor = professorDAO.findByEmailAndPassword(email, password);
        if (professor != null) {
            System.out.println("Bienvenido, " + professor.getName() + " " + professor.getLastName() + "\n");
            runProfessorMenu(professor.getId());
        } else {
            System.out.println("Credenciales inválidas.\n");
        }
    }

    private void loginAsStudent() {
        System.out.println("\n-- Login Alumno --");
        String email = promptNonEmptyString("Email: ");
        String password = promptNonEmptyString("Contraseña: ");

        Student student = studentDAO.findByEmailAndPassword(email, password);
        if (student != null) {
            System.out.println("Bienvenido, " + student.getName() + " " + student.getLastName() + "\n");
            studentMenu(student.getId());
        } else {
            System.out.println("Credenciales inválidas.\n");
        }
    }

    public void runProfessorMenu(long professorId) {
        ProfessorSubjectDAO professorSubjectDAO = new ProfessorSubjectDAOImpl(connection);
        GradeDAO gradeDAO = new GradeDAOImpl(connection);
        ExamDAO examDAO = new ExamDAOImpl(connection);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Menú Profesor ---");
            System.out.println("1. Asignarme a una materia");
            System.out.println("2. Crear examen");
            System.out.println("3. Poner nota a alumno");
            System.out.println("4. Registrar nuevo alumno");
            System.out.println("5. Ver alumnos");
            System.out.println("0. Salir");

            int option = promptInt("\nElegí una opción: ");

            switch (option) {
                case 1 -> {
                    List<Subject> subjects = subjectDAO.findAll();
                    System.out.println("Materias disponibles: \n");
                    for (Subject s : subjects) {
                        System.out.println(s.getId() + ": " + s.getName());
                    }
                    System.out.println();
                    long subjectId = promptLong("Ingrese ID materia a asignarse: ");

                    if (!professorSubjectDAO.isSubjectAssignedToProfessor(professorId, subjectId)) {
                        professorSubjectDAO.assignSubjectToProfessor(professorId, subjectId);
                        System.out.println("Materia asignada correctamente.");
                    } else {
                        System.out.println("Ya estás asignado a esa materia.");
                    }
                }
                case 2 -> {
                    List<Long> subjectIds = professorSubjectDAO.findSubjectIdsByProfessor(professorId);
                    if (subjectIds.isEmpty()) {
                        System.out.println("No estás asignado a ninguna materia.");
                        break;
                    }

                    System.out.println("Materias asignadas:");
                    for (Long id : subjectIds) {
                        Subject s = subjectDAO.findById(id);
                        if (s != null) {
                            System.out.println(s.getId() + ": " + s.getName());
                        }
                    }

                    long subjectId = promptLong("Ingrese ID de la materia para el examen: ");

                    if (!professorSubjectDAO.isSubjectAssignedToProfessor(professorId, subjectId)) {
                        System.out.println("No estás asignado a esa materia.");
                        break;
                    }

                    String title = promptNonEmptyString("Ingrese título del examen: ");
                    LocalDate date = promptDate("Ingrese fecha del examen (AAAA-MM-DD): ");

                    Exam exam = new Exam(null, title, date, subjectId, professorId);
                    examDAO.save(exam);
                    System.out.println("Examen creado con ID: " + exam.getId());
                }
                case 3 -> {
                    List<Exam> exams = examDAO.findByProfessorId(professorId);
                    if (exams.isEmpty()) {
                        System.out.println("No tenés exámenes creados.");
                        break;
                    }

                    System.out.println("Tus exámenes:");
                    for (Exam ex : exams) {
                        Subject subj = subjectDAO.findById(ex.getSubjectId());
                        System.out.println(ex.getId() + ": " + ex.getTitle() + " | Fecha: " + ex.getDate() + " | Materia: " + (subj != null ? subj.getName() : "Desconocida"));
                    }

                    long examId = promptLong("Ingrese ID del examen: ");
                    Exam exam = examDAO.findById(examId);
                    if (exam == null || !professorSubjectDAO.isSubjectAssignedToProfessor(professorId, exam.getSubjectId())) {
                        System.out.println("Examen inválido o no perteneciente a tus materias.");
                        break;
                    }

                    List<Student> students = studentDAO.findBySubjectId(exam.getSubjectId());
                    if (students.isEmpty()) {
                        System.out.println("No hay alumnos inscritos en la materia del examen. No podés registrar notas.");
                        break;
                    }

                    System.out.println("Alumnos:");
                    for (Student st : students) {
                        System.out.println(st.getId() + ": " + st.getName() + " " + st.getLastName());
                    }

                    long studentId = promptLong("Ingrese ID del alumno: ");
                    double gradeValue = promptGrade("Ingrese nota (1-10): ");

                    Grade grade = new Grade(null, examId, studentId, gradeValue, null);
                    gradeDAO.save(grade);
                    System.out.println("Nota registrada correctamente.");
                }
                case 4 -> {
                    System.out.println("\n--- Registro de Alumno ---");
                    String name = promptNonEmptyString("Nombre: ");
                    String lastName = promptNonEmptyString("Apellido: ");
                    String email = promptNonEmptyString("Email: ");
                    String password = promptNonEmptyString("Contraseña: ");

                    Student student = new Student(null, name, lastName, email, password);
                    studentDAO.save(student);
                    System.out.println("\nAlumno registrado con ID: " + student.getId() + "\n");

                    List<Subject> subjects = subjectDAO.findAll();
                    System.out.println("Materias disponibles:");
                    for (Subject s : subjects) {
                        System.out.println(s.getId() + ": " + s.getName());
                    }

                    long subjectId = promptLong("Asignar materia por ID: ");
                    StudentSubjectDAO studentSubjectDAO = new StudentSubjectDAOImpl(connection);
                    studentSubjectDAO.assignStudentToSubject(student.getId(), subjectId);
                    System.out.println("Alumno asignado a la materia correctamente.");
                }
                case 5 -> {
                    List<Long> subjectIds = professorSubjectDAO.findSubjectIdsByProfessor(professorId);
                    if (subjectIds.isEmpty()) {
                        System.out.println("No estás asignado a ninguna materia.");
                        break;
                    }

                    List<Student> students = new ArrayList<>();
                    for (Long subjectId : subjectIds) {
                        for (Student s : studentDAO.findBySubjectId(subjectId)) {
                            if (students.stream().noneMatch(st -> st.getId().equals(s.getId()))) {
                                students.add(s);
                            }
                        }
                    }

                    if (students.isEmpty()) {
                        System.out.println("No hay alumnos inscritos en tus materias.");
                        break;
                    }

                    System.out.println("\n-- Alumnos inscritos en tus materias:");
                    for (Student s : students) {
                        System.out.println(s.getId() + ": " + s.getName() + " " + s.getLastName());
                    }

                    long studentId = promptLong("\nIngrese ID del alumno para ver sus notas o 0 para volver: ");
                    if (studentId == 0) break;

                    Student selectedStudent = students.stream()
                            .filter(s -> s.getId().equals(studentId))
                            .findFirst().orElse(null);

                    if (selectedStudent == null) {
                        System.out.println("Alumno no encontrado.");
                        break;
                    }

                    List<Grade> grades = new ArrayList<>();
                    for (Long subjectId : subjectIds) {
                        for (Exam ex : examDAO.findBySubjectId(subjectId)) {
                            grades.addAll(gradeDAO.findByExamIdAndStudentId(ex.getId(), studentId));
                        }
                    }

                    if (grades.isEmpty()) {
                        System.out.println("El alumno no tiene notas en tus materias.");
                    } else {
                        System.out.println("Notas del alumno:");
                        for (Grade g : grades) {
                            Exam exam = examDAO.findById(g.getExamId());
                            Subject subject = subjectDAO.findById(exam.getSubjectId());
                            System.out.println("Materia: " + (subject != null ? subject.getName() : "Desconocida")
                                    + " | Examen: " + exam.getTitle()
                                    + " | Nota: " + g.getGrade());
                        }
                    }
                }
                case 0 -> running = false;
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private void studentMenu(long studentId) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n--- Menú del Alumno ---");
            System.out.println("1. Ver materias");
            System.out.println("2. Ver notas");
            System.out.println("0. Salir");

            System.out.print("Ingrese una opción: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1" -> gradeDAO.printSubjectsByStudent(studentId);
                case "2" -> gradeDAO.printGradesWithDetailsByStudent(studentId);
                case "0" -> exit = true;
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    // metodos auxiliares para validación de entrada

    private String promptNonEmptyString(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Entrada vacía. Por favor, intentá nuevamente.");
        }
    }

    private int promptInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ser un número entero.");
            }
        }
    }

    private long promptLong(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ser un número.");
            }
        }
    }

    private double promptDouble(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ser un número con decimales.");
            }
        }
    }

    private LocalDate promptDate(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida. Usá el formato YYYY-MM-DD.");
            }
        }
    }
    private double promptGrade(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                double grade = Double.parseDouble(input);
                if (grade >= 1 && grade <= 10) {
                    return grade;
                } else {
                    System.out.println("La nota debe estar entre 1 y 10.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ser un número con decimales.");
            }
        }
    }
    private void viewEnrolledSubjects(Student student) {
        StudentSubjectDAO studentSubjectDAO = new StudentSubjectDAOImpl(connection);
        List<Subject> subjects = studentSubjectDAO.findSubjectsByStudentId(student.getId());

        if (subjects.isEmpty()) {
            System.out.println("No estás inscrito en ninguna materia.");
        } else {
            System.out.println("\nMaterias inscritas:");
            for (Subject s : subjects) {
                System.out.println("- " + s.getName());
            }
        }
    }

}
