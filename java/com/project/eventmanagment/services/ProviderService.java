package com.project.eventmanagment.services;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.data.IDGenerator;
import com.project.eventmanagment.models.Bill;
import com.project.eventmanagment.models.ServiceOffer;
import com.project.eventmanagment.models.ServiceProvider;
import com.project.eventmanagment.models.ServiceRequest;
import com.project.eventmanagment.models.enums.RequestStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ProviderService {
    private DataStore dataStore;
    private IDGenerator idGen;
    private ServiceRequestService reqserv;
    private ServiceOfferService offerserv;
    
    // Update constructor to accept all dependencies
    public ProviderService(DataStore dataStore, IDGenerator idGen, 
                          ServiceRequestService reqserv, ServiceOfferService offerserv) {
        this.dataStore = dataStore;
        this.idGen = idGen;
        this.reqserv = reqserv;
        this.offerserv = offerserv;
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

    public boolean completeService(int requestId, ServiceProvider provider) {
        ServiceRequest req = reqserv.findRequestById(requestId);
        if (req == null) {
            return false;
        }

        req.changeStatus(RequestStatus.FINISHED);
        return true;
    }

    public Bill createBill(int requestId, double amount, ServiceProvider provider) {
    ServiceRequest req = reqserv.findRequestById(requestId);
    if (req == null || req.getReservation() == null) return null;
    
    int billId = idGen.generateBillID();
    Bill bill = new Bill(billId, req.getReservation(), amount);
    dataStore.getBills().add(bill);
    return bill;
}
    
// View bill for a request/reservation
    public Bill viewBill(int reservationId) {
        return dataStore.getBills().stream()
                .filter(b -> b.getReservation().getReservationId() == reservationId)
                .findFirst()
                .orElse(null);
    }

    public boolean updateOfferStatus(int offerID, boolean accepted) {
        ServiceOffer offer = offerserv.findOfferById(offerID);
        if (offer == null) {
            return false;
        }
        if (accepted) {
            offer.acceptOffer();
        } else {
            offer.rejectOffer();
        }
        return true;
    }
}
