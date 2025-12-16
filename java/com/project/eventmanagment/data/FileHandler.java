package com.project.eventmanagment.data;

import com.project.eventmanagment.models.ProjectManager;
import com.project.eventmanagment.models.ServiceRequest;
import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    private static final String PROJECT_MANAGERS_FILE = "../../../../../resources/data/project_managers.txt";
    private static final String REQUESTS_FILE = "../../../../../resources/data/service_requests.txt";
    private static final String OFFERS_FILE = "../../../../../resources/data/service_offers.txt";
    private static final String RESERVATIONS_FILE = "../../../../../resources/data/reservations.txt";
    private static final String BILLS_FILE = "../../../../../resources/data/bills.txt";

    public void saveProjectManagers(ArrayList<ProjectManager> pms) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PROJECT_MANAGERS_FILE))) {
            for (ProjectManager pm : pms) {
                pw.println(
                        pm.getId() + "|"
                        + pm.getName() + "|"
                        + pm.getEmail() + "|"
                        + pm.getPhone()
                );
            }
        } catch (IOException e) {
        }
    }

    public ArrayList<ProjectManager> loadProjectManagers() {
        ArrayList<ProjectManager> pms = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(PROJECT_MANAGERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");

                ProjectManager pm = new ProjectManager(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        parts[3],
                        "password123"
                );
                pms.add(pm);
            }
        } catch (IOException e) {
        }
        return pms;
    }

    public void saveRequests(ArrayList<ServiceRequest> requests) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(REQUESTS_FILE))) {
            for (ServiceRequest r : requests) {
                pw.println(
                        r.getRequestID() + "|"
                        + r.getDescription() + "|"
                        + r.getReservation().getReservationId() + "|"
                        + (r.getPm() != null ? r.getPm().getId() : -1)
                );
            }
        } catch (IOException e) {
        }
    }

    public ArrayList<ServiceRequest> loadRequests(DataStore dataStore) {
        ArrayList<ServiceRequest> requests = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(REQUESTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");

                ServiceRequest r = new ServiceRequest();
                r.setRequestID(Integer.parseInt(p[0]));
                r.setDescription(p[1]);
                r.setReservation(
                        dataStore.findReservationById(Integer.parseInt(p[2]))
                );

                int pmId = Integer.parseInt(p[3]);
                if (pmId != -1) {
                    r.setPm(dataStore.findPMById(pmId));
                }

                requests.add(r);
            }
        } catch (IOException e) {
        }
        return requests;
    }
}
