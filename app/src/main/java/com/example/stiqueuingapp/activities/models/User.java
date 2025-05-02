package com.example.stiqueuingapp.activities.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;

public class User {

    @PropertyName("campus")
    private String campus;
    @PropertyName("email")
    private String email;
    @PropertyName("id")
    private String id;
    @PropertyName("lastQueueRequest")
    private Timestamp lastQueueRequest;
    @PropertyName ("isOnCooldown")
    private Boolean isOnCooldown;
    @PropertyName("isUserInQueue")
    private Boolean isUserInQueue;

    public void setCampus(String campus) { this.campus = campus; }

    public void setEmail(String email) { this.email = email; }

    public void setId(String id) {
        this.id = id;
    }

    public String getCampus() {
        return campus;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public User(String email, String campus, String id, Timestamp lastQueueRequest, Boolean isOnCooldown, Boolean isUserInQueue) {
        this.email = email;
        this.campus = campus;
        this.id = id;
        this.lastQueueRequest = lastQueueRequest;
        this.isOnCooldown = isOnCooldown;
        this.isUserInQueue = isUserInQueue;
    }
}
