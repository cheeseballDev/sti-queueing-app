package com.example.stiqueuingapp.activities.forms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stiqueuingapp.R;
import com.example.stiqueuingapp.activities.activities.HomeActivity;

public class srf_page5 extends AppCompatActivity {

    private Button previousPageSrf4, submitPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_srf_page5);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setUI();
        setButtons();
    }

    protected void setButtons(){
        previousPageSrf4.setOnClickListener(view -> {
            startActivity(new Intent(this, srf_page4.class));
        });

        submitPage.setOnClickListener(view -> {
            showSuccessQueueForm();
            finish();
        });
    }

    protected void showSuccessQueueForm() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("shouldShowQueueSuccessPopup", true);
        startActivity(intent);
        finish();
    }

    protected void setUI() {
        previousPageSrf4=findViewById(R.id.back_srf5);
        submitPage = findViewById(R.id.submitForm_srf5);
    }
}