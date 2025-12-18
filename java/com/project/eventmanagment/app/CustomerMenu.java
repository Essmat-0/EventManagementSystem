package com.project.eventmanagment.app;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.data.IDGenerator;
import com.project.eventmanagment.data.FileHandler;

import com.project.eventmanagment.models.*;
import com.project.eventmanagment.services.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerMenu extends JFrame {
    
    private DataStore dataStore;
    private Customer customer;
    private CustomerService customerService;
    private IDGenerator idGen;
    private FileHandler fileHandler;
    private JTabbedPane tabbedPane;
    
    public CustomerMenu(DataStore dataStore, Customer customer) {
        this.dataStore = dataStore;
        this.customer = customer;
        this.idGen = new IDGenerator(dataStore);
        
        ReservationService resService = new ReservationService(dataStore, idGen);
        this.customerService = new CustomerService(dataStore, resService, idGen, customer);
        
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Customer Menu - " + customer.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Create Event", createEventPanel());
        tabbedPane.addTab("Book Event", createBookEventPanel());
        tabbedPane.addTab("My Reservations", createMyReservationsPanel());
        tabbedPane.addTab("Contact PM", createContactPMPanel());
        tabbedPane.addTab("My Requests", createMyRequestsPanel());
        
        add(tabbedPane);
    }
    
    // ============================================================================
    // CREATE EVENT PANEL
    // ============================================================================
    private JPanel createEventPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("Create New Event");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        
        JLabel titleLabel = new JLabel("Event Title:");
        JTextField titleField = new JTextField();
        
        JLabel locationLabel = new JLabel("Location:");
        JTextField locationField = new JTextField();
        
        JLabel attendeesLabel = new JLabel("Attendees Count:");
        JTextField attendeesField = new JTextField();
        
        JButton createBtn = new JButton("Create Event");
        
        formPanel.add(titleLabel);
        formPanel.add(titleField);
        formPanel.add(locationLabel);
        formPanel.add(locationField);
        formPanel.add(attendeesLabel);
        formPanel.add(attendeesField);
        formPanel.add(new JLabel(""));
        formPanel.add(createBtn);
        
        createBtn.addActionListener(e -> {
            try {
                String eventTitle = titleField.getText().trim();
                String location = locationField.getText().trim();
                int attendees = Integer.parseInt(attendeesField.getText().trim());
                
                if (eventTitle.isEmpty() || location.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields");
                    return;
                }
                
                com.project.eventmanagment.models.Event event = customerService.createEvent(eventTitle, location, attendees);
                if (event != null) {
                    JOptionPane.showMessageDialog(this, "Event created successfully!");
                    titleField.setText("");
                    locationField.setText("");
                    attendeesField.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid attendees count");
            }
        });
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        fileHandler.saveEvents(dataStore);
        return panel;
    }
    
    // ============================================================================
    // BOOK EVENT PANEL
    // ============================================================================
    private JPanel createBookEventPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("Available Events");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        String[] columns = {"Event ID", "Title", "Location", "Attendees"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JButton refreshBtn = new JButton("Refresh");
        JButton bookBtn = new JButton("Book Selected Event");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshBtn);
        buttonPanel.add(bookBtn);
        
        // Load events
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            for (com.project.eventmanagment.models.Event event : dataStore.getEvents()) {
                model.addRow(new Object[]{
                    event.getEventId(),
                    event.getTitle(),
                    event.getLocation(),
                    event.getAttendeesCount()
                });
            }
        });
        
        // Initial load
        refreshBtn.doClick();
        
        bookBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int eventId = (int) model.getValueAt(row, 0);
                com.project.eventmanagment.models.Event event = dataStore.findEventById(eventId);
                
                Reservation res = customerService.bookEvent(event);
                if (res != null) {
                    JOptionPane.showMessageDialog(this, 
                        "Event booked successfully!\nReservation: " + res.getReservationNumber());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an event");
            }
        });
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ============================================================================
    // MY RESERVATIONS PANEL
    // ============================================================================
    private JPanel createMyReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("My Reservations");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        String[] columns = {"Reservation ID", "Number", "Event", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JButton refreshBtn = new JButton("Refresh");
        JButton viewDetailsBtn = new JButton("View Details");
        JButton cancelBtn = new JButton("Cancel Reservation");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshBtn);
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(cancelBtn);
        
        // Load reservations
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            for (Reservation res : customerService.viewMyReservations()) {
                model.addRow(new Object[]{
                    res.getReservationId(),
                    res.getReservationNumber(),
                    res.getEventDetails().getTitle(),
                    res.getStatus()
                });
            }
        });
        
        // Initial load
        refreshBtn.doClick();
        
        viewDetailsBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int resId = (int) model.getValueAt(row, 0);
                Reservation res = customerService.viewReservation(resId);
                if (res != null) {
                    String details = "Reservation: " + res.getReservationNumber() + "\n"
                            + "Event: " + res.getEventDetails().getTitle() + "\n"
                            + "Location: " + res.getEventDetails().getLocation() + "\n"
                            + "Status: " + res.getStatus() + "\n"
                            + "Date: " + res.getDate();
                    JOptionPane.showMessageDialog(this, details, "Reservation Details", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a reservation");
            }
        });
        
        cancelBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int resId = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to cancel this reservation?", 
                    "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = customerService.cancelReservation(resId);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Reservation cancelled");
                        refreshBtn.doClick();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a reservation");
            }
        });
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ============================================================================
    // CONTACT PM PANEL
    // ============================================================================
    private JPanel createContactPMPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("Contact Project Manager");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("PM ID:"), gbc);
        
        gbc.gridx = 1;
        JTextField pmIdField = new JTextField(20);
        formPanel.add(pmIdField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Message:"), gbc);
        
        gbc.gridx = 1;
        JTextArea messageArea = new JTextArea(5, 20);
        JScrollPane msgScroll = new JScrollPane(messageArea);
        formPanel.add(msgScroll, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton sendBtn = new JButton("Send Message");
        formPanel.add(sendBtn, gbc);
        
        sendBtn.addActionListener(e -> {
            try {
                int pmId = Integer.parseInt(pmIdField.getText().trim());
                String message = messageArea.getText().trim();
                
                if (message.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a message");
                    return;
                }
                
                boolean success = customerService.contactPM(pmId, message);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Message sent successfully");
                    pmIdField.setText("");
                    messageArea.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid PM ID");
            }
        });
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ============================================================================
    // MY REQUESTS PANEL
    // ============================================================================
    private JPanel createMyRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("My Service Requests");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        String[] columns = {"Request ID", "Description", "Status", "Assigned To"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JButton refreshBtn = new JButton("Refresh");
        
        // Load requests
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            for (ServiceRequest req : customerService.viewMyServiceRequests()) {
                model.addRow(new Object[]{
                    req.getRequestID(),
                    req.getDescription(),
                    req.getStatus(),
                    req.getAssignedTo() != null ? req.getAssignedTo().getProviderName() : "Not assigned"
                });
            }
        });
        
        // Initial load
        refreshBtn.doClick();
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);
        
        return panel;
    }
}