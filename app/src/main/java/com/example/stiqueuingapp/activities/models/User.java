package com.example.stiqueuingapp.activities.models;

import com.google.firebase.firestore.PropertyName;

public class User {

    @PropertyName("CAMPUS")
    private String userCampus;
    @PropertyName("EMAIL")
    private String userEmail;


    public void setUserCampus(String userCampus) {
        this.userCampus = userCampus;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserCampus() {
        return userCampus;
    }

    public String getUserEmail() {
        return userEmail;
    }


    public User(String userEmail, String userCampus) {
        this.userEmail = userEmail;
        this.userCampus = userCampus;
    }
}
