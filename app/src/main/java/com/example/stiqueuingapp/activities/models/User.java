package com.example.stiqueuingapp.activities.models;

import com.google.firebase.firestore.PropertyName;

public class User {

    @PropertyName("campus")
    private String campus;
    @PropertyName("email")
    private String email;
    @PropertyName("id")
    private String id;

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public User(String email, String campus, String id) {
        this.email = email;
        this.campus = campus;
        this.id = id;
    }
}
