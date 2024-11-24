package com.myschoolprj.employeems.model;

import java.io.Serializable;
import java.util.Date;

public class Employee implements Serializable {

    private String ID;
    private String firstName;
    private String lastName;
    private String phone;
    private String gender;
    private Date dob;
    private String address;
    private String position;
    private String role;
    private int roleID; // Thêm biến roleID kiểu int
    private int positionID; // Thêm biến positionID kiểu int
    private float baseSalary;
    private float netSalary;
    private String title;

    // Constructor
    public Employee(String ID, String firstName, String lastName, String phone, String gender, Date dob, String address, String position, String role, int baseSalary, int netSalary, String title) {
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
        this.title = title;
    }

    // Default constructor
    public Employee() {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public float getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(float baseSalary) {
        this.baseSalary = baseSalary;
    }

    public float getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(float netSalary) {
        this.netSalary = netSalary;
    }

    // Getter cho roleID
    public int getRoleID() {
        return roleID;
    }

    // Setter cho roleID
    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    // Getter cho positionID
    public int getPositionID() {
        return positionID;
    }

    // Setter cho positionID
    public void setPositionID(int positionID) {
        this.positionID = positionID;
    }

}
