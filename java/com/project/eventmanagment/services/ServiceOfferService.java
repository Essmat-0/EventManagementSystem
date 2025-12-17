package com.project.eventmanagment.services;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.models.ServiceOffer;
import com.project.eventmanagment.models.ServiceRequest;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ServiceOfferService {
    
    private DataStore dataStore;
    
    public ServiceOfferService(DataStore dataStore) {
        this.dataStore = dataStore;
    }
    
    public ServiceOffer findOfferById(int offerId) {
        return dataStore.getOffers().stream()
               .filter(o -> o.getOfferID() == offerId)
               .findFirst()
               .orElse(null);
    }
    
    public void addOffer(ServiceOffer offer) {
        if (offer != null) {
            dataStore.getOffers().add(offer);
        }
    }
    
    public boolean removeOffer(int offerId) {
        ServiceOffer offer = findOfferById(offerId);
        if (offer != null) {
            return dataStore.getOffers().remove(offer);
        }
        return false;
    }
    
    public ArrayList<ServiceOffer> getOffersForRequest(int requestId) {
        return dataStore.getOffers().stream()
                .filter(o -> o.getRequest() != null && o.getRequest().getRequestID() == requestId)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public ArrayList<ServiceOffer> getAcceptedOffers() {
        return dataStore.getOffers().stream()
                .filter(ServiceOffer::isAccepted)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public boolean acceptOffer(int offerId) {
        ServiceOffer offer = findOfferById(offerId);
        if (offer != null) {
            offer.setAccepted(true);
            return true;
        }
        return false;
    }
}