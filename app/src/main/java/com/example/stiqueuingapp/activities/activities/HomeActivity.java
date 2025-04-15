package com.example.stiqueuingapp.activities.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stiqueuingapp.R;
import com.example.stiqueuingapp.activities.enums.Campuses;
import com.example.stiqueuingapp.activities.enums.Forms;
import com.example.stiqueuingapp.activities.enums.QueueType;
import com.example.stiqueuingapp.activities.forms.saf_page1;
import com.example.stiqueuingapp.activities.forms.srf_page1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private View admission, registrar, cashier, admissionDivider, registrarDivider, cashierDivider;

    private Button
            enterQueueButton,
            PWDConfirmButton, PWDDeclineButton,
            selectQueueNextButton,
            selectFormNextButton;

    private ImageButton
            PWDCloseButton,
            selectQueueCloseButton,
            selectFormCloseButton;

    private TextView
            userNumber, userCooldown,
            admissionCurrentQueueNumber, registrarCurrentQueueNumber, cashierCurrentQueueNumber,
            admissionCurrentCutOff, registrarCurrentCutOff, cashierCurrentCutOff,
            admissionCurrentCounter, registrarCurrentCounter, cashierCurrentCounter;

    private Dialog dialogPWD, dialogSelectQueue, dialogSelectForm;

    private Spinner spinnerSelectQueue, spinnerSelectForm;

    private boolean isPWD = false, isNewUser = false;

    private String id;

    private String selectedQueueType;

    private ArrayList<QueueType> queueTypes = new ArrayList<>();
    private ArrayList<String> forms = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setDialogsAndButtons();
        setCategories();
        setSpinner();
        startQueueButton();
    }

    // DATABASE QUEUE
    protected void updateQueue() {
        userNumber = findViewById(R.id.user_number);
        userCooldown = findViewById(R.id.user_cooldown);

        admissionCurrentCounter = admission.findViewById(R.id.queue_current_counter);
        admissionCurrentQueueNumber = admission.findViewById(R.id.queue_current_number);
        admissionCurrentCutOff = admission.findViewById(R.id.queue_current_cut_off);

        registrarCurrentCounter = registrar.findViewById(R.id.queue_current_counter);
        registrarCurrentCutOff = registrar.findViewById(R.id.queue_current_cut_off);
        registrarCurrentQueueNumber = registrar.findViewById(R.id.queue_current_counter);

        cashierCurrentCounter = cashier.findViewById(R.id.queue_current_counter);
        cashierCurrentCutOff = cashier.findViewById(R.id.queue_current_cut_off);
        cashierCurrentQueueNumber = cashier.findViewById(R.id.queue_current_counter);

        // PUT THE SHIT ABOVE IN THE METHOD BELOW THAT SETS THE THING

            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference queueRef = db.collection("QUEUES").document(selectedQueueType.toUpperCase());

            db.runTransaction(transaction -> {
                DocumentSnapshot snapshot = transaction.get(queueRef);
                Long currentNumber = snapshot.getLong("currentNumber");
                long newNumber = (currentNumber != null) ? currentNumber + 1 : 1L;
                transaction.update(queueRef, "currentNumber", newNumber);
                createNewTicket(db, newNumber);
                return newNumber;
            }).addOnFailureListener(e -> {
                Log.w("TICKET", e.getMessage(), e);
            });
    }

    protected void createNewTicket(FirebaseFirestore db, long newNumber) {
        db.runTransaction(transaction -> {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
            isNewUser = sharedPreferences.getBoolean("isNewUser", false);
            String email = sharedPreferences.getString("userEmail", "");

            if (isNewUser) {
                DocumentReference userRef = db.collection("USERS").document(email);
                DocumentSnapshot userSnapshot = transaction.get(userRef);
                id = userSnapshot.getString("id");
            } else {
                id = sharedPreferences.getString("studentNumber", "");
            }

            Map<String, Object> ticket = new HashMap<>();
            ticket.put("createdAt", FieldValue.serverTimestamp());
            ticket.put("isPWD", isPWD);
            ticket.put("number", newNumber);
            ticket.put("service", selectedQueueType);
            ticket.put("status", "waiting");
            ticket.put("userid", id);

            DocumentReference ticketRef = db.collection("TICKETS").document();
            transaction.set(ticketRef, ticket);
            return null;
        }).addOnSuccessListener(assignedNumber -> {
            Log.d("TICKET", "Ticket # " + assignedNumber);
        }).addOnFailureListener(e -> {
            Log.w("TICKET", e.getMessage(), e);
        });

    }

    // START QUEUE
    protected void startQueueButton() {
        enterQueueButton.setOnClickListener(view ->{
            dialogPWD.show();
            startPWD();
        });
    }

    // PWD POP UP
    protected void startPWD() {
        PWDConfirmButton.setOnClickListener(view -> {
            startSelectQueue();
            isPWD = true;
            dialogPWD.dismiss();
        });

        PWDDeclineButton.setOnClickListener(view -> {
            startSelectQueue();
            dialogPWD.dismiss();
        });

        PWDCloseButton.setOnClickListener(view -> {
            dialogPWD.dismiss();
        });
    }

    // SELECT QUEUE POP UP
    protected void startSelectQueue() {
        dialogSelectQueue.show();
        selectQueueNextButton.setOnClickListener(view -> {
            selectedQueueType = spinnerSelectQueue.getSelectedItem().toString().toLowerCase();
            startSelectForm();
            dialogSelectQueue.dismiss();
        });

        selectQueueCloseButton.setOnClickListener(view -> {
            isPWD = false;
            dialogSelectQueue.dismiss();
        });
    }

    // SELECT FORM POP UP
    // MAIN
    protected void startSelectForm() {
        dialogSelectForm.show();

        selectFormNextButton.setOnClickListener(view -> {
            if (spinnerSelectForm.getSelectedItem().toString().equalsIgnoreCase("None")) {
                updateQueue();
                dialogSelectForm.dismiss();
            }

            if (spinnerSelectForm.getSelectedItem().toString().equalsIgnoreCase("Scholarship Application Form")) {
                dialogSelectForm.dismiss();
                startActivity(new Intent(HomeActivity.this, saf_page1.class));
                finish();
            }

            if (spinnerSelectForm.getSelectedItem().toString().equalsIgnoreCase("Scholarship Renewal Form")) {
                dialogSelectForm.dismiss();
                startActivity(new Intent(HomeActivity.this, srf_page1.class));
                finish();
            }
        });

        selectFormCloseButton.setOnClickListener(view -> {
            dialogSelectForm.dismiss();
        });
    }

    protected void setDialogsAndButtons() {
        enterQueueButton = findViewById(R.id.enter_the_queue_button);

        dialogPWD = new Dialog(HomeActivity.this);
        dialogPWD.setContentView(R.layout.pop_up_pwd);
        dialogPWD.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogPWD.setCancelable(true);

        PWDConfirmButton = dialogPWD.findViewById(R.id.confirm_button);
        PWDDeclineButton = dialogPWD.findViewById(R.id.decline_button);
        PWDCloseButton = dialogPWD.findViewById(R.id.close_button);

        dialogSelectQueue = new Dialog(HomeActivity.this);
        dialogSelectQueue.setContentView(R.layout.pop_up_select_queue);
        dialogSelectQueue.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogSelectQueue.setCancelable(true);

        selectQueueNextButton = dialogSelectQueue.findViewById(R.id.queue_next_button);
        selectQueueCloseButton = dialogSelectQueue.findViewById(R.id.close_button);

        dialogSelectForm = new Dialog(HomeActivity.this);
        dialogSelectForm.setContentView(R.layout.pop_up_select_form);
        dialogSelectForm.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogSelectForm.setCancelable(true);

        selectFormNextButton = dialogSelectForm.findViewById(R.id.enter_queue_button);
        selectFormCloseButton = dialogSelectForm.findViewById(R.id.close_button);
    }

    protected void setCategories() {
        admission = findViewById(R.id.admission_queue);
        registrar = findViewById(R.id.registrar_queue);
        cashier = findViewById(R.id.cashier_queue);

        admissionDivider = admission.findViewById(R.id.divider);
        registrarDivider = registrar.findViewById(R.id.divider);
        cashierDivider = cashier.findViewById(R.id.divider);

        admissionDivider.setBackgroundColor(getResources().getColor(R.color.blue, null));
        registrarDivider.setBackgroundColor(getResources().getColor(R.color.red, null));
        cashierDivider.setBackgroundColor(getResources().getColor(R.color.green, null));
    }

    protected void setSpinner() {
        spinnerSelectQueue = dialogSelectQueue.findViewById(R.id.spinner_select_queue);
        queueTypes.addAll(Arrays.asList(QueueType.values()));
        ArrayAdapter<QueueType> queueTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, queueTypes);
        spinnerSelectQueue.setAdapter(queueTypeAdapter);

        spinnerSelectForm = dialogSelectForm.findViewById(R.id.spinner_select_form);
        for (Forms form : Forms.values()) {
            if (form.toString().contains("_")) {
                forms.add(form.toString().replaceAll("_", " "));
                continue;
            }
            forms.add(form.toString());
        }
        ArrayAdapter<String> formAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, forms);
        spinnerSelectForm.setAdapter(formAdapter);
    }
}