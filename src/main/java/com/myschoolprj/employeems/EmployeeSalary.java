/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myschoolprj.employeems;

import java.io.Serializable;

/**
 *
 * @author ADMIN
 */
public class EmployeeSalary implements Serializable {

    private String ID;
    private String First_name;
    private String Last_name;
    private float Salary;

    // Constructor
    public EmployeeSalary(String ID, String firstName, String lastName, float salary) {
        this.ID = ID;
        this.First_name = firstName;
        this.Last_name = lastName;
        this.Salary = salary;
    }

    // Default constructor
    public EmployeeSalary() {
    }

    // Getters and Setters
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return First_name;
    }

    public void setFirstName(String firstName) {
        this.First_name = firstName;
    }

    public String getLastName() {
        return Last_name;
    }

    public void setLastName(String lastName) {
        this.Last_name = lastName;
    }
    
    public float getSalary(){
        return Salary;
    }
    
    public void setSalary(float salary){
        this.Salary = salary;
    }
}
