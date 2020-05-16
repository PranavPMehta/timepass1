package com.example.rohit.arishit_f.dashboard;

public class CallHistoryItems {
    String username,time;
    int personIcon,callIcon;

    public int getCallIcon() {
        return callIcon;
    }

    public void setCallIcon(int callIcon) {
        this.callIcon = callIcon;
    }

    public CallHistoryItems(int personIcon, String username, String time, int callIcon) {
        this.personIcon = personIcon;
     this.username = username;
        this.time = time;
        this.callIcon = callIcon;
    }

    public int getPersonIcon() {
        return personIcon;
    }

    public void setPersonIcon(int personIcon) {
        this.personIcon = personIcon;
    }

    public CallHistoryItems() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
