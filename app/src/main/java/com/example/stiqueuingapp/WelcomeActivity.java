package com.example.stiqueuingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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