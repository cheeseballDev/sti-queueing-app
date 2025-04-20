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

public class srf_page4 extends AppCompatActivity {
    private Button nextPageSrf4;
    private Button previousPageSrf4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_srf_page4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nextPageSrf4=findViewById(R.id.next_srf4);
        nextPageSrf4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(srf_page4.this, srf_page5.class);
                startActivity(intent);
            }
        });

        previousPageSrf4=findViewById(R.id.back_srf4);
        previousPageSrf4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(srf_page4.this, srf_page3.class);
                startActivity(intent);
            }
        });
    }
}