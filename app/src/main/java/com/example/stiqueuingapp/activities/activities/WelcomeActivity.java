package com.example.stiqueuingapp.activities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stiqueuingapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class WelcomeActivity extends AppCompatActivity {

    private Button newStudentButton, oldStudentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        newStudentButton = findViewById(R.id.new_student_button);
        oldStudentButton = findViewById(R.id.old_student_button);

        oldStudentButton.setOnClickListener(view -> {
            startActivity(new Intent(this, OldStudentActivity.class));
            finish();
        });

        newStudentButton.setOnClickListener(view -> {
            startActivity(new Intent(this, SelectCampusActivity.class));
            finish();
        });
    }
}