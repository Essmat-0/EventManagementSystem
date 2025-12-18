package com.project.eventmanagment.app;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.data.FileHandler;
import com.project.eventmanagment.data.IDGenerator;
import com.project.eventmanagment.models.*;
import com.project.eventmanagment.models.enums.ReservationStatus;
import com.project.eventmanagment.services.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  EVENT MANAGEMENT SYSTEM - TEST");
        System.out.println("========================================\n");
        
        // Initialize core components
        DataStore dataStore = new DataStore();
        FileHandler fileHandler = new FileHandler();
        IDGenerator idGen = new IDGenerator(dataStore);
        
        // Create test data
        setupTestData(dataStore);
        
        // Save initial data
        fileHandler.saveUsers(dataStore);
        fileHandler.saveEvents(dataStore);
        
        System.out.println("✓ Test data created and saved\n");
        
        // TEST 1: Login System
        testLogin(dataStore);
        
        // TEST 2: Customer Flow
        testCustomerFlow(dataStore, idGen);
        
        // TEST 3: Admin Flow
        testAdminFlow(dataStore, fileHandler);
        
        // TEST 4: PM Flow
        testPMFlow(dataStore, idGen);
        
        // TEST 5: Provider Flow
        testProviderFlow(dataStore, idGen);
        
        System.out.println("\n========================================");
        System.out.println("  ALL TESTS COMPLETED!");
        System.out.println("========================================");
    }
    
    private static void setupTestData(DataStore dataStore) {
        // Create Admin
        Admin admin = new Admin(1, "Admin User", "admin@system.com", "111", "admin123", "SUPER_ADMIN");
        dataStore.getAdmins().add(admin);
        
        // Create PM
        ProjectManager pm = new ProjectManager(2, "John PM", "pm@system.com", "222", "pm123");
        dataStore.getPms().add(pm);
        
        // Create Customer
        Customer customer = new Customer(3, "Alice Customer", "alice@email.com", "333", "cust123");
        dataStore.getCustomers().add(customer);
        
        // Create Provider
        ServiceProvider provider = new ServiceProvider(4, "Best Catering Co", "catering@email.com", "444", "prov123");
        dataStore.getProviders().add(provider);
        
        
    }
    
    private static void testLogin(DataStore dataStore) {
        System.out.println("========================================");
        System.out.println("TEST 1: LOGIN SYSTEM");
        System.out.println("========================================");
        
        LoginService login = new LoginService(dataStore);
        
        // Test valid login
        Object user = login.authenticate("alice@email.com", "cust123");
        if (user != null && user instanceof Customer) {
            System.out.println("✓ Customer login successful: " + ((Customer)user).getName());
        } else {
            System.out.println("✗ Customer login FAILED");
        }
        
        // Test invalid login
        user = login.authenticate("wrong@email.com", "wrong");
        if (user == null) {
            System.out.println("✓ Invalid login correctly rejected");
        } else {
            System.out.println("✗ Invalid login incorrectly accepted");
        }
        
        // Test role detection
        user = login.authenticate("pm@system.com", "pm123");
        String role = login.getUserRole(user);
        if ("PROJECT_MANAGER".equals(role)) {
            System.out.println("✓ PM role detected correctly");
        } else {
            System.out.println("✗ PM role detection FAILED");
        }
        
        System.out.println();
    }
    
    private static void testCustomerFlow(DataStore dataStore, IDGenerator idGen) {
    System.out.println("========================================");
    System.out.println("TEST X: CUSTOMER CREATE EVENT + SEND TO PM");
    System.out.println("========================================");

    // Assume at least one customer & one PM already exist
    Customer customer = dataStore.getCustomers().get(0);
    ProjectManager pm = dataStore.getPms().get(0);

    ReservationService resService = new ReservationService(dataStore, idGen);
    CustomerService custService = new CustomerService(dataStore, resService, idGen, customer);
    ServiceRequestService reqService = new ServiceRequestService(dataStore, idGen);

    // Test 1: Customer creates event
    Event event = custService.createEvent(
            "Birthday Party",
            "City Hall",
            80
    );

    if (event != null) {
        System.out.println("✓ Event created: " + event.getTitle());
    } else {
        System.out.println("✗ Event creation FAILED");
        return;
    }

    // Test 2: Customer books the event
    Reservation reservation = custService.bookEvent(event);
    if (reservation != null) {
        System.out.println("✓ Reservation created: " + reservation.getReservationNumber());
    } else {
        System.out.println("✗ Reservation FAILED");
        return;
    }

    // Test 3: Customer creates service request
    ServiceRequest request = reqService.createRequest(
            "BLAH BLAH BLAH",
            dataStore.findReservationById(reservation.getReservationId()),
            dataStore.findCustomerById(customer.getId()),
            dataStore.findPMById(pm.getId())
    );

    if (request != null) {
        System.out.println("✓ Service request created");
    } else {
        System.out.println("✗ Service request FAILED");
        return;
    }

    // Test 4: Admin / system assigns request to PM
    request.setPm(pm);

    System.out.println("✓ Request assigned to PM: " + pm.getName());

    // Test 5: PM sees the request
    PMService pmService = new PMService(dataStore, reqService);
    ArrayList<ServiceRequest> pmRequests = pmService.getRequestsForPM(pm);

    if (!pmRequests.isEmpty()) {
        System.out.println("✓ PM received request");
    } else {
        System.out.println("✗ PM did not receive request");
    }

    System.out.println();
}

    
    private static void testAdminFlow(DataStore dataStore, FileHandler fileHandler) {
        System.out.println("========================================");
        System.out.println("TEST 3: ADMIN FLOW");
        System.out.println("========================================");
        IDGenerator idGen = new IDGenerator(dataStore);
        ServiceRequestService reqService = new ServiceRequestService(dataStore,idGen);
        AdminService adminService = new AdminService(dataStore, reqService);
        
        // Test 1: Add User
        Customer newCustomer = new Customer(10, "Bob New", "bob@email.com", "555", "bob123");
        adminService.addUser(newCustomer);
        if (dataStore.getCustomers().contains(newCustomer)) {
            System.out.println("✓ Add customer works");
        } else {
            System.out.println("✗ Add customer FAILED");
        }
        
        // Test 2: Update User
        boolean updated = adminService.updateUser(newCustomer.getId(), "Bob Updated", "bob@email.com", "555");
        if (updated && newCustomer.getName().equals("Bob Updated")) {
            System.out.println("✓ Update customer works");
        } else {
            System.out.println("✗ Update customer FAILED");
        }
        
        // Test 3: Delete User
        int deleteId = newCustomer.getId();
        boolean deleted = adminService.deleteUser(deleteId);
        if (deleted && dataStore.findCustomerById(deleteId) == null) {
            System.out.println("✓ Delete customer works");
        } else {
            System.out.println("✗ Delete customer FAILED");
        }
        
        // Test 4: Create and Assign Request to PM
        Customer customer = dataStore.getCustomers().get(0);
        Reservation res = customer.getReservation().get(0);
        
        ServiceRequest request = new ServiceRequest();
        request.setRequestID(1);
        request.setDescription("Need catering service");
        request.setReservation(res);
        dataStore.getRequests().add(request);
        
        ProjectManager pm = dataStore.getPms().get(0);
        boolean assigned = adminService.forwardRequestToPM(request.getRequestID(), pm.getId());
        if (assigned && request.getPm() != null) {
            System.out.println("✓ Assign request to PM works");
        } else {
            System.out.println("✗ Assign request to PM FAILED");
        }
        
        System.out.println();
    }
    
    private static void testPMFlow(DataStore dataStore, IDGenerator idGen) {
        System.out.println("========================================");
        System.out.println("TEST 4: PM FLOW");
        System.out.println("========================================");
        
        ProjectManager pm = dataStore.getPms().get(0);
        ServiceRequestService reqService = new ServiceRequestService(dataStore,idGen);
        BillingService billService = new BillingService(dataStore, idGen);
        PMService pmService = new PMService(dataStore, reqService, billService, idGen, pm);
        
        // Test 1: View My Requests
        if (pmService.getMyRequests().size() > 0) {
            System.out.println("✓ View PM requests works");
        } else {
            System.out.println("✗ View PM requests FAILED");
        }
        
        // Test 2: Forward Request to Provider
        ServiceRequest request = dataStore.getRequests().get(0);
        ServiceProvider provider = dataStore.getProviders().get(0);
        boolean forwarded = pmService.forwardRequestToProvider(request.getRequestID(), provider.getId());
        if (forwarded && request.getAssignedTo() != null) {
            System.out.println("✓ Forward request to provider works");
        } else {
            System.out.println("✗ Forward request to provider FAILED");
        }
        
        // Test 3: Contact Customer
        Customer customer = dataStore.getCustomers().get(0);
        boolean contacted = pmService.contactCustomer(customer.getId(), "Your request is being processed");
        if (contacted) {
            System.out.println("✓ Contact customer works");
        } else {
            System.out.println("✗ Contact customer FAILED");
        }
        
        // Test 4: View Request Details
        pmService.followRequest(request.getRequestID());
        System.out.println("✓ Follow request displays details");
        
        System.out.println();
    }
    
       private static void testProviderFlow(DataStore dataStore, IDGenerator idGen) {
        System.out.println("========================================");
        System.out.println("TEST 5: PROVIDER FLOW");
        System.out.println("========================================");
        
        ServiceProvider provider = dataStore.getProviders().get(0);
        ServiceRequestService reqService = new ServiceRequestService(dataStore,idGen);
        ServiceOfferService offerService = new ServiceOfferService(dataStore);
        
        // Create ProviderService with all required dependencies
        ProviderService provService = new ProviderService(dataStore, idGen, reqService, offerService);
        
        // Test 1: View Assigned Requests
        if (provService.viewAssignedRequests(provider).size() > 0) {
            System.out.println("✓ View assigned requests works");
        } else {
            System.out.println("✗ View assigned requests FAILED");
        }
        
        // Test 2: Submit Offer
        ServiceRequest request = dataStore.getRequests().get(0);
        ServiceOffer offer = provService.submitOffer(
            request.getRequestID(), 
            5000.0, 
            LocalDateTime.now().plusDays(7), 
            provider
        );
        if (offer != null && dataStore.getOffers().contains(offer)) {
            System.out.println("✓ Submit offer works: $" + offer.getPrice());
        } else {
            System.out.println("✗ Submit offer FAILED");
        }
        
        // Test 3: Update Offer Status (Accept)
        boolean accepted = provService.updateOfferStatus(offer.getOfferID(), true);
        if (accepted && offer.isAccepted()) {
            System.out.println("✓ Accept offer works");
        } else {
            System.out.println("✗ Accept offer FAILED");
        }
        
        System.out.println();
    }
}
