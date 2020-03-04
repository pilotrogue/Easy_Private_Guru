package com.example.easyprivateguru.models;

public class Jadwal {
     private String eventId, eventTitle, eventDescription, eventLocation;

    public Jadwal(String eventId, String eventTitle, String eventDescription, String eventLocation) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventLocation() {
        return eventLocation;
    }
}
