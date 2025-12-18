package com.project.eventmanagment.app;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.data.IDGenerator;
import com.project.eventmanagment.models.*;
import com.project.eventmanagment.services.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProviderMenu extends JFrame {
    
    private DataStore dataStore;
    private ServiceProvider provider;
    private ProviderService providerService;
    private IDGenerator idGen;
    
    private JTabbedPane tabbedPane;
    
    public ProviderMenu(DataStore dataStore, ServiceProvider provider) {
        this.dataStore = dataStore;
        this.provider = provider;
        this.idGen = new IDGenerator(dataStore);
        
        ServiceRequestService reqService = new ServiceRequestService(dataStore,idGen);
        ServiceOfferService offerService = new ServiceOfferService(dataStore);
        this.providerService = new ProviderService(dataStore, idGen, reqService, offerService);
        
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Service Provider Menu - " + provider.getProviderName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Assigned Requests", createAssignedRequestsPanel());
        tabbedPane.addTab("Submit Offer", createSubmitOfferPanel());
        tabbedPane.addTab("Complete Service", createCompleteServicePanel());
        tabbedPane.addTab("Create Bill", createCreateBillPanel());
        tabbedPane.addTab("View Bills", createViewBillsPanel());
        
        add(tabbedPane);
    }
    
    // ============================================================================
    // ASSIGNED REQUESTS PANEL
    // ============================================================================
    private JPanel createAssignedRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("My Assigned Requests");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        String[] columns = {"Request ID", "Description", "Status", "Customer", "Reservation"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JButton refreshBtn = new JButton("Refresh");
        
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            for (ServiceRequest req : providerService.viewAssignedRequests(provider)) {
                model.addRow(new Object[]{
                    req.getRequestID(),
                    req.getDescription(),
                    req.getStatus(),
                    req.getReservation() != null ? req.getReservation().getCustomer().getName() : "N/A",
                    req.getReservation() != null ? req.getReservation().getReservationNumber() : "N/A"
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
    
    // ============================================================================
    // SUBMIT OFFER PANEL
    // ============================================================================
    private JPanel createSubmitOfferPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("Submit Offer for Request");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        
        JLabel reqIdLabel = new JLabel("Request ID:");
        JTextField reqIdField = new JTextField();
        
        JLabel priceLabel = new JLabel("Price ($):");
        JTextField priceField = new JTextField();
        
        JLabel daysLabel = new JLabel("Days Until Ready:");
        JTextField daysField = new JTextField();
        
        JButton submitBtn = new JButton("Submit Offer");
        
        formPanel.add(reqIdLabel);
        formPanel.add(reqIdField);
        formPanel.add(priceLabel);
        formPanel.add(priceField);
        formPanel.add(daysLabel);
        formPanel.add(daysField);
        formPanel.add(new JLabel(""));
        formPanel.add(submitBtn);
        
        submitBtn.addActionListener(e -> {
            try {
                int reqId = Integer.parseInt(reqIdField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                int days = Integer.parseInt(daysField.getText().trim());
                
                LocalDateTime readyDate = LocalDateTime.now().plusDays(days);
                
                ServiceOffer offer = providerService.submitOffer(reqId, price, readyDate, provider);
                
                if (offer != null) {
                    JOptionPane.showMessageDialog(this, 
                        "Offer submitted successfully!\nOffer ID: " + offer.getOfferID());
                    reqIdField.setText("");
                    priceField.setText("");
                    daysField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to submit offer");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers");
            }
        });
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ============================================================================
    // COMPLETE SERVICE PANEL
    // ============================================================================
    private JPanel createCompleteServicePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("Complete Service Request");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        JLabel reqIdLabel = new JLabel("Request ID:");
        JTextField reqIdField = new JTextField();
        
        JButton completeBtn = new JButton("Mark as Completed");
        
        formPanel.add(reqIdLabel);
        formPanel.add(reqIdField);
        formPanel.add(new JLabel(""));
        formPanel.add(completeBtn);
        
        completeBtn.addActionListener(e -> {
            try {
                int reqId = Integer.parseInt(reqIdField.getText().trim());
                
                boolean success = providerService.completeService(reqId, provider);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Service marked as completed!");
                    reqIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to complete service");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Request ID");
            }
        });
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ============================================================================
    // CREATE BILL PANEL
    // ============================================================================
    private JPanel createCreateBillPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("Create Bill for Request");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JLabel reqIdLabel = new JLabel("Request ID:");
        JTextField reqIdField = new JTextField();
        
        JLabel amountLabel = new JLabel("Amount ($):");
        JTextField amountField = new JTextField();
        
        JButton createBtn = new JButton("Create Bill");
        
        formPanel.add(reqIdLabel);
        formPanel.add(reqIdField);
        formPanel.add(amountLabel);
        formPanel.add(amountField);
        formPanel.add(new JLabel(""));
        formPanel.add(createBtn);
        
        createBtn.addActionListener(e -> {
            try {
                int reqId = Integer.parseInt(reqIdField.getText().trim());
                double amount = Double.parseDouble(amountField.getText().trim());
                
                Bill bill = providerService.createBill(reqId, amount, provider);
                
                if (bill != null) {
                    JOptionPane.showMessageDialog(this, 
                        "Bill created successfully!\nBill ID: " + bill.getBillID() + 
                        "\nAmount: $" + bill.getAmount());
                    reqIdField.setText("");
                    amountField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create bill");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers");
            }
        });
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ============================================================================
    // VIEW BILLS PANEL
    // ============================================================================
    private JPanel createViewBillsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("View Bill by Reservation");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel resIdLabel = new JLabel("Reservation ID:");
        JTextField resIdField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        
        topPanel.add(resIdLabel);
        topPanel.add(resIdField);
        topPanel.add(searchBtn);
        
        JTextArea resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        
        searchBtn.addActionListener(e -> {
            try {
                int resId = Integer.parseInt(resIdField.getText().trim());
                
                Bill bill = providerService.viewBill(resId);
                
                if (bill != null) {
                    resultArea.setText(
                        "========== BILL DETAILS ==========\n" +
                        "Bill ID: " + bill.getBillID() + "\n" +
                        "Reservation ID: " + bill.getReservation().getReservationId() + "\n" +
                        "Reservation Number: " + bill.getReservation().getReservationNumber() + "\n" +
                        "Customer: " + bill.getReservation().getCustomer().getName() + "\n" +
                        "Amount: $" + bill.getAmount() + "\n" +
                        "=================================="
                    );
                } else {
                    resultArea.setText("No bill found for Reservation ID: " + resId);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Reservation ID");
            }
        });
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
}