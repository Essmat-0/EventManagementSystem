package com.project.eventmanagment.models;

import java.time.LocalDateTime;
import com.project.eventmanagment.data.DataStore;
public class ServiceOffer {

    private boolean accepted;
    private int offerID;
    private LocalDateTime readyDate;
    private double price;
    private ServiceProvider provider;
    private ServiceRequest request;
    
    public ServiceOffer(){}
    public ServiceOffer(boolean accepted, int offerID, double price, ServiceProvider provider) {
        this.accepted = accepted;
        this.offerID = offerID;
        this.price = price;
        this.provider = provider;
    }

    public int getOfferID() {
        return offerID;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public LocalDateTime getReadyDate() {
        return readyDate;
    }

    public double getPrice() {
        return price;
    }

    public ServiceProvider getProvider() {
        return provider;
    }

    public ServiceRequest getRequest() {
        return request;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setReadyDate(LocalDateTime readyDate) {
        this.readyDate = readyDate;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProvider(ServiceProvider provider) {
        this.provider = provider;
    }

    public void setOfferID(int offerID) {
        this.offerID = offerID;
    }

    public void setRequest(ServiceRequest request) {
        this.request = request;
    }

    public void acceptOffer() {
    this.accepted = true;
    }

    public void rejectOffer() {
    this.accepted = false;
    }
    
     
    public String getOfferDetails() {
        return toString();
    }

    @Override
    public String toString() {
        return "ServiceOffer{"
                + "offerID=" + offerID
                + ", price=" + price
                + ", accepted=" + accepted
                + ", Request =" + request
                + ", readyDate=" + readyDate
                + ", provider=" + (provider != null ? provider.getProviderName(): "N/A")
                + '}';
    }

}
