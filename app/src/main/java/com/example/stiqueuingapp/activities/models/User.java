package com.example.stiqueuingapp.activities.models;

import com.google.firebase.firestore.PropertyName;

public class User {

    @PropertyName("campus")
    private String campus;
    @PropertyName("email")
    private String email;


    public void setCampus(String campus) {
        this.campus = campus;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCampus() {
        return campus;
    }

    public String getEMAIL() {
        return email;
    }


    public User(String email, String campus) {
        this.email = email;
        this.campus = campus;
    }
}
