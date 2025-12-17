package com.project.eventmanagment.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class ServiceProvider extends User {

    private final ArrayList<ServiceOffer> offers = new ArrayList<>();
    private String providerName;

    public String getProviderName() {
        return providerName;
    }

    public ServiceProvider(int id, String providerName, String email, String phone, String password) {
        super(id, providerName, email, phone, password);
        this.providerName = providerName;
    }

    public void addOffer(ServiceOffer offer) {
        if (offer != null && !offers.contains(offer)) {
            offers.add(offer);
        }
    }

    public void removeOffer(ServiceOffer offer) {
        offers.remove(offer);
    }

    private final ArrayList<ServiceRequest> assignedRequests = new ArrayList<>();

    public void addAssignedRequest(ServiceRequest req) {
        if (req != null && !assignedRequests.contains(req)) {
            assignedRequests.add(req);
        }
    }

    public ArrayList<ServiceRequest> viewAssignedRequests() {
        return assignedRequests;
    }

    @Override
    public boolean login(String email, String password) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public void logout() {
    }

    @Override
    public void updateProfile(Map<String, Object> data) {
    }
}
