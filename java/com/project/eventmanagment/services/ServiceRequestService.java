package com.project.eventmanagment.services;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.data.IDGenerator;
import com.project.eventmanagment.models.*;
import com.project.eventmanagment.models.enums.RequestStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ServiceRequestService {

    private final DataStore dataStore;
    private final  IDGenerator idGen;

    public ServiceRequestService(DataStore dataStore, IDGenerator idGen) {
        this.dataStore = dataStore;
        this.idGen = idGen;
    }

    public ServiceRequest createRequest(String description, Reservation reservation, Customer customer, ProjectManager pm) {
        if (reservation == null || customer == null) {
            return null;
        }

        int id = idGen.generateReservationID();
        ServiceRequest req = new ServiceRequest(description, id, reservation, pm);
        req.setRequestID(id);
        req.setDescription(description);
        req.setReservation(reservation);
        req.setPm(pm);
        req.setCreatedAt(LocalDateTime.now());
        req.changeStatus(RequestStatus.PENDING);

        synchronized (dataStore.getRequests()) {
            dataStore.getRequests().add(req);
        }
        return req;
    }
    
    public ServiceRequest findRequestById(int id){
        return dataStore.getRequests()
                .stream()
                .filter( r -> r.getRequestID() == id)
                .findFirst()
                .orElse(null);
    }
    
    public boolean assignToProvider(int requestId, int providerID){
        ServiceRequest req = findRequestById(requestId);
        ServiceProvider provider = dataStore.findProviderByID(providerID);
        if(req == null || provider == null) return false;
        
        req.assignToProvider(provider);
        req.changeStatus(RequestStatus.ASSIGNED);
        provider.addAssignedRequest(req);
        
        return true;
    }

    public boolean updateStatus(int requestId, RequestStatus status){
        ServiceRequest req = findRequestById(requestId);
        if(req == null || status == null) return false;
        
        req.changeStatus(status);
        return true;
    }
    // requests managed by pm
    public ArrayList <ServiceRequest> getRequestsForPM(ProjectManager pm){
        if(pm == null) return new ArrayList<>();
        
        return (ArrayList<ServiceRequest>) dataStore.getRequests().stream().filter(r -> r.getPm() != null && r.getPm().getId() == pm.getId()).collect(Collectors.toList());
    }
    // requests assigned to provider 
    public ArrayList <ServiceRequest> getRequestForProvider(ServiceProvider provider){
        if(provider == null ) return new ArrayList<>();
        
        return (ArrayList<ServiceRequest>) dataStore.getRequests()
                .stream()
                .filter(r -> r.getAssignedTo() != null && r.getAssignedTo().getId() == provider.getId())
                .collect(Collectors.toList());

    }
    // find request by reservation id
    public ArrayList<ServiceRequest> getRequestByReservation(int reservationId){
        return (ArrayList<ServiceRequest>) dataStore.getRequests().stream().filter(r -> r.getReservation() != null && r.getReservation().getReservationId() == reservationId)
                .collect(Collectors.toList());
    }
    
    
    // Admin and Pm 
    public boolean removeRequest(int requestId){
        ServiceRequest req = findRequestById(requestId);
        if(req == null) return false;
        synchronized (dataStore.getRequests()){
            return dataStore.getRequests().remove(req);
        }
    }
}