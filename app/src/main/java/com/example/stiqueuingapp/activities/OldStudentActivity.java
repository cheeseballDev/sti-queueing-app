package com.example.stiqueuingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stiqueuingapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class OldStudentActivity extends AppCompatActivity {

    private Button nextPageButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText studentNumberTextField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_old_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        studentNumberTextField = findViewById(R.id.student_number_number_box);
        nextPageButton = findViewById(R.id.button_verify_page);

        nextPageButton.setOnClickListener(view -> {
            if (studentNumberTextField.getText().toString().trim().equalsIgnoreCase("")) {
                studentNumberTextField.setError("This field cannot be blank");
            }
            /*
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            */
        });
    }
}