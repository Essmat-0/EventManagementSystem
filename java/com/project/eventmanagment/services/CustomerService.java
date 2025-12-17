package com.project.eventmanagment.services;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.data.IDGenerator;
import com.project.eventmanagment.models.*;
import com.project.eventmanagment.models.enums.NotificationType;
import com.project.eventmanagment.models.enums.ReservationStatus;
import java.util.ArrayList;

public class CustomerService {
    
    private DataStore dataStore;
    private ReservationService reservationService;
    private IDGenerator idGen;
    private Customer customer;
    
    public CustomerService(DataStore dataStore, ReservationService reservationService, 
                          IDGenerator idGen, Customer customer) {
        this.dataStore = dataStore;
        this.reservationService = reservationService;
        this.idGen = idGen;
        this.customer = customer;
    }
    
    // =============================================================================
    // ACCOUNT MANAGEMENT
    // =============================================================================
    
    /**
     * Creates a new customer account
     * @param name Customer name
     * @param email Customer email
     * @param phone Customer phone
     * @param password Customer password
     * @return The newly created Customer object
     */
    public Customer createAccount(String name, String email, String phone, String password) {
        // Check if email already exists
        if (isEmailExists(email)) {
            System.out.println("✗ Email already exists: " + email);
            return null;
        }
        
        int newId = generateCustomerId();
        Customer newCustomer = new Customer(newId, name, email, phone, password);
        dataStore.getCustomers().add(newCustomer);
        
        System.out.println("✓ Account created successfully for: " + name);
        return newCustomer;
    }
    
    // =============================================================================
    // EVENT BROWSING
    // =============================================================================
    
    /**
     * Views all available events
     * @return List of all events
     */
    public ArrayList<Event> viewAllEvents() {
        return dataStore.getEvents();
    }
    
    /**
     * Views details of a specific event
     * @param eventId ID of the event
     * @return Event object or null if not found
     */
    public Event viewEventDetails(int eventId) {
        return dataStore.findEventById(eventId);
    }
    
    /**
     * Searches for events by title
     * @param title Event title to search for
     * @return List of matching events
     */
    public ArrayList<Event> searchEventsByTitle(String title) {
        ArrayList<Event> results = new ArrayList<>();
        for (Event event : dataStore.getEvents()) {
            if (event.getTitle().toLowerCase().contains(title.toLowerCase())) {
                results.add(event);
            }
        }
        return results;
    }
    
    // =============================================================================
    // BOOKING MANAGEMENT
    // =============================================================================
    
    /**
     * Books an event for the customer
     * @param eventDetails The event to book
     * @return The created Reservation object or null if booking fails
     */
    public Reservation bookEvent(Event eventDetails) {
        if (customer == null) {
            System.out.println("✗ No customer logged in");
            return null;
        }
        
        if (eventDetails == null) {
            System.out.println("✗ Invalid event");
            return null;
        }
        
        // Check if customer already has a reservation for this event
        for (Reservation res : customer.getReservation()) {
            if (res.getEventDetails().getEventId() == eventDetails.getEventId() 
                && res.getStatus() != ReservationStatus.CANCELED) {
                System.out.println("✗ You already have a reservation for this event");
                return null;
            }
        }
        
        Reservation newReservation = reservationService.createReservation(eventDetails, customer);
        System.out.println("✓ Event booked successfully! Reservation #" + newReservation.getReservationNumber());
        return newReservation;
    }
    
    /**
     * Views all reservations for the logged-in customer
     * @return List of customer's reservations
     */
    public ArrayList<Reservation> viewMyReservations() {
        if (customer == null) {
            System.out.println("✗ No customer logged in");
            return new ArrayList<>();
        }
        return customer.getReservation();
    }

    public Reservation viewReservation(int reservationID) {
        if (customer == null) {
            System.out.println("No customer logged in");
            return null;
        }
        
        Reservation reservation = reservationService.findReservationById(reservationID);
        
        if (reservation == null) {
            System.out.println("Reservation not found");
            return null;
        }
        
        if (reservation.getCustomer().getId() != customer.getId()) {
            System.out.println("You don't have permission to view this reservation");
            return null;
        }
        
        return reservation;
    }
    
    public boolean cancelReservation(int reservationID) {
        if (customer == null) {
            System.out.println("✗ No customer logged in");
            return false;
        }
        
        boolean success = reservationService.cancelReservation(reservationID, customer);
        
        if (success) {
            System.out.println("Reservation cancelled successfully");
        } else {
            System.out.println("Failed to cancel reservation");
        }
        
        return success;
    }
    
    public Reservation manageBooking(int reservationID) {
        return viewReservation(reservationID);
    }
    
    public void addReservation(Reservation res) {
        if (customer != null && res != null) {
            customer.addReservation(res);
        }
    }
    
    public void removeReservation(Reservation res) {
        if (customer != null && res != null) {
            customer.removeReservation(res);
        }
    }
    
    public boolean contactPM(int pmID, String message) {
        if (customer == null) {
            System.out.println("✗ No customer logged in");
            return false;
        }
        
        ProjectManager pm = dataStore.findPMById(pmID);
        
        if (pm == null) {
            System.out.println("✗ Project Manager not found");
            return false;
        }
        
        // Create notification
        int notificationId = idGen.generateNotificationID();
        Notification notification = new Notification(
            notificationId,
            pm.getEmail(),
            NotificationType.SYSTEM,
            "Message from " + customer.getName() + ": " + message
        );
        
        notification.send();
        System.out.println("✓ Message sent to PM: " + pm.getName());
        
        return true;
    }
    

    // =============================================================================
    // SERVICE REQUESTS
    // =============================================================================
    
    /**
     * Views service requests associated with customer's reservations
     * @return List of service requests
     */
    public ArrayList<ServiceRequest> viewMyServiceRequests() {
        ArrayList<ServiceRequest> customerRequests = new ArrayList<>();
        
        for (ServiceRequest request : dataStore.getRequests()) {
            if (request.getReservation() != null 
                && request.getReservation().getCustomer().getId() == customer.getId()) {
                customerRequests.add(request);
            }
        }
        
        return customerRequests;
    }
    
    /**
     * Finds service request by reservation ID
     * @param reservationID ID of the reservation
     * @return ServiceRequest object or null
     */
    private ServiceRequest findRequestByReservation(int reservationID) {
        for (ServiceRequest request : dataStore.getRequests()) {
            if (request.getReservation() != null 
                && request.getReservation().getReservationId() == reservationID) {
                return request;
            }
        }
        return null;
    }

    
    /**
     * Checks if an email already exists in the system
     * @param email Email to check
     * @return true if email exists
     */
    private boolean isEmailExists(String email) {
        for (Customer c : dataStore.getCustomers()) {
            if (c.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
    
    private int generateCustomerId() {
        int maxId = 0;
        for (Customer c : dataStore.getCustomers()) {
            if (c.getId() > maxId) {
                maxId = c.getId();
            }
        }
        return maxId + 1;
    }
    
    public Customer getCurrentCustomer() {
        return customer;
    }
 
}