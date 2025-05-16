package com.example.stiqueuingapp.activities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stiqueuingapp.R;
import com.example.stiqueuingapp.activities.enums.Campuses;
import com.example.stiqueuingapp.activities.enums.Languages;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {

    private Button newStudentButton, oldStudentButton;

    private Spinner languageSpinner;

    private ArrayList<String> languages = new ArrayList<>();

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
        setButtons();
        setDropDown();
    }

    protected void setDropDown() {
        languageSpinner = findViewById(R.id.language_spinner);
        for (Languages language: Languages.values()) {
            languages.add(language.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languages);
        languageSpinner.setAdapter(adapter);
    }

    protected void setButtons() {
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