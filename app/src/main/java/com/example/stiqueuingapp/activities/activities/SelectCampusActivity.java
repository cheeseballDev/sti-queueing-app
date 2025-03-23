package com.example.stiqueuingapp.activities.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stiqueuingapp.R;
import com.example.stiqueuingapp.activities.enums.Campuses;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectCampusActivity extends AppCompatActivity {

    private Spinner campusDropDown;

    private ArrayList<Campuses> campuses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_campus);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        initializeDropDown();


    }

    void initializeDropDown() {
        campusDropDown = findViewById(R.id.campus_spinner);

        campuses.addAll(Arrays.asList(Campuses.values()));
        ArrayAdapter<Campuses> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, campuses);
        campusDropDown.setAdapter(adapter);

    }
}