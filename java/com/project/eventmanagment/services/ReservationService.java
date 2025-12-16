package com.project.eventmanagment.services;
import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.data.IDGenerator;
import com.project.eventmanagment.models.Customer;
import com.project.eventmanagment.models.Reservation;
import com.project.eventmanagment.models.Event;
import com.project.eventmanagment.models.enums.ReservationStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReservationService {
    private DataStore dataStore;
    private IDGenerator idGen;
    public ReservationService(DataStore dataStore, IDGenerator idGen) {
        this.dataStore = dataStore;
        this.idGen = idGen;
    }
    public Reservation createReservation(Event event, Customer customer) {
        int id = idGen.generateReservationID();
        String number = "Res-" + id;

        Reservation r = new Reservation(event, number, id, customer);
        r.updateStatus(ReservationStatus.PENDING);
        r.setCreatedAt(LocalDateTime.now());

        customer.addReservation(r);
        dataStore.getReservations().add(r);

        return r;
    }
    public boolean cancelReservation(int reservationID, Customer customer) {
        Reservation r = findReservationById(reservationID);
        if(r == null || r.getCustomer() != customer) return false;
        
        r.updateStatus(ReservationStatus.CANCELED);
        return true;
    }
    public Reservation findReservationById(int id) {
        for(Reservation r : dataStore.getReservations()){
            if(r.getReservationId() == id) return r;
        }
        return null;
    }
    public boolean updateReservationStatus(int reservationID, ReservationStatus status) {
        Reservation r = findReservationById(reservationID);
        if(r == null) return false;
        
        r.updateStatus(status);
        return true;
    }
    public ArrayList<Reservation> getReservationsForCustomer(Customer customer) {
        return customer.getReservation();
    }
    public Reservation viewReservation(int reservationID) {
        return findReservationById(reservationID);
    }
}
