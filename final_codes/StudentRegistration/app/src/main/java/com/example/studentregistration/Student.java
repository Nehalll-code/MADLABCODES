package com.example.studentregistration;

import java.io.Serializable;

public class Student implements Serializable {
    private int id;
    private String name;
    private int age;
    private String email;
    private String course;
    private String status;
    private String department;
    private String subjects;
    private String admissionDate;
    private String section;

    public Student() {}

    public Student(String name, int age, String email, String course, String status, String department, String subjects, String admissionDate, String section) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.course = course;
        this.status = status;
        this.department = department;
        this.subjects = subjects;
        this.admissionDate = admissionDate;
        this.section = section;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getSubjects() { return subjects; }
    public void setSubjects(String subjects) { this.subjects = subjects; }
    public String getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(String admissionDate) { this.admissionDate = admissionDate; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
}
