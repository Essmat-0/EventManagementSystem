package com.project.eventmanagment.services;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.data.IDGenerator;
import com.project.eventmanagment.models.*;
import com.project.eventmanagment.models.enums.NotificationType;
import com.project.eventmanagment.models.enums.RequestStatus;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PMService {

    private DataStore dataStore;
    private ServiceRequestService reqServ;
    private BillingService billingService;
    private IDGenerator idGen;
    private ProjectManager pm;

    public PMService(DataStore dataStore, ServiceRequestService reqServ) {
        this.dataStore = dataStore;
        this.reqServ = reqServ;
    }

    public PMService(DataStore dataStore, ServiceRequestService reqServ, BillingService billingService) {
        this.dataStore = dataStore;
        this.reqServ = reqServ;
        this.billingService = billingService;
    }

    public PMService(DataStore dataStore, ServiceRequestService reqServ,
            BillingService billingService, IDGenerator idGen, ProjectManager pm) {
        this.dataStore = dataStore;
        this.reqServ = reqServ;
        this.billingService = billingService;
        this.idGen = idGen;
        this.pm = pm;
    }

    // =============================================================================
    // REQUEST MANAGEMENT
    // =============================================================================
    /**
     * Gets all service requests assigned to this PM
     *
     * @param pm Project Manager
     * @return List of assigned requests
     */
    public ArrayList<ServiceRequest> getRequestsForPM(ProjectManager pm) {
        if (pm == null) {
            return new ArrayList<>();
        }
        return dataStore.getRequests().stream()
                .filter(r -> r.getPm() != null && r.getPm().getId() == pm.getId())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets requests for the currently logged-in PM
     *
     * @return List of requests
     */
    public ArrayList<ServiceRequest> getMyRequests() {
        if (pm == null) {
            System.out.println("✗ No PM logged in");
            return new ArrayList<>();
        }
        return getRequestsForPM(pm);
    }

    /**
     * Follows/tracks a service request and displays its details
     *
     * @param requestID ID of the request
     */
    public void followRequest(int requestID) {
        ServiceRequest req = reqServ.findRequestById(requestID);

        if (req == null) {
            System.out.println("✗ Request not found with ID: " + requestID);
            return;
        }

        System.out.println("\n========== REQUEST DETAILS ==========");
        System.out.println("Request ID: " + req.getRequestID());
        System.out.println("Description: " + req.getDescription());
        System.out.println("Status: " + req.getStatus());
        System.out.println("Created At: " + req.getDate());
        System.out.println("Assigned To: " + (req.getAssignedTo() != null ? req.getAssignedTo().getProviderName() : "Not assigned"));
        System.out.println("PM: " + (req.getPm() != null ? req.getPm().getName() : "Not assigned"));

        if (req.getReservation() != null) {
            System.out.println("Reservation: " + req.getReservation().getReservationNumber());
            System.out.println("Customer: " + req.getReservation().getCustomer().getName());
        }

        System.out.println("=====================================\n");
    }

    /**
     * Views detailed information about a specific request
     *
     * @param requestID ID of the request
     * @return ServiceRequest object or null
     */
    public ServiceRequest viewRequestDetails(int requestID) {
        ServiceRequest req = reqServ.findRequestById(requestID);

        if (req == null) {
            System.out.println("Request not found");
            return null;
        }

        // Check if PM has access to this request
        if (pm != null && req.getPm() != null && req.getPm().getId() != pm.getId()) {
            System.out.println("You don't have access to this request");
            return null;
        }

        return req;
    }

    /**
     * Forwards/assigns a service request to a Service Provider
     *
     * @param requestID ID of the request
     * @param providerID ID of the provider
     * @return true if assignment successful
     */
    public boolean forwardRequestToProvider(int requestID, int providerID) {
        ServiceRequest req = reqServ.findRequestById(requestID);
        ServiceProvider provider = dataStore.findProviderByID(providerID);

        if (req == null) {
            System.out.println("Request not found with ID: " + requestID);
            return false;
        }

        if (provider == null) {
            System.out.println("Service Provider not found with ID: " + providerID);
            return false;
        }

        // Check if PM owns this request
        if (pm != null && req.getPm() != null && req.getPm().getId() != pm.getId()) {
            System.out.println("You don't have permission to forward this request");
            return false;
        }

        boolean success = reqServ.assignToProvider(requestID, providerID);

        if (success) {
            System.out.println("Request forwarded to provider: " + provider.getProviderName());
        }

        return success;
    }

    /**
     * Updates the status of a service request
     *
     * @param requestID ID of the request
     * @param status New status
     * @return true if update successful
     */
    public boolean updateRequestStatus(int requestID, RequestStatus status) {
        ServiceRequest req = reqServ.findRequestById(requestID);

        if (req == null) {
            System.out.println("Request not found");
            return false;
        }

        // Check if PM owns this request
        if (pm != null && req.getPm() != null && req.getPm().getId() != pm.getId()) {
            System.out.println("You don't have permission to update this request");
            return false;
        }

        req.changeStatus(status);
        System.out.println("Request status updated to: " + status);
        return true;
    }

    /**
     * Gets all pending requests (not yet assigned to provider)
     *
     * @return List of pending requests
     */
    public ArrayList<ServiceRequest> getPendingRequests() {
        if (pm == null) {
            return new ArrayList<>();
        }

        return getRequestsForPM(pm).stream()
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // =============================================================================
    // COMMUNICATION
    // =============================================================================
    /**
     * Contacts a customer with a message
     *
     * @param customerID ID of the customer
     * @param message Message content
     * @return true if message sent successfully
     */
    public boolean contactCustomer(int customerID, String message) {

    // Validate PM login
    if (pm == null) {
        System.out.println("✗ No PM logged in");
        return false;
    }

    // Validate message
    if (message == null || message.trim().isEmpty()) {
        System.out.println("✗ Message cannot be empty");
        return false;
    }

    // Find customer
    Customer customer = dataStore.findCustomerById(customerID);
    if (customer == null) {
        System.out.println("✗ Customer not found with ID: " + customerID);
        return false;
    }

    // Validate customer email
    String email = customer.getEmail();
    if (email == null || email.trim().isEmpty()) {
        System.out.println("✗ Customer has no email on record");
        return false;
    }

    boolean sentSuccessfully = false;

    // Attempt to send notification
    if (idGen != null) {
        int notificationId = idGen.generateNotificationID();

        Notification notification = new Notification(
                notificationId,
                email,
                NotificationType.SYSTEM,
                "Message from PM " + pm.getName() + ": " + message
        );

        sentSuccessfully = notification.send();
    } else {
        // Fallback: simulate sending
        System.out.println("\n⚠️ IDGenerator unavailable — message not actually sent");
        System.out.println("📧 MESSAGE PREVIEW");
        System.out.println("From: PM " + pm.getName());
        System.out.println("To: " + customer.getName() + " (" + email + ")");
        System.out.println("Message: " + message);
        System.out.println("---");
    }

    if (!sentSuccessfully) {
        System.out.println("✗ Failed to send message to customer: " + customer.getName());
        return false;
    }

    System.out.println("✓ Message sent to customer: " + customer.getName());
    return true;
}


    // =============================================================================
    // BILLING
    // =============================================================================
    /**
     * Views bill for a specific reservation
     *
     * @param reservationID ID of the reservation
     * @return Bill object or null if not found
     */
    public Bill viewBill(int reservationID) {
        if (billingService == null) {
            System.out.println("✗ Billing service not available");
            return null;
        }

        Bill bill = billingService.getBillByReservationId(reservationID);

        if (bill == null) {
            System.out.println("✗ No bill found for reservation ID: " + reservationID);
            return null;
        }

        System.out.println("\n========== BILL DETAILS ==========");
        System.out.println("Bill ID: " + bill.getBillID());
        System.out.println("Reservation ID: " + bill.getReservation().getReservationId());
        System.out.println("Amount: $" + bill.getAmount());
        System.out.println("==================================\n");

        return bill;
    }

    /**
     * Gets all bills for requests managed by this PM
     *
     * @return List of bills
     */
    public ArrayList<Bill> getMyBills() {
        if (pm == null || billingService == null) {
            return new ArrayList<>();
        }

        ArrayList<Bill> pmBills = new ArrayList<>();

        for (ServiceRequest req : getRequestsForPM(pm)) {
            if (req.getReservation() != null) {
                Bill bill = billingService.getBillByReservationId(
                        req.getReservation().getReservationId()
                );
                if (bill != null) {
                    pmBills.add(bill);
                }
            }
        }

        return pmBills;
    }

    // =============================================================================
    // SERVICE OFFERS
    // =============================================================================
    /**
     * Views all offers for a specific request
     *
     * @param requestID ID of the request
     * @return List of offers
     */
    public ArrayList<ServiceOffer> viewOffersForRequest(int requestID) {
        ArrayList<ServiceOffer> offers = new ArrayList<>();

        for (ServiceOffer offer : dataStore.getOffers()) {
            if (offer.getRequest() != null && offer.getRequest().getRequestID() == requestID) {
                offers.add(offer);
            }
        }

        return offers;
    }

    /**
     * Reviews and approves a service offer
     *
     * @param offerID ID of the offer
     * @return true if approval successful
     */
    public boolean approveOffer(int offerID) {
        for (ServiceOffer offer : dataStore.getOffers()) {
            if (offer.getOfferID() == offerID) {
                offer.setAccepted(true);
                System.out.println("Offer approved: $" + offer.getPrice());
                return true;
            }
        }

        System.out.println("Offer not found");
        return false;
    }

}
