package com.project.eventmanagment.models;

public class Notification {

    public enum NotificationType {
    EMAIL,
    SMS,
    SYSTEM
}

import java.time.LocalDateTime;

public class Notification {

    private int id;
    private String to; // receiver ID or email/phone
    private NotificationType type;
    private String message;
    private LocalDateTime sentAt;

    // Default constructor
    public Notification() {
        this.sentAt = LocalDateTime.now();
    }

    // Parameterized constructor
    public Notification(int id, String to, NotificationType type, String message) {
        this.id = id;
        this.to = to;
        this.type = type;
        this.message = message;
        this.sentAt = LocalDateTime.now();
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSentAt(LocalDateTime time) {
        this.sentAt = time;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    // Send notification
    public void send() {
        System.out.println("Sending " + type + " to " + to);
        System.out.println("Message: " + message);
        System.out.println("Sent at: " + sentAt);
    }

    // Notification details
    public String getNotification() {
        return id + " | " + to + " | " + type + " | " + message + " | " + sentAt;
    }
}
    
    @Override
    public String toString() {
        return "Notification{"
                + "id=" + id
                + ", to='" + to + '\''
                + ", type=" + type
                + ", message='" + message + '\''
                + ", sentAt=" + sentAt
                + '}';
    }
    
    */

}
