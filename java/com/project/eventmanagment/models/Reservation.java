package com.project.eventmanagment.models;

import com.project.eventmanagment.models.enums.ReservationStatus;
import java.time.LocalDateTime;

public class Reservation {

    private Event eventDetails;
    private String reservationNumber;
    private int reservationId;
    private LocalDateTime createdAt;
    private ReservationStatus status;
    private Customer customer;

    public Reservation(Event eventDetails, String reservationNumber, int reservationId, Customer customer) {
        this.eventDetails = eventDetails;
        this.reservationNumber = reservationNumber;
        this.reservationId = reservationId;
        this.customer = customer;
    }


    public Event getEventDetails() {
        return eventDetails;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }
    public Customer getCustomer(){
        return customer;
    }
    public int getReservationId() {
        return reservationId;
    }

    public LocalDateTime getDate() {
        return createdAt;
    }

    public void setEventDetails(Event eventDetails) {
        this.eventDetails = eventDetails;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getReservationInfo() {
        return toString();
    }

  public void updateStatus(ReservationStatus status) {
    this.status = status;
}

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Reservation{"
                + "reservationID=" + reservationId
                + ", reservationNumber='" + reservationNumber + '\''
                + ", status=" + status
                + ", eventTitle='" + (eventDetails != null ? eventDetails.getTitle() : "N/A") + '\''
                + ", createdAt=" + createdAt
                + '}';
    }

}
