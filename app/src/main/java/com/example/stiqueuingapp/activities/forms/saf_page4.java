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

public class saf_page4 extends AppCompatActivity {
    private Button nextPageSaf4;
    private Button previousPageSaf4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_saf_page4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nextPageSaf4=findViewById(R.id.next_saf4);
        nextPageSaf4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(saf_page4.this, saf_page5.class);
                startActivity(intent);
            }
        });

        previousPageSaf4=findViewById(R.id.back_saf4);
        previousPageSaf4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(saf_page4.this, saf_page3.class);
                startActivity(intent);
            }
        });
    }
}