package com.example.stiqueuingapp.activities.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stiqueuingapp.R;
import com.example.stiqueuingapp.activities.models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OldStudentActivity extends AppCompatActivity {

    private Button nextPageButton;

    private EditText studentNumberTextField;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<String> studentNumbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_old_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            getStudentNumbers();
            return insets;
        });
        setButtons();
    }

    protected void setButtons() {

        studentNumberTextField = findViewById(R.id.student_number_number_box);
        nextPageButton = findViewById(R.id.button_verify_page);

        nextPageButton.setOnClickListener(view -> {

            if (studentNumberTextField.getText().toString().trim().equalsIgnoreCase("")) {
                studentNumberTextField.setError("This field cannot be blank");
                return;
            }

            if (studentNumberTextField.getText().toString().length() != 11) {
                studentNumberTextField.setError("Invalid student number");
                return;
            }

            for (int i = 0; i < studentNumbers.size(); i++) {
                if (studentNumberTextField.getText().toString().equals(studentNumbers.get(i))) {
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                    sharedPreferences.edit().putString("studentNumber", studentNumberTextField.getText().toString()).apply();
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                    return;
                }
            }

            if (!studentNumbers.contains(studentNumberTextField.getText().toString()))
                studentNumberTextField.setError("No such student number exists");
        });
    }


    protected void getStudentNumbers() {
        db.collection("STUDENTS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful())
                            Toast.makeText(OldStudentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        for (QueryDocumentSnapshot document : task.getResult())
                            studentNumbers.add(document.getId());
                    }
                });
    }
}