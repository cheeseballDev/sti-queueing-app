package com.example.stiqueuingapp.activities.activities;

import static com.google.android.gms.tasks.Tasks.await;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stiqueuingapp.R;
import com.example.stiqueuingapp.activities.models.User;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkEmailActivity extends AppCompatActivity {

    private Button nextButton;

    private EditText emailTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_link_email);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setButtons();
    }

    protected void setButtons() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String campus = sharedPreferences.getString("campus", "");

        emailTextField = findViewById(R.id.edit_text_email);
        nextButton = findViewById(R.id.next_button);

        nextButton.setOnClickListener(view -> {

            if (emailTextField.getText().toString().trim().equalsIgnoreCase("")) {
                emailTextField.setError("This field cannot be blank");
                return;
            }

            if (!emailTextField.getText().toString().contains("@")) {
                 emailTextField.setError("Invalid email");
                 return;
            }

            if (!isValidEmail(emailTextField.getText().toString())) {
                emailTextField.setError("Email contains invalid characters");
                return;
            }

            updateDatabase(emailTextField.getText().toString().trim().toLowerCase(),campus);
        });
    }

    protected void updateDatabase(String email, String campus) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference emailDoc = db.collection("USERS").document(email);

        generateAndCheckUniqueId(FirebaseFirestore.getInstance(), new OnUuidGeneratedListener() {
            @Override
            public void onUuidGenerated(String uniqueId) {
                db.runTransaction(transaction -> {
                    DocumentSnapshot snapshot = transaction.get(emailDoc);

                    if (snapshot.exists())
                        transaction.update(emailDoc, "campus", campus);

                    transaction.set(emailDoc, new User(email, campus, uniqueId , Timestamp.now(), false, false));
                    return null;

                }).addOnSuccessListener(e -> {
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userEmail", email)
                            .putBoolean("isNewUser", true)
                            .apply();
                    startActivity(new Intent(LinkEmailActivity.this, HomeActivity.class));
                    finish();

                }).addOnFailureListener(e -> {
                    Toast.makeText(LinkEmailActivity.this, "Error in transaction. Please contact support", Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(LinkEmailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void generateAndCheckUniqueId(FirebaseFirestore db, OnUuidGeneratedListener listener) {
        String newUUID = UUID.randomUUID().toString();

        db.collection("USERS")
                .whereEqualTo("id", newUUID)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            listener.onUuidGenerated(newUUID);
                        } else {
                            generateAndCheckUniqueId(db, listener);
                        }
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }

    public interface OnUuidGeneratedListener {
        void onUuidGenerated(String uniqueId);
        void onError(Exception e);
    }

    protected boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}