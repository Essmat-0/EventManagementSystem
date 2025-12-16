/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.eventmanagment.app;

import com.project.eventmanagment.models.*;
import com.project.eventmanagment.data.*;
import com.project.eventmanagment.services.*;

public class Main {

    public static void main(String[] args) {
        DataStore ds = new DataStore();
        IDGenerator idGen = new IDGenerator();
        FileHandler fh = new FileHandler();

        ReservationService resService = new ReservationService(ds, idGen);
        ServiceRequestService reqService = new ServiceRequestService(ds, idGen);
        PMService pmService = new PMService(ds, reqService);
        AdminService adminService = new AdminService(ds, reqService);

        Customer customer = new Customer(1, "Ali", "ali@mail.com", "010", "1234");
        ProjectManager pm = new ProjectManager(2, "Mona", "pm@mail.com", "011", "1234");
        ServiceProvider provider = new ServiceProvider( 1, "Ahmed","ahmed@mail.com", "015", "1234");

        ds.getCustomers().add(customer);
        ds.getPms().add(pm);
        ds.getProviders().add(provider);

        Event event = new Event("Wedding", "Big hall", 200);
        ds.getEvents().add(event);

        Reservation reservation = resService.createReservation(event, customer);
        System.out.println("Reservation created:");
        System.out.println(reservation);

        ServiceRequest request = reqService.createRequest(
                "Need sound system",
                reservation,
                customer,
                null
        );

        System.out.println("\nService request created:");
        System.out.println(request);

        adminService.forwardRequestToPM(request.getRequestID(), pm.getId());

        System.out.println("\nAdmin forwarded request to PM.");

        System.out.println("\nPM viewing assigned requests:");
        for (ServiceRequest r : pmService.getRequestsForPM(pm)) {
            System.out.println(r);
        }

        System.out.println("\nPM follows request:");
        pmService.followRequest(request.getRequestID());

        pmService.forwardRequestToProvider(request.getRequestID(), provider.getId());
        System.out.println("\nPM forwarded request to provider.");

        System.out.println("\nFinal request state:");
        System.out.println(reqService.findRequestById(request.getRequestID()));
    }
}
