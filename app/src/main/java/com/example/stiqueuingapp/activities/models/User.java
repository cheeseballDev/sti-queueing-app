package com.example.stiqueuingapp.activities.models;

import com.google.firebase.firestore.PropertyName;

public class User {

    @PropertyName("CAMPUS")
    private String CAMPUS;
    @PropertyName("EMAIL")
    private String EMAIL;


    public void setCAMPUS(String CAMPUS) {
        this.CAMPUS = CAMPUS;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getCAMPUS() {
        return CAMPUS;
    }

    public String getEMAIL() {
        return EMAIL;
    }


    public User(String EMAIL, String CAMPUS) {
        this.EMAIL = EMAIL;
        this.CAMPUS = CAMPUS;
    }
}
