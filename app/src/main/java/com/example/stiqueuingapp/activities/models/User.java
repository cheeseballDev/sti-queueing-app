package com.example.stiqueuingapp.activities.models;

public class User {
    private String userCampus;
    private String userEmail;


    public void getUserCampus(String userCampus) {
        this.userCampus = userCampus;
    }

    public void getUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserCampus() {
        return userCampus;
    }

    public String getUserEmail() {
        return userEmail;
    }

}
