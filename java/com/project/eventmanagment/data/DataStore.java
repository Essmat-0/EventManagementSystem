/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.eventmanagment.data;
import com.project.eventmanagment.models.*;

import java.util.ArrayList;
public class DataStore {
    private final ArrayList<Reservation> reservations = new ArrayList<>();
    private final ArrayList<Customer> customers = new ArrayList<>();
    private final ArrayList<ProjectManager> pms = new ArrayList<>();
    private final ArrayList<ServiceProvider> providers = new ArrayList<>();
    private final ArrayList<Event> events = new ArrayList<>();
    private final ArrayList<ServiceRequest> requests = new ArrayList<>();
    private final ArrayList<ServiceOffer> offers = new ArrayList<>();
    private final ArrayList<Bill> bills = new ArrayList<>();

    private final ArrayList<Admin> admins = new ArrayList<>();
    
    
    public ArrayList<Customer> getCustomers() {
        return customers;
    }
    public ArrayList<ProjectManager> getPms() {
        return pms;
    }
    public ArrayList<ServiceProvider> getProviders() {
        return providers;
    }
    public ArrayList<Event> getEvents() {
        return events;
    }
    public ArrayList<ServiceRequest> getRequests() {
        return requests;
    }
    public ArrayList<ServiceOffer> getOffers() {
        return offers;
    }
    public ArrayList<Bill> getBills() {
        return bills;
    }
    public Event findEventById(int eventId) {
    for (Event e : events) {
        if (e.getEventId() == eventId) {
            return e;
        }
    }
    return null;
}
    
    public Customer findCustomerById(int customerId) {
    for (Customer c : customers) {
        if (c.getId() == customerId) {
            return c;
        }
    }
    return null;
 
}
    public ServiceProvider findProviderByID(int providerID) {
        return providers.stream().filter(p -> p.getId() == providerID).findFirst().orElse(null);
    }
    public ArrayList<Reservation> getReservations() {
        return reservations;
    }
    public ArrayList<Admin> getAdmins() {
        return admins;
    }
    public Reservation findReservationById(int resId) {
        return reservations.stream().filter(r -> r.getReservationId() == resId).findFirst().orElse(null);
    }
     public ServiceRequest findRequestsById(int resId) {
        return requests.stream().filter(r -> r.getRequestID() == resId).findFirst().orElse(null);
    }
    public ProjectManager findPMById(int pmId) {
        return pms.stream().filter(pm -> pm.getId()== pmId).findFirst().orElse(null);
    }
}