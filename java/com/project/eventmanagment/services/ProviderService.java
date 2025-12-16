package com.project.eventmanagment.services;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.data.IDGenerator;
import com.project.eventmanagment.models.ServiceOffer;
import com.project.eventmanagment.models.ServiceProvider;
import com.project.eventmanagment.models.ServiceRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ProviderService {

    private DataStore dataStore;
    private IDGenerator idGen;
    private ServiceRequestService reqserv;
    private ServiceOfferService offerserv;
    public ProviderService(DataStore dataStore, IDGenerator idGen) {
        this.dataStore = dataStore;
        this.idGen = idGen;
    }

    public ArrayList<ServiceRequest> viewAssignedRequests(ServiceProvider provider) {
        if (provider == null) {
            return new ArrayList<>();
        }
        return (ArrayList<ServiceRequest>) dataStore.getRequests().stream().filter(r -> r.getAssignedTo() != null && r.getAssignedTo().getId() == provider.getId())
                .collect(Collectors.toList());
    }

    public ServiceOffer submitOffer(int requestId, double price, LocalDateTime readyDate, ServiceProvider provider) {
        ServiceRequest req = reqserv.findRequestById(requestId);
        if (req == null || provider == null) {
            return null;
        }

        ServiceOffer offer = new ServiceOffer();
        offer.setOfferID(idGen.generateOfferID());
        offer.setPrice(price);
        offer.setReadyDate(readyDate);
        offer.setProvider(provider);
        offer.setAccepted(false);
        offer.setRequest(req);

        dataStore.getOffers().add(offer);
        provider.addOffer(offer);

        return offer;

    }
    
    public boolean updateOfferStatus(int offerID, boolean accepted) {
    ServiceOffer offer = offerserv.findOfferById(offerID);
    if (offer == null) return false;

    if (accepted) {
        offer.acceptOffer();
    } else {
        offer.rejectOffer();
    }
    return true;
}
}
