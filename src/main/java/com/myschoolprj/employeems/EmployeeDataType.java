package com.myschoolprj.employeems;

import java.io.Serializable;
import java.util.Date;

public class EmployeeDataType implements Serializable {

    private String ID;
    private String status;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String position;
    private String gender;
    private Date dateOfbirth;

    // Constructor
    public EmployeeDataType(String ID, String Status, String firstName, String lastName, String gender, String phone, String address, String position, Date dateOfbirth) {
        this.ID = ID;
        this.status = Status;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.position = position;
        this.dateOfbirth = dateOfbirth;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
