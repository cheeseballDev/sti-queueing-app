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

public class saf_page5 extends AppCompatActivity {

    private Button nextPageSaf5;
    private Button previousPageSaf5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_saf_page5);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nextPageSaf5=findViewById(R.id.next_saf5);
        nextPageSaf5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(saf_page5.this, saf_page6.class);
                startActivity(intent);
            }
        });

        previousPageSaf5=findViewById(R.id.back_saf5);
        previousPageSaf5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(saf_page5.this, saf_page4.class);
                startActivity(intent);
            }
        });
    }
}