
package com.project.eventmanagment.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Bill {
private int billID;
private Reservation reservation;
private double amount;
private LocalDateTime issuedAt;

public Bill(){}

    public Bill(int billID, Reservation reservation, double amount) {
        this.billID = billID;
        this.reservation = reservation;
        this.amount = amount;
    }

    public int getBillID() {
        return billID;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }



    public void setBillID(int billID) {
        this.billID = billID;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }
    
    public double calculateTotal(){
        return 1;
    }
    public String getBillDetails(){
        return toString();
    }

    @Override
    public String toString() {
        return "Bill{"
                + "billID=" + billID
                + ", reservationID=" + (reservation != null ? reservation.getReservationId() : -1)
                + ", amount=" + amount
                + ", issuedAt=" + issuedAt
                + '}';
    }
}
