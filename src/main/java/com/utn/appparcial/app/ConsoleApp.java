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
    private final Scanner scanner;

    public ConsoleApp(Connection connection) {
        this.connection = connection;
        this.studentDAO = new StudentDAOImpl(connection);
        this.professorDAO = new ProfessorDAOImpl(connection);
        this.subjectDAO = new SubjectDAOImpl(connection);
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("=== Bienvenido al sistema de gestión de exámenes ===");
        System.out.println("1. Ingresar como Profesor");
        System.out.println("2. Ingresar como Alumno");
        System.out.print("Elegí una opción: ");
        int option = Integer.parseInt(scanner.nextLine());

        switch (option) {
            case 1 -> loginAsProfessor();
            case 2 -> loginAsStudent();
            default -> System.out.println("Opción inválida.");
        }
    }

    private void loginAsProfessor() {
        System.out.println("\n-- Login Profesor --");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

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
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        Student student = studentDAO.findByEmailAndPassword(email, password);
        if (student != null) {
            System.out.println("Bienvenido, " + student.getName() + " " + student.getLastName() + "\n");
            studentMenu();
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
            System.out.println("6. Salir");
            System.out.print("\nElegí una opción: ");

            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1 -> {
                    List<Subject> subjects = subjectDAO.findAll();
                    System.out.println("Materias disponibles: \n");
                    for (Subject s : subjects) {
                        System.out.println(s.getId() + ": " + s.getName());
                    }
                    System.out.println("\n");
                    System.out.print("Ingrese ID materia a asignarse: ");
                    long subjectId = Long.parseLong(scanner.nextLine());

                    if (!professorSubjectDAO.isSubjectAssignedToProfessor(professorId, subjectId)) {
                        professorSubjectDAO.assignSubjectToProfessor(professorId, subjectId);
                        System.out.println("Materia asignada correctamente.");
                    } else {
                        System.out.println("Ya estás asignado a esa materia.");
                    }
                }
                case 2 -> {
                    List<Long> subjectIds = professorSubjectDAO.findSubjectIdsByProfessor(professorId);
                    if(subjectIds.isEmpty()) {
                        System.out.println("No estás asignado a ninguna materia.");
                        break;
                    }

                    System.out.println("Materias asignadas a vos:");
                    for(Long id : subjectIds) {
                        Subject s = subjectDAO.findById(id);
                        if(s != null) {
                            System.out.println(s.getId() + ": " + s.getName());
                        }
                    }
                    System.out.print("Ingrese ID de la materia para el examen: ");
                    long subjectId = Long.parseLong(scanner.nextLine());

                    if (!professorSubjectDAO.isSubjectAssignedToProfessor(professorId, subjectId)) {
                        System.out.println("No estás asignado a esa materia.");
                        break;
                    }

                    System.out.print("Ingrese título del examen: ");
                    String title = scanner.nextLine();

                    System.out.print("Ingrese fecha del examen (YYYY-MM-DD): ");
                    String dateStr = scanner.nextLine();
                    LocalDate date;
                    try {
                        date = LocalDate.parse(dateStr);
                    } catch (DateTimeParseException e) {
                        System.out.println("Fecha inválida.");
                        break;
                    }

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
                    System.out.print("Ingrese ID del examen: ");
                    long examId = Long.parseLong(scanner.nextLine());

                    Exam exam = examDAO.findById(examId);
                    if (exam == null) {
                        System.out.println("Examen no encontrado.");
                        break;
                    }

                    if (!professorSubjectDAO.isSubjectAssignedToProfessor(professorId, exam.getSubjectId())) {
                        System.out.println("No estás asignado a la materia de ese examen.");
                        break;
                    }

                    List<Student> students = studentDAO.findBySubjectId(exam.getSubjectId());
                    System.out.println("Alumnos para poner nota:");
                    for (Student st : students) {
                        System.out.println(st.getId() + ": " + st.getName() + " " + st.getLastName());
                    }

                    System.out.print("Ingrese ID del alumno: ");
                    long studentId = Long.parseLong(scanner.nextLine());
                    System.out.print("Ingrese nota: ");
                    double gradeValue = Double.parseDouble(scanner.nextLine());

                    Grade grade = new Grade(null, examId, studentId, gradeValue, null);
                    gradeDAO.save(grade);
                    System.out.println("Nota registrada correctamente.");
                }

                case 4 -> {
                    System.out.println("\n--- Registro de Alumno ---");
                    System.out.print("Nombre: ");
                    String name = scanner.nextLine();
                    System.out.print("Apellido: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Contraseña: ");
                    String password = scanner.nextLine();

                    Student student = new Student(null, name, lastName, email, password);
                    studentDAO.save(student);
                    System.out.println("\nAlumno registrado con ID: " + student.getId() + "\n");

                    List<Subject> subjects = subjectDAO.findAll();
                    System.out.println("Asignar al alumno a una materia: \n");
                    for (Subject s : subjects) {
                        System.out.println(s.getId() + ": " + s.getName());
                    }
                    System.out.print("Asignar materia por ID: ");
                    long subjectId = Long.parseLong(scanner.nextLine());

                    // Necesitás un DAO para eso
                    StudentSubjectDAO studentSubjectDAO = new StudentSubjectDAOImpl(connection);
                    studentSubjectDAO.assignStudentToSubject(student.getId(), subjectId);
                    System.out.println("Alumno asignado a la materia correctamente.");
                }
                case 5 -> {
                    List<Long> subjectIds = professorSubjectDAO.findSubjectIdsByProfessor(professorId);
                    if(subjectIds.isEmpty()) {
                        System.out.println("No estás asignado a ninguna materia.");
                        break;
                    }

                    // Listar alumnos de todas las materias del profesor
                    List<Student> students = new ArrayList<>();
                    for(Long subjectId : subjectIds) {
                        List<Student> studentsBySubject = studentDAO.findBySubjectId(subjectId);
                        for(Student s : studentsBySubject) {
                            if(students.stream().noneMatch(st -> st.getId().equals(s.getId()))) {
                                students.add(s);
                            }
                        }
                    }

                    if(students.isEmpty()) {
                        System.out.println("No hay alumnos inscritos en tus materias.");
                        break;
                    }

                    System.out.println("\n-- Alumnos inscritos en tus materias:");
                    for(Student s : students) {
                        System.out.println(s.getId() + ": " + s.getName() + " " + s.getLastName());
                    }
                    System.out.println("\n");

                    System.out.print("Ingrese ID del alumno para ver sus notas o 0 para volver: ");
                    long studentId = Long.parseLong(scanner.nextLine());
                    if(studentId == 0) break;

                    Student selectedStudent = students.stream()
                            .filter(s -> s.getId().equals(studentId))
                            .findFirst()
                            .orElse(null);

                    if(selectedStudent == null) {
                        System.out.println("Alumno no encontrado.");
                        break;
                    }

                    // Listar notas del alumno en las materias del profesor
                    List<Grade> grades = new ArrayList<>();
                    for(Long subjectId : subjectIds) {
                        List<Exam> exams = examDAO.findBySubjectId(subjectId);
                        for(Exam ex : exams) {
                            List<Grade> gradesByExam = gradeDAO.findByExamIdAndStudentId(ex.getId(), studentId);
                            grades.addAll(gradesByExam);
                        }
                    }

                    if(grades.isEmpty()) {
                        System.out.println("El alumno no tiene notas en tus materias.");
                    } else {
                        System.out.println("Notas del alumno:");
                        for(Grade g : grades) {
                            Exam exam = examDAO.findById(g.getExamId());
                            if(exam != null) {
                                Subject subject = subjectDAO.findById(exam.getSubjectId());
                                System.out.println("Materia: " + (subject != null ? subject.getName() : "Desconocida") +
                                        " | Examen: " + exam.getTitle() +
                                        " | Nota: " + g.getGrade());
                            }
                        }
                    }
                }
                case 6 -> running = false;
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private void studentMenu() {
        System.out.println(">> (Aquí podrías mostrar calificaciones, materias, etc.)\n");
    }

    private void createStudent() {
        System.out.print("Nombre: ");
        String name = scanner.nextLine();
        System.out.print("Apellido: ");
        String lastName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        Student student = new Student(null, name, lastName, email, password);
        studentDAO.save(student);

        System.out.println("\n>>> Alumno creado <<<\n");
    }

    private void listStudents() {
        System.out.println("\n--- Lista de alumnos ---");
        for (Student s : studentDAO.findAll()) {
            System.out.println(s.getId() + ": " + s.getName() + " " + s.getLastName());
        }
        System.out.println("--- Fin de la lista ---\n");
    }
}
