package com.example.stiqueuingapp.activities.models;

import com.google.firebase.firestore.PropertyName;

public class Student {
    @PropertyName("STUDENT_ID")
    private int studentID;

    public Student() { }

    @PropertyName("STUDENT_ID")
    public int getStudentID() { return studentID; }

    @PropertyName("STUDENT_ID")
    public void setStudentID(int studentID) { this.studentID = studentID; }

}
