package com.project.eventmanagment.models;

import java.util.Map;

public class Admin extends User {

    private String role;

    public Admin(int id, String name, String email,
                 String phone, String password, String role) {
        super(id, name, email, phone, password);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean login(String email, String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void logout() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void updateProfile(Map<String, Object> data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}