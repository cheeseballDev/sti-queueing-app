package com.example.stiqueuingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stiqueuingapp.R;
import com.example.stiqueuingapp.activities.models.Student;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.FileInputStream;

public class OldStudentActivity extends AppCompatActivity {

    private Button nextPageButton;
    private EditText studentNumberTextField;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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

            db.collection("STUDENTS")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                Student student = document.toObject(Student.class);
                                Log.d("Firestore", String.valueOf(document.getData()));
                            }
                        } else {
                            Log.e("Firestore", "ERROR FETCHING", task.getException());
                        }
                    });
            /*
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            */
        });
    }
}