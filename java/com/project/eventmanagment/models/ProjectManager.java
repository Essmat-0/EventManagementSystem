package com.project.eventmanagment.models;

import java.util.ArrayList;
import java.util.Map;


public class ProjectManager extends User {
    private final ArrayList <ServiceRequest> managedRequests = new ArrayList<>();

    public ProjectManager(int id, String name, String email, String phone, String password) {
        super(id, name, email, phone, password);
    }

    public ArrayList<ServiceRequest> getManagedRequests() {
        return managedRequests;
    }
    
    public void addRequest(ServiceRequest req){
    }
    
    public void removeRequest(ServiceRequest req){}
    
    public void followRequest(int requestID){}
    
    public void contactCustomer(int customerID,String message){}
    
    public Bill viewBill(int reservationID){return null;}

    @Override
    public boolean login(String email, String password) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void logout() {
    }
    @Override
    public void updateProfile(Map<String, Object> data) {
    }
    
}

