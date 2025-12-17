package com.project.eventmanagment.services;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.models.Admin;
import com.project.eventmanagment.models.Customer;
import com.project.eventmanagment.models.ProjectManager;
import com.project.eventmanagment.models.ServiceProvider;
import com.project.eventmanagment.models.ServiceRequest;
import com.project.eventmanagment.models.User;

public class AdminService {

    private DataStore dataStore;
    private ServiceRequestService reqserv;

    public AdminService(DataStore dataStore, ServiceRequestService reqserv) {
        this.dataStore = dataStore;
        this.reqserv = reqserv;
    }

    public void addUser(User user) {
        if (user == null) {
            System.out.println("Cannot add null user");
            return;
        }
        if (user instanceof Customer) {
            dataStore.getCustomers().add((Customer) user);
        } else if (user instanceof ProjectManager) {
            dataStore.getPms().add((ProjectManager) user);
        } else if (user instanceof ServiceProvider) {
            dataStore.getProviders().add((ServiceProvider) user);
        } else if (user instanceof Admin) {
            dataStore.getAdmins().add((Admin) user);
        }
        System.out.println("User added: " + user.getName());

    }

    public boolean updateUser(int userID, String name, String email, String phone) {
        User user = findUserById(userID);
        if (user == null) {
            return false;
        }

        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        return true;
    }
    public boolean deleteUser(int userID) {
    User user = findUserById(userID);
    if (user == null) {
        return false;
    }
    
    if (user instanceof Customer) {
        return dataStore.getCustomers().remove((Customer) user);
    } else if (user instanceof ProjectManager) {
        return dataStore.getPms().remove((ProjectManager) user);
    } else if (user instanceof ServiceProvider) {
        return dataStore.getProviders().remove((ServiceProvider) user);
    } else if (user instanceof Admin) {
        return dataStore.getAdmins().remove((Admin) user);
    }
    return false;
}

    public boolean forwardRequestToPM(int requestID, int pmID) {
        ServiceRequest req = reqserv.findRequestById(requestID);
        ProjectManager pm = findPMById(pmID);

        if (req == null || pm == null) {
            return false;
        }

        req.setPm(pm);
        pm.addRequest(req);
        return true;
    }

    public ServiceRequest receiveRequest(int requestID) {
        return reqserv.findRequestById(requestID);
    }

    private User findUserById(int id) {
        for (Customer c : dataStore.getCustomers()) {
            if (c.getId() == id) {
                return c;
            }
        }

        for (ProjectManager pm : dataStore.getPms()) {
            if (pm.getId() == id) {
                return pm;
            }
        }

        for (ServiceProvider sp : dataStore.getProviders()) {
            if (sp.getId() == id) {
                return sp;
            }
        }

        for (Admin a : dataStore.getAdmins()) {
            if (a.getId() == id) {
                return a;
            }
        }

        return null;
    }

    private ProjectManager findPMById(int id) {
        for (ProjectManager pm : dataStore.getPms()) {
            if (pm.getId() == id) {
                return pm;
            }
        }
        return null;
    }
}
