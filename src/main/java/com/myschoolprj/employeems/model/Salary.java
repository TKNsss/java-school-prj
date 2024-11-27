package com.myschoolprj.employeems.model;

public class Salary {
    private int salId;
    private String emId;
    private float baseSalary;
    private float netSalary;
    private int workDay;
    private int month;
    
    public Salary(String emID, float baseSalary, float netSalary, int workDay, int month) {
        this.emId = emID;
        this.baseSalary = baseSalary;
        this.netSalary = netSalary;
        this.workDay = workDay;
        this.month = month;
    }   
    
    public Salary() {}

    // Getters and setters
    public int getSalId() { return salId; }
    public void setSalId(int salId) { this.salId = salId; }

    public String getEmId() { return emId; }
    public void setEmId(String emId) { this.emId = emId; }

    public float getBaseSalary() { return baseSalary; }
    public void setBaseSalary(float baseSalary) { this.baseSalary = baseSalary; }

    public float getNetSalary() { return netSalary; }
    public void setNetSalary(float netSalary) { this.netSalary = netSalary; }

    public int getWorkDay() { return workDay; }
    public void setWorkDay(int workDay) { this.workDay = workDay; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
}
