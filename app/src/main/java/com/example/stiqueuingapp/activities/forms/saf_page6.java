package com.example.stiqueuingapp.activities.forms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stiqueuingapp.R;
import com.example.stiqueuingapp.activities.activities.HomeActivity;

public class saf_page6 extends AppCompatActivity {

    private Button previousPageSaf6, submitPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_saf_page6);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        previousPageSaf6=findViewById(R.id.back_saf6);
        submitPage = findViewById(R.id.submitForm_saf6);

        previousPageSaf6.setOnClickListener(view -> {
            startActivity(new Intent(saf_page6.this, saf_page5.class));
        });

        submitPage.setOnClickListener(view -> {
            startActivity(new Intent(saf_page6.this, HomeActivity.class));
            finish();
        });
    }
}