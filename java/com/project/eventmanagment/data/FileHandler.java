package com.project.eventmanagment.data;

import com.project.eventmanagment.models.*;
import com.project.eventmanagment.models.enums.RequestStatus;
import com.project.eventmanagment.models.enums.ReservationStatus;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileHandler {

    private static final String BASE_PATH
            = "src/main/java/com/project/data/";

    private static final String USERS_FILE = BASE_PATH + "users.txt";
    private static final String EVENTS_FILE = BASE_PATH + "events.txt";
    private static final String RESERVATIONS_FILE = BASE_PATH + "reservations.txt";
    private static final String REQUESTS_FILE = BASE_PATH + "requests.txt";
    private static final String OFFERS_FILE = BASE_PATH + "offers.txt";
    private static final String BILLS_FILE = BASE_PATH + "bills.txt";

    // =============================================================================
    // USERS
    // ============================================================================= 
    public void saveUsers(DataStore ds) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {

            for (Admin a : ds.getAdmins()) {
                pw.println("ADMIN|" + a.getId() + "|" + a.getName() + "|"
                        + a.getEmail() + "|" + a.getPhone() + "|" + a.getPassword());
            }

            for (ProjectManager pm : ds.getPms()) {
                pw.println("PM|" + pm.getId() + "|" + pm.getName() + "|"
                        + pm.getEmail() + "|" + pm.getPhone() + "|" + pm.getPassword());
            }

            for (Customer c : ds.getCustomers()) {
                pw.println("CUSTOMER|" + c.getId() + "|" + c.getName() + "|"
                        + c.getEmail() + "|" + c.getPhone() + "|" + c.getPassword());
            }

            for (ServiceProvider sp : ds.getProviders()) {
                pw.println("PROVIDER|" + sp.getId() + "|" + sp.getProviderName() + "|"
                        + sp.getEmail() + "|" + sp.getPhone() + "|" + sp.getPassword());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUsers(DataStore ds) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");

                String role = p[0];
                int id = Integer.parseInt(p[1]);
                String name = p[2];
                String email = p[3];
                String phone = p[4];
                String password = p[5];

                switch (role) {
                    case "ADMIN":
                        ds.getAdmins().add(new Admin(id, name, email, phone, password, "ADMIN"));
                        break;

                    case "PM":
                        ds.getPms().add(new ProjectManager(id, name, email, phone, password));
                        break;

                    case "CUSTOMER":
                        ds.getCustomers().add(new Customer(id, name, email, phone, password));
                        break;

                    case "PROVIDER":
                        ds.getProviders().add(new ServiceProvider(id, name, email, phone, password));
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =============================================================================
    // REQUESTS
    // =============================================================================
    public void saveRequests(DataStore ds) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(REQUESTS_FILE))) {
            for (ServiceRequest r : ds.getRequests()) {
                pw.println(
                        r.getRequestID() + "|"
                        + r.getDescription() + "|"
                        + r.getReservation().getReservationId() + "|"
                        + r.getStatus() + "|"
                        + r.getDate() + "|"
                        + (r.getPm() != null ? r.getPm().getId() : -1) + "|"
                        + (r.getAssignedTo() != null ? r.getAssignedTo().getId() : -1)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRequests(DataStore ds) {
        try (BufferedReader br = new BufferedReader(new FileReader(REQUESTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");

                ServiceRequest r = new ServiceRequest();
                r.setRequestID(Integer.parseInt(p[0]));
                r.setDescription(p[1]);
                r.setReservation(ds.findReservationById(Integer.parseInt(p[2])));
                r.changeStatus(RequestStatus.valueOf(p[3]));
                r.setCreatedAt(LocalDateTime.parse(p[4]));

                int pmId = Integer.parseInt(p[5]);
                if (pmId != -1) {
                    r.setPm(ds.findPMById(pmId));
                }

                int providerId = Integer.parseInt(p[6]);
                if (providerId != -1) {
                    r.assignToProvider(ds.findProviderByID(providerId));
                }

                ds.getRequests().add(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =============================================================================
    // EVENTS
    // =============================================================================
    public void saveEvents(DataStore ds) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(EVENTS_FILE))) {
            for (Event e : ds.getEvents()) {
                pw.println(
                        e.getEventId() + "|"
                        + e.getTitle() + "|"
                        + e.getLocation() + "|"
                        + e.getAttendeesCount()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadEvents(DataStore ds) {
        try (BufferedReader br = new BufferedReader(new FileReader(EVENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");

                Event e = new Event(
                        Integer.parseInt(p[0]),
                        p[1],
                        p[2],
                        Integer.parseInt(p[3])
                );

                ds.getEvents().add(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // =============================================================================
    // RESERVATIONS
    // =============================================================================

    public void saveReservations(DataStore ds) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RESERVATIONS_FILE))) {
            for (Reservation r : ds.getReservations()) {
                pw.println(
                        r.getReservationId() + "|"
                        + r.getReservationNumber() + "|"
                        + r.getEventDetails().getEventId() + "|"
                        + r.getCustomer().getId() + "|"
                        + r.getStatus() + "|"
                        + r.getDate()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadReservations(DataStore ds) {
        try (BufferedReader br = new BufferedReader(new FileReader(RESERVATIONS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");

                Reservation r = new Reservation(
                        ds.findEventById(Integer.parseInt(p[2])),
                        p[1],
                        Integer.parseInt(p[0]),
                        ds.findCustomerById(Integer.parseInt(p[3]))
                );

                r.updateStatus(ReservationStatus.valueOf(p[4]));
                r.setCreatedAt(LocalDateTime.parse(p[5]));

                ds.getReservations().add(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // =============================================================================
    // Bills
    // =============================================================================

    public void saveBills(DataStore ds) {
    try (PrintWriter pw = new PrintWriter(new FileWriter(BILLS_FILE))) {
        for (Bill b : ds.getBills()) {
            pw.println(
                b.getBillID() + "|"
              + b.getReservation().getReservationId() + "|"
              + b.getAmount()
            );
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

  public void loadBills(DataStore ds) {
    try (BufferedReader br = new BufferedReader(new FileReader(BILLS_FILE))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split("\\|");

            Bill b = new Bill(
                Integer.parseInt(p[0]),
                ds.findReservationById(Integer.parseInt(p[1])),
                Double.parseDouble(p[2])
            );

            ds.getBills().add(b);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    // =============================================================================
    // OFFERS
    // =============================================================================
public void saveOffers(DataStore ds) {
    try (PrintWriter pw = new PrintWriter(new FileWriter(OFFERS_FILE))) {
        for (ServiceOffer o : ds.getOffers()) {
            pw.println(
                o.getOfferID() + "|"
              + o.getPrice() + "|"
              + o.isAccepted() + "|"
              + o.getProvider().getId() + "|"
              + o.getRequest().getRequestID() + "|"
              + o.getReadyDate()
            );
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


public void loadOffers(DataStore ds) {
    try (BufferedReader br = new BufferedReader(new FileReader(OFFERS_FILE))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split("\\|");

            ServiceOffer o = new ServiceOffer(
                Boolean.parseBoolean(p[2]),
                Integer.parseInt(p[0]),
                Double.parseDouble(p[1]),
                ds.findProviderByID(Integer.parseInt(p[3]))
            );

            o.setRequest(ds.findRequestsById(Integer.parseInt(p[4])));
            o.setReadyDate(LocalDateTime.parse(p[5]));

            ds.getOffers().add(o);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}



}
