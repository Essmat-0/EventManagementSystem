package com.project.eventmanagment.app;

import com.project.eventmanagment.models.*;
import com.project.eventmanagment.data.DataStore;
import com.project.eventmanagment.data.FileHandler;
import com.project.eventmanagment.services.LoginService;
import javax.swing.JOptionPane;
import java.net.PasswordAuthentication;

public class LoginWindow extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoginWindow.class.getName());
    private DataStore dataStore;
    private LoginService loginService;

    public LoginWindow(DataStore dataStore) {
        initComponents();
        this.dataStore = dataStore;
        this.loginService = new LoginService(dataStore);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        SignUpBtn = new javax.swing.JButton();
        LoginBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Auth | EMS");
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(204, 0, 204));
        jTextArea1.setColumns(10);
        jTextArea1.setFont(new java.awt.Font("NSimSun", 1, 18)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(0, 204, 204));
        jTextArea1.setRows(1);
        jTextArea1.setText("\t\t\t     LOGIN/SIGN UP");
        jTextArea1.setAlignmentY(1.0F);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText("Email:");

        emailField.setInheritsPopupMenu(true);
        emailField.setName("emailField"); // NOI18N
        emailField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailFieldActionPerformed(evt);
            }
        });

        jLabel2.setText("Password:");

        SignUpBtn.setBackground(new java.awt.Color(0, 255, 255));
        SignUpBtn.setForeground(new java.awt.Color(204, 62, 147));
        SignUpBtn.setText("Sign Up");
        SignUpBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SignUpBtnActionPerformed(evt);
            }
        });

        LoginBtn.setBackground(new java.awt.Color(0, 255, 255));
        LoginBtn.setForeground(new java.awt.Color(204, 62, 147));
        LoginBtn.setText("LOG IN");
        LoginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginBtnActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("New Customer ?");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(LoginBtn)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(emailField, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                            .addComponent(jLabel2)
                            .addComponent(passwordField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 432, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(SignUpBtn)
                .addGap(35, 35, 35))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SignUpBtn)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(LoginBtn)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void emailFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailFieldActionPerformed
    }//GEN-LAST:event_emailFieldActionPerformed
    private void LoginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginBtnActionPerformed
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        Object user = loginService.authenticate(email, password);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid email or password");
            return;
        }
        if (user instanceof Customer) {
            new CustomerMenu(dataStore, (Customer) user).setVisible(true);
        } else if (user instanceof ProjectManager) {
            new PMMenu(dataStore, (ProjectManager) user).setVisible(true);
        } else if (user instanceof ServiceProvider) {
            new ProviderMenu(dataStore, (ServiceProvider) user).setVisible(true);
        } else if (user instanceof Admin) {
            new AdminMenu(dataStore, (Admin) user).setVisible(true);
        }
        this.dispose();
    }//GEN-LAST:event_LoginBtnActionPerformed

    private void SignUpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SignUpBtnActionPerformed
    new SignUpWindow(dataStore).setVisible(true);
    this.dispose(); // Close login window

    }//GEN-LAST:event_SignUpBtnActionPerformed
public static void main(String args[]) {
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
        logger.log(java.util.logging.Level.SEVERE, null, ex);
    }
    
    // Initialize DataStore and load data
    java.awt.EventQueue.invokeLater(() -> {
        DataStore dataStore = new DataStore();
        FileHandler fileHandler = new FileHandler();
        fileHandler.loadUsers(dataStore);
        fileHandler.loadEvents(dataStore);
        fileHandler.loadReservations(dataStore);
        fileHandler.loadRequests(dataStore);
        fileHandler.loadOffers(dataStore);
        fileHandler.loadBills(dataStore);
        
        new LoginWindow(dataStore).setVisible(true);
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton LoginBtn;
    private javax.swing.JButton SignUpBtn;
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPasswordField passwordField;
    // End of variables declaration//GEN-END:variables
}
