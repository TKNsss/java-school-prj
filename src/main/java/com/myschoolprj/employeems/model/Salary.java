package com.myschoolprj.employeems.model;

public class Salary {
    private int salId;
    private String emId;
    private String firstName;
    private String lastName;
    private float baseSalary;
    private float netSalary;
    private int workDay;
    private int month;
    private int year;
    private float monthSalary;
    
    public Salary(int salId, String emId, String firstName, String lastName, int workDay, int month, int year, float baseSalary, float netSalary,  float monthSalary) {
        this.salId = salId;
        this.emId = emId;  
        this.firstName = firstName;
        this.lastName = lastName;
        this.workDay = workDay;
        this.month = month;
        this.year = year;
        this.baseSalary = baseSalary;
        this.netSalary = netSalary;  
        this.monthSalary = monthSalary;
    }

    // Getters and setters
    public int getSalId() { return salId; }
    public void setSalId(int salId) { this.salId = salId; }

    public String getEmId() { return emId; }
    public void setEmId(String emId) { this.emId = emId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public float getBaseSalary() { return baseSalary; }
    public void setBaseSalary(float baseSalary) { this.baseSalary = baseSalary; }

    public float getNetSalary() { return netSalary; }
    public void setNetSalary(float netSalary) { this.netSalary = netSalary; }
    
    public float getMonSalary() { return monthSalary; }
    public void setMonSalary(float monthSalary) { this.monthSalary = monthSalary; }

    public int getWorkDay() { return workDay; }
    public void setWorkDay(int workDay) { this.workDay = workDay; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}
