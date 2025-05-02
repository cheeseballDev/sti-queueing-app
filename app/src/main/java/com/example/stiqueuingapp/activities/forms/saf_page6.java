package com.example.stiqueuingapp.activities.forms;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class saf_page6 extends AppCompatActivity {

    private Button previousPageSaf6, submitPage;

    //private String id, selectedQueueType;

    //private boolean isInQueue, isPWD;
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
        //setSharedPreferences();
        setUI();
        setButtons();
    }

    protected void setButtons(){
        previousPageSaf6.setOnClickListener(view -> {
            startActivity(new Intent(saf_page6.this, saf_page5.class));
        });

        submitPage.setOnClickListener(view -> {
            showSuccessQueueForm();
            startActivity(new Intent(saf_page6.this, HomeActivity.class));
            finish();
        });
    }

    /*
    protected void setSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("HomePreferences", MODE_PRIVATE);
        id = sharedPreferences.getString("userid", null);
        selectedQueueType = sharedPreferences.getString("selectedQueueType", "admission");
        isPWD = sharedPreferences .getBoolean("isPWD", false);
        isInQueue = sharedPreferences.getBoolean("isInQueue", false);
    }
    protected void updateQueueNumber() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference queueRef = db.collection("QUEUES").document(selectedQueueType.toUpperCase());

        db.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(queueRef);
            Long currentNumber = snapshot.getLong("currentNumber");
            long newNumber;
            if (isInQueue) {
                newNumber = (currentNumber != null) ? currentNumber - 1 : 1L;
                transaction.update(queueRef, "currentNumber", newNumber);
                isInQueue = false;
            } else {
                newNumber = (currentNumber != null) ? currentNumber + 1 : 1L;
                transaction.update(queueRef, "currentNumber", newNumber);
                createNewTicket(db, newNumber);
            }
            return newNumber;
        });
    }
    protected void createNewTicket(FirebaseFirestore db, long newNumber) {
        db.runTransaction(transaction -> {

            Map<String, Object> ticket = new HashMap<>();
            ticket.put("createdAt", FieldValue.serverTimestamp());
            ticket.put("isPWD", isPWD);
            ticket.put("number", newNumber);
            ticket.put("service", selectedQueueType);
            ticket.put("userid", id);

            DocumentReference ticketRef = db.collection("TICKETS").document();
            transaction.set(ticketRef, ticket);
            return null;
        });
    }
    */
    protected void showSuccessQueueForm() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("shouldShowQueueSuccessPopup", true);
        startActivity(intent);
        finish();
    }

    protected void setUI() {
        previousPageSaf6=findViewById(R.id.back_saf6);
        submitPage = findViewById(R.id.submitForm_saf6);
    }
}