package com.project.eventmanagment.app;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.data.FileHandler;
import com.project.eventmanagment.models.*;
import java.util.Scanner;


public class Login {
    
    private DataStore dataStore;
    private Scanner scanner;
    private Object currentUser;
    
    public Login(DataStore dataStore) {
        this.dataStore = dataStore;
        this.scanner = new Scanner(System.in);
        this.currentUser = null;
    }
    
    /**
     * Performs console-based login authentication
     * @return The logged-in user object (Admin, ProjectManager, Customer, or ServiceProvider)
     *         Returns null if login fails
     */
    public Object performLogin() {
        System.out.println("=================================");
        System.out.println("    EVENT MANAGEMENT SYSTEM");
        System.out.println("=================================");
        System.out.println();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();
        
        currentUser = authenticate(email, password);
        
        if (currentUser != null) {
            displayWelcomeMessage(currentUser);
        } else {
            System.out.println("\n✗ Login failed! Invalid email or password.");
        }
        
        return currentUser;
    }
    
    /**
     * Authenticates user credentials without console interaction
     * @param email User's email
     * @param password User's password
     * @return The authenticated user object or null if authentication fails
     */
    public Object authenticate(String email, String password) {
        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }
        
        // Check Admin
        for (Admin admin : dataStore.getAdmins()) {
            if (admin.getEmail().equalsIgnoreCase(email) && 
                admin.getPassword().equals(password)) {
                return admin;
            }
        }
        
        // Check Project Manager
        for (ProjectManager pm : dataStore.getPms()) {
            if (pm.getEmail().equalsIgnoreCase(email) && 
                pm.getPassword().equals(password)) {
                return pm;
            }
        }
        
        // Check Customer
        for (Customer customer : dataStore.getCustomers()) {
            if (customer.getEmail().equalsIgnoreCase(email) && 
                customer.getPassword().equals(password)) {
                return customer;
            }
        }
        
        // Check Service Provider
        for (ServiceProvider provider : dataStore.getProviders()) {
            if (provider.getEmail().equalsIgnoreCase(email) && 
                provider.getPassword().equals(password)) {
                return provider;
            }
        }
        
        return null;
    }
    
    /**
     * Displays welcome message based on user type
     * @param user The authenticated user
     */
    private void displayWelcomeMessage(Object user) {
        System.out.println();
        if (user instanceof Admin) {
            Admin admin = (Admin) user;
            System.out.println("✓ Login successful! Welcome, " + admin.getName() + " (Admin)");
        } else if (user instanceof ProjectManager) {
            ProjectManager pm = (ProjectManager) user;
            System.out.println("✓ Login successful! Welcome, " + pm.getName() + " (Project Manager)");
        } else if (user instanceof Customer) {
            Customer customer = (Customer) user;
            System.out.println("✓ Login successful! Welcome, " + customer.getName() + " (Customer)");
        } else if (user instanceof ServiceProvider) {
            ServiceProvider provider = (ServiceProvider) user;
            System.out.println("✓ Login successful! Welcome, " + provider.getProviderName() + " (Service Provider)");
        }
    }
    
    /**
     * Gets the role/type of the logged-in user
     * @param user The user object
     * @return String representation of the user role
     */
    public String getUserRole(Object user) {
        if (user instanceof Admin) {
            return "ADMIN";
        } else if (user instanceof ProjectManager) {
            return "PROJECT_MANAGER";
        } else if (user instanceof Customer) {
            return "CUSTOMER";
        } else if (user instanceof ServiceProvider) {
            return "SERVICE_PROVIDER";
        }
        return "UNKNOWN";
    }
    
    /**
     * Gets the ID of the logged-in user
     * @param user The user object
     * @return User ID or -1 if user is invalid
     */
    public int getUserId(Object user) {
        if (user instanceof Admin) {
            return ((Admin) user).getId();
        } else if (user instanceof ProjectManager) {
            return ((ProjectManager) user).getId();
        } else if (user instanceof Customer) {
            return ((Customer) user).getId();
        } else if (user instanceof ServiceProvider) {
            return ((ServiceProvider) user).getId();
        }
        return -1;
    }
    
    /**
     * Gets the name of the logged-in user
     * @param user The user object
     * @return User name or empty string if user is invalid
     */
    public String getUserName(Object user) {
        if (user instanceof ServiceProvider) {
            return ((ServiceProvider) user).getProviderName();
        } else if (user instanceof User) {
            return ((User) user).getName();
        }
        return "";
    }
    
    /**
     * Gets the current logged-in user
     * @return Current user object or null if no user is logged in
     */
    public Object getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Logs out the current user
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("User " + getUserName(currentUser) + " logged out successfully.");
            currentUser = null;
        }
    }
    
    /**
     * Checks if a user is currently logged in
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Example usage in main method
     */
    public static void main(String[] args) {
        // Initialize data store
        DataStore dataStore = new DataStore();
        
        // Load users from file
        FileHandler fileHandler = new FileHandler();
        fileHandler.loadUsers(dataStore);
        
        // Create login instance
        Login login = new Login(dataStore);
        
        // Perform login
        Object loggedInUser = login.performLogin();
        
        if (loggedInUser != null) {
            String role = login.getUserRole(loggedInUser);
            int userId = login.getUserId(loggedInUser);
            String userName = login.getUserName(loggedInUser);
            
            System.out.println("\nUser Details:");
            System.out.println("ID: " + userId);
            System.out.println("Name: " + userName);
            System.out.println("Role: " + role);
            
            // Route to appropriate dashboard based on role
            switch (role) {
                case "ADMIN":
                    System.out.println("\nRedirecting to Admin Dashboard...");
                    // Launch admin dashboard
                    break;
                case "PROJECT_MANAGER":
                    System.out.println("\nRedirecting to Project Manager Dashboard...");
                    // Launch PM dashboard
                    break;
                case "CUSTOMER":
                    System.out.println("\nRedirecting to Customer Dashboard...");
                    // Launch customer dashboard
                    break;
                case "SERVICE_PROVIDER":
                    System.out.println("\nRedirecting to Service Provider Dashboard...");
                    // Launch provider dashboard
                    break;
            }
            
            // Logout example
            login.logout();
        } else {
            System.out.println("\nPlease try again or contact support.");
        }
    }
}