
package com.project.eventmanagment.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class ServiceProvider extends User{

    private final ArrayList<ServiceOffer> offers = new ArrayList<>();
    private String providerName;

    public String getProviderName() {
        return providerName;
    }

    public ServiceProvider( int id,String providerName ,String email, String phone, String password) {
        super(id, providerName, email, phone, password);
        this.providerName = providerName;
    }
    
 
    
    public ArrayList<ServiceOffer> getOffers(){return offers;}
    
    public void addOffer(ServiceOffer offer){}
    public void removeOffer(ServiceOffer offer){}
    public ArrayList<ServiceRequest> viewAssignedRequests(){return null;}
    public void submitOffer(int requestId, double price, LocalDateTime readydate){}
    public void updateOfferStatus(int offerId, boolean accepted){}
    
    public void addAssignedRequest(ServiceRequest req){
        
    }

    @Override
    public boolean login(String email, String password) {
        throw new UnsupportedOperationException("Not supported yet");
    }
    @Override
    public void logout() {
    }

    @Override
    public void updateProfile(Map<String, Object> data) {
    }
    
}
