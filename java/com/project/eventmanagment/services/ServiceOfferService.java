
package com.project.eventmanagment.services;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.models.ServiceOffer;


public class ServiceOfferService {
    DataStore dataStore;
    public ServiceOffer findOfferById(int offerId){
        
        return dataStore.getOffers().stream()
               .filter(o -> o.getOfferID() == offerId).findFirst().orElse(null);
    }
}
