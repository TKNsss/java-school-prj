package com.myschoolprj.employeems.model;

public class Role {
    private int roleId;
    private String emID;
    private String roleName;
    private double allowance;

    public Role(int roleId, String emID, String roleName, double allowance) {
        this.roleId = roleId;
        this.emID = emID;
        this.roleName = roleName;
        this.allowance = allowance;
    }
    
    public Role() {}

    // Getters and Setters
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getEmID() {
        return emID;
    }

    public void setEmID(String emID) {
        this.emID = emID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public double getAllowance() {
        return allowance;
    }

    public void setAllowance(double allowance) {
        this.allowance = allowance;
    }
}
