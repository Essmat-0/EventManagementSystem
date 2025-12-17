package com.project.eventmanagment.models;

import java.time.LocalDateTime;

public class Event {

    private int eventId;
    private String title;
    private String notes;
    private int attendeesCount;
    private String location;
    private LocalDateTime date;
    
    
    
    public Event(){}
    public Event(int eventId,String title, String notes, int attendeesCount) {
        this.eventId = eventId;
        this.title = title;
        this.notes = notes;
        this.attendeesCount = attendeesCount;
    }

    
    
    public int getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return notes;
    }

    public int getAttendeesCount() {
        return attendeesCount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setAttendeesCount(int attendeesCount) {
        this.attendeesCount = attendeesCount;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Event{"
                + "eventID=" + eventId
                + ", title='" + title + '\''
                + ", date=" + date
                + ", location='" + location + '\''
                + ", attendeesCount=" + attendeesCount
                + '}';
    }

}
