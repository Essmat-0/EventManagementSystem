
package com.project.eventmanagment.services;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.data.IDGenerator;
import com.project.eventmanagment.models.Bill;
import com.project.eventmanagment.models.BillItem;
import com.project.eventmanagment.models.Reservation;
import com.project.eventmanagment.models.ServiceOffer;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class BillingService {
    private DataStore dataStore;
    private IDGenerator idGen;

    public BillingService(DataStore dataStore, IDGenerator idGen) {
        this.dataStore = dataStore;
        this.idGen = idGen;
    }
    
    public Bill generateBill(Reservation reservation) {
    if (reservation == null) return null;

    Bill bill = new Bill();
    bill.setBillID(idGen.generateBillID());
    bill.setReservation(reservation);
    bill.setIssuedAt(LocalDateTime.now());

    ArrayList<BillItem> items = new ArrayList<>();
    double total = 0;

    for (ServiceOffer offer : dataStore.getOffers()) {
        if (offer.isAccepted() &&
            offer.getRequest() != null &&
            offer.getRequest().getReservation().getReservationId()
                == reservation.getReservationId()) {

            BillItem item = new BillItem(
                    offer.getRequest().getDescription(),
                    offer.getPrice()
            );

            items.add(item);
            total += offer.getPrice();
        }
    }

    bill.setItems(items);
    bill.setAmount(total);
    dataStore.getBills().add(bill);

    return bill;
}
    public Bill getBillByReservationId(int reservationID) {
    for (Bill b : dataStore.getBills()) {
        if(b.getReservation().getReservationId() == reservationID)
            return b;
    }
    return null;
}

}
