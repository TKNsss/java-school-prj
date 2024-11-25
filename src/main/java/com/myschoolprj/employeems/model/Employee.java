package com.myschoolprj.employeems.model;

import java.util.Date;

public class Employee {

    private String ID;
    private String firstName;
    private String lastName;
    private String phone;
    private String gender;
    private Date dob;
    private String address;
    private String position;
    private String role;
    private int baseSalary;
    private int netSalary;    
    private float allowanceLevel;
    private float coefLevel;
    private int posID;

    // Constructor
    public Employee(String ID, String firstName, String lastName, String phone, String gender, Date dob, String address, String position, String role, int baseSalary, int netSalary, int posID) {
        this.ID = ID;  
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.address = address;        
        this.position = position;
        this.role = role;
        this.baseSalary = baseSalary;
        this.netSalary = netSalary;    
        this.posID = posID;   
    }

    // Default constructor
    public Employee() {
    }
    
    public String getID() {
        return ID;
    }
    
    public void setID(String id) {
        this.ID = id;
    }

    // Getters and Setters
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
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
    
    public int getPositionID() {
        return posID;
    }
    
    public void setPositionID(int posID) {
        this.posID = posID;
    }
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public int getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(int baseSalary) {
        this.baseSalary = baseSalary;
    }
    
    public int getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(int netSalary) {
        this.netSalary = netSalary;
    }
    
    public void setAllowanceLevel(float alLevel) {
        this.allowanceLevel = alLevel;
    }
    
    public float getAllowanceLevel() {
        return allowanceLevel;
    }
    
    public void setCoefLevel(float coefLevel) {
        this.coefLevel = coefLevel;
    }
    
    public float getCoefLevel() {
        return coefLevel;
    }
}
