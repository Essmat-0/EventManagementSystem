package com.project.eventmanagment.models;

import java.util.ArrayList;
import java.util.Map;

public class Customer extends User {
    
    private final ArrayList<Reservation> reservations = new ArrayList<>();
    
    public Customer(int id, String name, String email, String phone, String password) {
        super(id, name, email, phone, password);
    }
    
    public ArrayList<Reservation> getReservation() {
        return reservations;
    }
    
    public void addReservation(Reservation reservation) {
        if (reservation != null && !reservations.contains(reservation)) {
            reservations.add(reservation);
        }
    }
    
    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
    }
    
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