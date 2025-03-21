package com.example.stiqueuingapp.activities.models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.PropertyName;

public class Student {

    @PropertyName("id")
    private long studentID;

    public Student() { }

    public Student(long studentID) {
            this.studentID = studentID;
    }

    @PropertyName("id")
    public long getStudentID() { return studentID; }

    @PropertyName("id")
    public void setStudentID(long studentID) { this.studentID = studentID; }



}
