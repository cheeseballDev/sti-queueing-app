package com.example.stiqueuingapp.activities.models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.PropertyName;

public class Student {

    private String email;

    private String name;

    public String getName() {
        return this.name;
    }

    public Student(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
