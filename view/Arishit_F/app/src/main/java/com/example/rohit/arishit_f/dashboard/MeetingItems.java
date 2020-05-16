package com.example.rohit.arishit_f.dashboard;

public class MeetingItems {
    String aganda,organizer,date;

    public MeetingItems(String aganda, String organizer, String date) {
        this.aganda = aganda;
        this.organizer = organizer;
        this.date = date;
    }

    public MeetingItems() {
    }

    public String getAganda() {
        return aganda;
    }

    public void setAganda(String aganda) {
        this.aganda = aganda;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
