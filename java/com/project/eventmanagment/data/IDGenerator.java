package com.project.eventmanagment.data;

public class IDGenerator {

    private DataStore dataStore;

    public IDGenerator(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public int generateReservationID() {
        int maxId = 0;
        for (var r : dataStore.getReservations()) {
            if (r.getReservationId() > maxId) {
                maxId = r.getReservationId();
            }
        }
        return maxId + 1;
    }

    public int generateOfferID() {
        int maxId = 0;
        for (var o : dataStore.getOffers()) {
            if (o.getOfferID() > maxId) {
                maxId = o.getOfferID();
            }
        }
        return maxId + 1;
    }

    public int generateBillID() {
        int maxId = 0;
        for (var b : dataStore.getBills()) {
            if (b.getBillID() > maxId) {
                maxId = b.getBillID();
            }
        }
        return maxId + 1;
    }

    public int generateNotificationID() {
        return (int) (System.currentTimeMillis() % 100000);
    }

    public int generateRequestID() {
        int maxId = 0;
        for (var r : dataStore.getRequests()) {
            if (r.getRequestID() > maxId) {
                maxId = r.getRequestID();
            }
        }
        return maxId + 1;
    }
    public int generateEventID() {
        int maxId = 0;
        for (var r : dataStore.getEvents()) {
            if (r.getEventId()> maxId) {
                maxId = r.getEventId();
            }
        }
        return ++maxId;
    }


}
