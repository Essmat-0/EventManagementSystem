package com.project.eventmanagment.services;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.models.Bill;
import com.project.eventmanagment.models.ProjectManager;
import com.project.eventmanagment.models.ServiceRequest;
import com.project.eventmanagment.services.ServiceRequestService;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PMService {

    private DataStore dataStore;
    private ServiceRequestService reqServ;
    private BillingService billingService;
    
    
    public PMService(DataStore dataStore,ServiceRequestService reqServ){ this.dataStore = dataStore;
        this.reqServ = reqServ;}
    public PMService(DataStore dataStore,ServiceRequestService reqServ,BillingService billingService) {
        this.dataStore = dataStore;
        this.reqServ = reqServ;
        this.billingService = billingService;
    }

    public ArrayList<ServiceRequest> getRequestsForPM(ProjectManager pm) {
        if (pm == null) {
            return new ArrayList<>();
        }

        return (ArrayList<ServiceRequest>) dataStore.getRequests().stream().filter(r -> r.getPm() != null && r.getPm().getId() == pm.getId()).collect(Collectors.toList());
    }

    public void followRequest(int requestID) {
        ServiceRequest req = reqServ.findRequestById(requestID);
        System.out.println("Request Status: " + req.getStatus() + "\nCreated At: " + req.getDate() + "\nAssigned To: " + req.getAssignedTo());

    }

    public void forwardRequestToProvider(int requestID, int providerID) {
        reqServ.assignToProvider(requestID, providerID);
    }

   public Bill viewBill(int reservationID) {
    return billingService.getBillByReservationId(reservationID);
}

    public void contactCustomer(int customerID, String message) {
    }

    public void savePM(ProjectManager pm) {
    }

}
