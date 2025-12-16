package com.project.eventmanagment.models;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.models.enums.RequestStatus;
import java.time.LocalDateTime;

public class ServiceRequest {

    private String description;
    private int requestID;
    private Reservation reservation;
    private ServiceProvider assignedTo;
    private LocalDateTime createdAt;
    private ProjectManager pm;
    private RequestStatus status;

    public ServiceRequest() {
    }

    public ServiceRequest(String description, int requestID, Reservation reservation, ProjectManager pm) {
        this.description = description;
        this.requestID = requestID;
        this.reservation = reservation;
        this.pm = pm;
    }

    public String getDescription() {
        return description;
    }

    public int getRequestID() {
        return requestID;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public LocalDateTime getDate() {
        return createdAt;
    }

    public ProjectManager getPm() {
        return pm;
    }

    public ServiceProvider getAssignedTo() {
        return assignedTo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void setPm(ProjectManager pm) {
        this.pm = pm;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void assignToProvider(ServiceProvider provider) {
        this.assignedTo = provider;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void changeStatus(RequestStatus status) {
        this.status = status;
    }

    public String getRequestDetails() {
        return null;
    }

    @Override
    public String toString() {
        return "ServiceRequest{"
                + "requestID=" + requestID
                + ", description='" + description + '\''
                + ", reservationID=" + (reservation != null ? reservation.getReservationId() : -1)
                + ", status=" + status
                + ", createdAt=" + createdAt
                + ",  Assigned To="+ assignedTo
                + '}';
    }

}
