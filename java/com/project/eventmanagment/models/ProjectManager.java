package com.project.eventmanagment.models;

import java.util.ArrayList;
import java.util.Map;

public class ProjectManager extends User {
    
    private final ArrayList<ServiceRequest> managedRequests = new ArrayList<>();
    
    public ProjectManager(int id, String name, String email, String phone, String password) {
        super(id, name, email, phone, password);
    }
    
    public ArrayList<ServiceRequest> getManagedRequests() {
        return managedRequests;
    }
    
    public void addRequest(ServiceRequest req) {
        if (req != null && !managedRequests.contains(req)) {
            managedRequests.add(req);
        }
    }
    
    public void removeRequest(ServiceRequest req) {
        managedRequests.remove(req);
    }
    
    public ServiceRequest findManagedRequest(int requestID) {
        for (ServiceRequest req : managedRequests) {
            if (req.getRequestID() == requestID) {
                return req;
            }
        }
        return null;
    }
    
    @Override
    public boolean login(String email, String password) {
        return this.getEmail().equals(email) && this.getPassword().equals(password);
    }
    
    @Override
    public void logout() {
        System.out.println("PM " + getName() + " logged out.");
    }
    
    @Override
    public void updateProfile(Map<String, Object> data) {
        if (data.containsKey("name")) {
            setName((String) data.get("name"));
        }
        if (data.containsKey("email")) {
            setEmail((String) data.get("email"));
        }
        if (data.containsKey("phone")) {
            setPhone((String) data.get("phone"));
        }
        if (data.containsKey("password")) {
            setPassword((String) data.get("password"));
        }
    }
    
    @Override
    public String toString() {
        return "ProjectManager{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", managedRequests=" + managedRequests.size() +
                '}';
    }
}