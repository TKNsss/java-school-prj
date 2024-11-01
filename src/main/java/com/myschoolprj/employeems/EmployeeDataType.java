package com.myschoolprj.employeems;

import java.io.Serializable;
import java.util.Date;

public class EmployeeDataType implements Serializable {
    
    private String ID;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String position;
    private String gender;
    private Date dateOfbirth;
    private double salary;
    private int age;

    // Constructor
    public EmployeeDataType(String ID, String firstName, String lastName, String phone, String address, String position, String gender, Date dateOfbirth, double salary, int age) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.position = position;
        this.salary = salary;
        this.age = age;
        this.dateOfbirth = dateOfbirth;
        this.gender = gender;
        
    }
    // Default constructor
    public EmployeeDataType() {
    }

    // Getters and Setters
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
     public Date getDateOfBirth() {
        return dateOfbirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfbirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}