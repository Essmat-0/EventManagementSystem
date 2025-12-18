package com.project.eventmanagment.app;

import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.data.IDGenerator;
import com.project.eventmanagment.models.*;
import com.project.eventmanagment.models.enums.RequestStatus;
import com.project.eventmanagment.services.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PMMenu extends JFrame {
    
    private DataStore dataStore;
    private ProjectManager pm;
    private PMService pmService;
    private IDGenerator idGen;
    
    private JTabbedPane tabbedPane;
    
    public PMMenu(DataStore dataStore, ProjectManager pm) {
        this.dataStore = dataStore;
        this.pm = pm;
        this.idGen = new IDGenerator(dataStore);
        
        ServiceRequestService reqService = new ServiceRequestService(dataStore,idGen);
        BillingService billService = new BillingService(dataStore, idGen);
        this.pmService = new PMService(dataStore, reqService, billService, idGen, pm);
        
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Project Manager Menu - " + pm.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("My Requests", createMyRequestsPanel());
        tabbedPane.addTab("Forward Request", createForwardRequestPanel());
        tabbedPane.addTab("Contact Customer", createContactCustomerPanel());
        tabbedPane.addTab("View Offers", createViewOffersPanel());
        tabbedPane.addTab("View Bills", createViewBillsPanel());
        
        add(tabbedPane);
    }
    
    // ============================================================================
    // MY REQUESTS PANEL
    // ============================================================================
    private JPanel createMyRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("My Assigned Requests");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        String[] columns = {"ID", "Description", "Status", "Customer", "Provider"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JButton refreshBtn = new JButton("Refresh");
        JButton viewDetailsBtn = new JButton("View Details");
        JButton updateStatusBtn = new JButton("Update Status");
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(refreshBtn);
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(updateStatusBtn);
        
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            for (ServiceRequest req : pmService.getMyRequests()) {
                model.addRow(new Object[]{
                    req.getRequestID(),
                    req.getDescription(),
                    req.getStatus(),
                    req.getReservation() != null ? req.getReservation().getCustomer().getName() : "N/A",
                    req.getAssignedTo() != null ? req.getAssignedTo().getProviderName() : "Not assigned"
                });
            }
        });
        
        viewDetailsBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int reqId = (int) model.getValueAt(row, 0);
                ServiceRequest req = pmService.viewRequestDetails(reqId);
                if (req != null) {
                    String details = "Request ID: " + req.getRequestID() + "\n"
                            + "Description: " + req.getDescription() + "\n"
                            + "Status: " + req.getStatus() + "\n"
                            + "Created: " + req.getDate() + "\n"
                            + "Provider: " + (req.getAssignedTo() != null ? req.getAssignedTo().getProviderName() : "Not assigned");
                    JOptionPane.showMessageDialog(this, details, "Request Details", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a request");
            }
        });
        
        updateStatusBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int reqId = (int) model.getValueAt(row, 0);
                String[] statuses = {"PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED"};
                String status = (String) JOptionPane.showInputDialog(this, "Select new status:", 
                        "Update Status", JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);
                if (status != null) {
                    pmService.updateRequestStatus(reqId, RequestStatus.valueOf(status));
                    refreshBtn.doClick();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a request");
            }
        });
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ============================================================================
    // FORWARD REQUEST PANEL
    // ============================================================================
    private JPanel createForwardRequestPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("Forward Request to Provider");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JLabel reqIdLabel = new JLabel("Request ID:");
        JTextField reqIdField = new JTextField();
        
        JLabel providerLabel = new JLabel("Provider:");
        JComboBox<String> providerCombo = new JComboBox<>();
        
        JButton loadProvidersBtn = new JButton("Load Providers");
        JButton forwardBtn = new JButton("Forward Request");
        
        formPanel.add(reqIdLabel);
        formPanel.add(reqIdField);
        formPanel.add(providerLabel);
        formPanel.add(providerCombo);
        formPanel.add(loadProvidersBtn);
        formPanel.add(forwardBtn);
        
        loadProvidersBtn.addActionListener(e -> {
            providerCombo.removeAllItems();
            for (ServiceProvider provider : dataStore.getProviders()) {
                providerCombo.addItem(provider.getId() + " - " + provider.getProviderName());
            }
        });
        
        forwardBtn.addActionListener(e -> {
            try {
                int reqId = Integer.parseInt(reqIdField.getText().trim());
                String selected = (String) providerCombo.getSelectedItem();
                if (selected != null) {
                    int providerId = Integer.parseInt(selected.split(" - ")[0]);
                    boolean success = pmService.forwardRequestToProvider(reqId, providerId);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Request forwarded successfully");
                        reqIdField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to forward request");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Request ID");
            }
        });
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ============================================================================
    // CONTACT CUSTOMER PANEL
    // ============================================================================
    private JPanel createContactCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("Contact Customer");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Customer ID:"), gbc);
        
        gbc.gridx = 1;
        JTextField customerIdField = new JTextField(20);
        formPanel.add(customerIdField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Message:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextArea messageArea = new JTextArea(5, 20);
        JScrollPane msgScroll = new JScrollPane(messageArea);
        formPanel.add(msgScroll, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton sendBtn = new JButton("Send Message");
        formPanel.add(sendBtn, gbc);
        
        sendBtn.addActionListener(e -> {
            try {
                int customerId = Integer.parseInt(customerIdField.getText().trim());
                String message = messageArea.getText().trim();
                
                if (message.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a message");
                    return;
                }
                
                boolean success = pmService.contactCustomer(customerId, message);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Message sent successfully");
                    customerIdField.setText("");
                    messageArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to send message");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Customer ID");
            }
        });
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ============================================================================
    // VIEW OFFERS PANEL
    // ============================================================================
    private JPanel createViewOffersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("View Offers for Request");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel reqLabel = new JLabel("Request ID:");
        JTextField reqIdField = new JTextField(10);
        JButton loadBtn = new JButton("Load Offers");
        
        topPanel.add(reqLabel);
        topPanel.add(reqIdField);
        topPanel.add(loadBtn);
        
        String[] columns = {"Offer ID", "Provider", "Price", "Ready Date", "Accepted"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JButton approveBtn = new JButton("Approve Offer");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(approveBtn);
        
        loadBtn.addActionListener(e -> {
            try {
                int reqId = Integer.parseInt(reqIdField.getText().trim());
                model.setRowCount(0);
                for (ServiceOffer offer : pmService.viewOffersForRequest(reqId)) {
                    model.addRow(new Object[]{
                        offer.getOfferID(),
                        offer.getProvider().getProviderName(),
                        "$" + offer.getPrice(),
                        offer.getReadyDate(),
                        offer.isAccepted() ? "Yes" : "No"
                    });
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Request ID");
            }
        });
        
        approveBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int offerId = (int) model.getValueAt(row, 0);
                boolean success = pmService.approveOffer(offerId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Offer approved");
                    loadBtn.doClick();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an offer");
            }
        });
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ============================================================================
    // VIEW BILLS PANEL
    // ============================================================================
    private JPanel createViewBillsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("View Bills");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        String[] columns = {"Bill ID", "Reservation ID", "Amount"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JButton refreshBtn = new JButton("Refresh");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshBtn);
        
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            for (Bill bill : pmService.getMyBills()) {
                model.addRow(new Object[]{
                    bill.getBillID(),
                    bill.getReservation().getReservationId(),
                    "$" + bill.getAmount()
                });
            }
        });
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
}