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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stiqueuingapp.R;
import com.example.stiqueuingapp.activities.enums.Forms;
import com.example.stiqueuingapp.activities.enums.QueueType;
import com.example.stiqueuingapp.activities.forms.saf_page1;
import com.example.stiqueuingapp.activities.forms.srf_page1;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private View admission, registrar, cashier,
            admissionDivider, registrarDivider, cashierDivider;

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

    private boolean isPWD = false, isNewUser = false, isQueuePWD = false;

    private String selectedQueueType;

    private String id = "";

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
        setQueues();
        setSpinner();
        setUserId();
        startQueueButton();
        updateQueue();
    }

    protected void setUserId() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

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
            return null;
        });
    }

    protected void updateQueue() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference admissionRef = db.collection("QUEUES").document("ADMISSION");
        DocumentReference cashierRef = db.collection("QUEUES").document("CASHIER");
        DocumentReference registrarRef = db.collection("QUEUES").document("REGISTRAR");
        CollectionReference ticketsRef = FirebaseFirestore.getInstance().collection("TICKETS");

        admissionRef.addSnapshotListener(
                new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (snapshot.exists()) {
                            Long currentNumber = snapshot.getLong("currentNumber");
                            Long currentCounter = snapshot.getLong("counter");
                            Long currentCutOff = snapshot.getLong("cutOffNumber");
                            long convertedNumber = (currentNumber != null) ? currentNumber : 1L;
                            String formattedNumber = String.format("%03d", convertedNumber);

                            ticketsRef.whereEqualTo("service", "admission")
                                    .whereEqualTo("number", convertedNumber)
                                    .limit(1)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                                            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                                                isQueuePWD = Boolean.TRUE.equals(doc.getBoolean("isPWD"));
                                            }
                                        }
                                    });

                            if (isQueuePWD) {
                                admissionCurrentQueueNumber.setText(new StringBuilder().append("A-P-").append(formattedNumber));
                            } else {
                                admissionCurrentQueueNumber.setText(new StringBuilder().append("A-").append(formattedNumber));
                            }
                            admissionCurrentCounter.setText(new StringBuilder().append(currentCounter));
                            admissionCurrentCutOff.setText(new StringBuilder().append(currentCutOff));
                        }
                    }
                }
        );

        registrarRef.addSnapshotListener(
                new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (snapshot.exists()) {
                            Long currentNumber = snapshot.getLong("currentNumber");
                            Long currentCounter = snapshot.getLong("counter");
                            Long currentCutOff = snapshot.getLong("cutOffNumber");
                            long convertedNumber = (currentNumber != null) ? currentNumber : 1L;
                            String formattedNumber = String.format("%03d", convertedNumber);

                            ticketsRef.whereEqualTo("service", "registrar")
                                    .whereEqualTo("number", convertedNumber)
                                    .limit(1)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                                            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                                                isQueuePWD = Boolean.TRUE.equals(doc.getBoolean("isPWD"));
                                            }
                                        }
                                    });

                            if (isQueuePWD) {
                                registrarCurrentQueueNumber.setText(new StringBuilder().append("R-P-").append(formattedNumber));
                            } else {
                                registrarCurrentQueueNumber.setText(new StringBuilder().append("R-").append(formattedNumber));
                            }
                            registrarCurrentCounter.setText(new StringBuilder().append(currentCounter));
                            registrarCurrentCutOff.setText(new StringBuilder().append(currentCutOff));
                        }
                    }
                }
        );

        cashierRef.addSnapshotListener(
                new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (snapshot.exists()) {
                            Long currentNumber = snapshot.getLong("currentNumber");
                            Long currentCounter = snapshot.getLong("counter");
                            Long currentCutOff = snapshot.getLong("cutOffNumber");
                            long convertedNumber = (currentNumber != null) ? currentNumber : 1L;
                            String formattedNumber = String.format("%03d", convertedNumber);

                            ticketsRef.whereEqualTo("service", "cashier")
                                    .whereEqualTo("number", convertedNumber)
                                    .limit(1)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                                            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                                                isQueuePWD = Boolean.TRUE.equals(doc.getBoolean("isPWD"));
                                            }
                                        }
                                    });

                            if (isQueuePWD) {
                                cashierCurrentQueueNumber.setText(new StringBuilder().append("C-P-").append(formattedNumber));
                            } else {
                                cashierCurrentQueueNumber.setText(new StringBuilder().append("C-").append(formattedNumber));
                            }
                            cashierCurrentCounter.setText(new StringBuilder().append(currentCounter));
                            cashierCurrentCutOff.setText(new StringBuilder().append(currentCutOff));
                        }
                    }
                }
        );

        db.collection("TICKETS")
                .whereEqualTo("userid", id)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (!snapshot.isEmpty()) {
                            for (DocumentSnapshot document : snapshot.getDocuments()) {
                                Long ticketNumber = document.getLong("number");
                                String formattedNumber = String.format("%03d", ticketNumber);
                                boolean isTicketPWD = Boolean.TRUE.equals(document.getBoolean("isPWD"));
                                String ticketQueueType = document.getString("service").toUpperCase();
                                if (ticketNumber != null) {
                                    updateUserNumber(ticketQueueType, isTicketPWD, formattedNumber);
                                }
                                return;
                            }
                        } else {
                            userNumber.setText("N/A");
                        }
                    }
                });
    }

    protected void updateUserNumber(String ticketQueueType, boolean isTicketPWD, String formattedNumber) {
        if (isTicketPWD) {
            switch (ticketQueueType) {
                case "ADMISSION":
                    userNumber.setText(new StringBuilder().append("A-P-").append(formattedNumber));
                    return;
                case "CASHIER":
                    userNumber.setText(new StringBuilder().append("C-P-").append(formattedNumber));
                    return;
                case "REGISTRAR":
                    userNumber.setText(new StringBuilder().append("R-P-").append(formattedNumber));
            }
        } else {
            switch (ticketQueueType) {
                case "ADMISSION":
                    userNumber.setText(new StringBuilder().append("A-").append(formattedNumber));
                    return;
                case "CASHIER":
                    userNumber.setText(new StringBuilder().append("C-").append(formattedNumber));
                    return;
                case "REGISTRAR":
                    userNumber.setText(new StringBuilder().append("R-").append(formattedNumber));
            }
        }
    }

    protected void updateQueueNumber() {;
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference queueRef = db.collection("QUEUES").document(selectedQueueType.toUpperCase());

        db.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(queueRef);
            Long currentNumber = snapshot.getLong("currentNumber");
            long newNumber = (currentNumber != null) ? currentNumber + 1 : 1L;
            transaction.update(queueRef, "currentNumber", newNumber);
            createNewTicket(db, newNumber);
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
            ticket.put("status", "waiting");
            ticket.put("userid", id);

            DocumentReference ticketRef = db.collection("TICKETS").document();
            transaction.set(ticketRef, ticket);
            return null;
        });
    }

    protected void startQueueButton() {
        enterQueueButton.setOnClickListener(view ->{
            dialogPWD.show();
            startPWD();
        });
    }

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


    protected void startSelectForm() {
        dialogSelectForm.show();

        selectFormNextButton.setOnClickListener(view -> {
            if (spinnerSelectForm.getSelectedItem().toString().equalsIgnoreCase("None")) {
                updateQueueNumber();
                // to be updated with success ticket
                dialogSelectForm.dismiss();
            }

            if (spinnerSelectForm.getSelectedItem().toString().equalsIgnoreCase("Scholarship Application Form")) {
                dialogSelectForm.dismiss();
                //to be updated
                startActivity(new Intent(HomeActivity.this, saf_page1.class));
                finish();
            }

            if (spinnerSelectForm.getSelectedItem().toString().equalsIgnoreCase("Scholarship Renewal Form")) {
                dialogSelectForm.dismiss();
                //to be updated
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

    protected void setQueues() {
        userNumber = findViewById(R.id.user_number);
        userCooldown = findViewById(R.id.user_cooldown);

        admissionCurrentCounter = admission.findViewById(R.id.queue_current_counter);
        admissionCurrentQueueNumber = admission.findViewById(R.id.queue_current_number);
        admissionCurrentCutOff = admission.findViewById(R.id.queue_current_cut_off);

        registrarCurrentCounter = registrar.findViewById(R.id.queue_current_counter);
        registrarCurrentQueueNumber = registrar.findViewById(R.id.queue_current_number);
        registrarCurrentCutOff = registrar.findViewById(R.id.queue_current_cut_off);

        cashierCurrentCounter = cashier.findViewById(R.id.queue_current_counter);
        cashierCurrentQueueNumber = cashier.findViewById(R.id.queue_current_number);
        cashierCurrentCutOff = cashier.findViewById(R.id.queue_current_cut_off);
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