package com.project.eventmanagment.services;

import javax.swing.JOptionPane;

public class NotificationService {

    public static void sendEmail(String to, String subject, String body) {
        JOptionPane.showMessageDialog(
            null,
            body,
            subject,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}