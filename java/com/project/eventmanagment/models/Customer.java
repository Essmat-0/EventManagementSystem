package com.project.eventmanagment.models;

import java.util.Map;
import java.util.ArrayList;

public class Customer extends User {
    
    private final ArrayList <Reservation> reservations = new ArrayList<>();
    
    public Customer(int id, String name, String email, String phone, String password) {
        super(id, name, email, phone, password);
    }   

    public ArrayList<Reservation> getReservation(){
        
        return reservations;
    }
    
    public void addReservation(Reservation res){}
    
    public void removeReservation(Reservation res){}
    
    public void createAccount(){}
    
    public void bookEvent(Event eventDetails){}
    
    public void manageBooking(int reservationID){}
    public Reservation viewReservation(int reservationID){
        
        return null;
    }
    public void cancelReservation(int reservationID){}
    public void contactPM(int pmID, String message){}
    
    
    
    
    
    @Override
    public boolean login(String email, String password) {
        return true;
    }

    @Override
    public void logout() {
    }

    @Override
    public void updateProfile(Map<String, Object> data) {
    }
    
}
