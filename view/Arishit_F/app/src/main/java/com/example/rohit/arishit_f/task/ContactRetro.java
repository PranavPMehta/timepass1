package com.example.rohit.arishit_f.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactRetro {

    @SerializedName("username")
    @Expose
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
