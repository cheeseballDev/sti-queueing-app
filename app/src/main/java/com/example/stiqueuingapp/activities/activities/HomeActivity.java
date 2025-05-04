package com.example.stiqueuingapp.activities.activities;

import android.app.Dialog;
import android.content.Context;
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
import androidx.core.content.ContextCompat;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private View admission, registrar, cashier;

    private Button
            enterQueueButton,
            PWDConfirmButton, PWDDeclineButton,
            leaveQueueConfirmButton, leaveQueueDeclineButton,
            selectQueueNextButton,
            selectFormNextButton,
            successQueueCloseButton ;

    private ImageButton
            PWDCloseButton,
            selectQueueCloseButton,
            selectFormCloseButton,
            successQueueCloseImageButton;

    private TextView
            userNumber, userCooldown,
            successQueueNumber,
            admissionCurrentQueueNumber, registrarCurrentQueueNumber, cashierCurrentQueueNumber,
            admissionCurrentCutOff, registrarCurrentCutOff, cashierCurrentCutOff,
            admissionCurrentCounter, registrarCurrentCounter, cashierCurrentCounter;

    private Dialog dialogPWD, dialogLeaveQueue, dialogSelectQueue, dialogSelectForm, dialogSuccessForm;

    private Spinner spinnerSelectQueue, spinnerSelectForm;

    private boolean isPWD = false, isNewUser = false, isInQueue = false, shouldShowQueueSuccessPopup = false;

    private String selectedQueueType;

    private String id = "";

    private ArrayList<QueueType> queueTypes = new ArrayList<>();

    private ArrayList<String> forms = new ArrayList<>();

    private SharedPreferences sharedPreferences;

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
        setUserId(new Callback<Void>() {
            @Override
            public void onSuccess() {
                setUserSelectedQueueType();
            }
            @Override
            public void onFailure(Exception e) {
                Log.w("FIREBASE", "Error getting documents:" + e);
            }
        });
        setDialogsAndButtons();
        setCategories();
        setQueues();
        setSpinner();
        updateQueue();
        updateEnterQueueButton();
        startQueueButton();
    }

    /*
        BACKEND LOGIC
     */

    protected void setUserId(final Callback<Void> callback) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.runTransaction(transaction -> {
            sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
            isNewUser = sharedPreferences.getBoolean("isNewUser", false);
            String email = sharedPreferences.getString("userEmail", "");
            if (isNewUser) {
                DocumentReference userRef = db.collection("USERS").document(email);
                DocumentSnapshot userSnapshot = transaction.get(userRef);
                id = userSnapshot.getString("id");
                deleteTicket(new Callback<Void>() {
                    @Override
                    public void onSuccess() {
                        setUserSelectedQueueType();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.w("FIREBASE", "Error getting documents:" + e);
                    }
                });
                callback.onSuccess();
            } else {
                id = sharedPreferences.getString("studentNumber", "");
                callback.onSuccess();
            }
            return null;
        }).addOnFailureListener(callback::onFailure);
    }

    protected void setUserSelectedQueueType() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("TICKETS")
                .whereEqualTo("userid", id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Log.w("FIREBASE", "SELECTED QUEUE TYPE" + document.getString("service"));
                            selectedQueueType = document.getString("service");
                        }
                    }
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
                                            boolean isAdmissionQueuePWD = false;
                                            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                                                isAdmissionQueuePWD = doc.getBoolean("isPWD");
                                            }
                                            if (isAdmissionQueuePWD) {
                                                admissionCurrentQueueNumber.setText(new StringBuilder().append("A-P-").append(formattedNumber));
                                            } else {
                                                admissionCurrentQueueNumber.setText(new StringBuilder().append("A-").append(formattedNumber));
                                            }
                                        }
                                    });
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
                                            boolean isRegistrarQueuePWD = false;
                                            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                                                isRegistrarQueuePWD = doc.getBoolean("isPWD");
                                            }
                                            if (isRegistrarQueuePWD) {
                                                registrarCurrentQueueNumber.setText(new StringBuilder().append("R-P-").append(formattedNumber));
                                            } else {
                                                registrarCurrentQueueNumber.setText(new StringBuilder().append("R-").append(formattedNumber));
                                            }
                                        }
                                    });
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
                                            boolean isCashierQueuePWD = false;
                                            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                                                isCashierQueuePWD = doc.getBoolean("isPWD");
                                            }
                                            if (isCashierQueuePWD) {
                                                cashierCurrentQueueNumber.setText(new StringBuilder().append("C-P-").append(formattedNumber));
                                            } else {
                                                cashierCurrentQueueNumber.setText(new StringBuilder().append("C-").append(formattedNumber));
                                            }
                                        }
                                    });
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
                                boolean isTicketPWD = document.getBoolean("isPWD");
                                String ticketQueueType = document.getString("service").toUpperCase();
                                if (ticketNumber != null) {
                                    updateUserNumberType(ticketQueueType, isTicketPWD, formattedNumber);
                                    isInQueue = true;
                                    updateEnterQueueButton();
                                    return;
                                }
                            }
                        } else {
                            userNumber.setText("N/A");
                            isInQueue = false;
                            updateEnterQueueButton();
                        }
                    }
                });
    }

    protected void updateUserNumberType(String ticketQueueType, boolean isTicketPWD, String formattedNumber) {
        if (isTicketPWD) {
            switch (ticketQueueType) {
                case "ADMISSION":
                    userNumber.setText(new StringBuilder().append("A-P-").append(formattedNumber));
                    successQueueNumber.setText(new StringBuilder().append("A-P-").append(formattedNumber));
                    return;
                case "CASHIER":
                    userNumber.setText(new StringBuilder().append("C-P-").append(formattedNumber));
                    successQueueNumber.setText(new StringBuilder().append("C-P-").append(formattedNumber));
                    return;
                case "REGISTRAR":
                    userNumber.setText(new StringBuilder().append("R-P-").append(formattedNumber));
                    successQueueNumber.setText(new StringBuilder().append("R-P-").append(formattedNumber));
            }
        } else {
            switch (ticketQueueType) {
                case "ADMISSION":
                    userNumber.setText(new StringBuilder().append("A-").append(formattedNumber));
                    successQueueNumber.setText(new StringBuilder().append("A-").append(formattedNumber));
                    return;
                case "CASHIER":
                    userNumber.setText(new StringBuilder().append("C-").append(formattedNumber));
                    successQueueNumber.setText(new StringBuilder().append("C-").append(formattedNumber));
                    return;
                case "REGISTRAR":
                    userNumber.setText(new StringBuilder().append("R-").append(formattedNumber));
                    successQueueNumber.setText(new StringBuilder().append("R-").append(formattedNumber));
            }
        }
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

    protected void updateEnterQueueButton() {
        if (isInQueue) {
            enterQueueButton.setText(R.string.leave_queue);
            enterQueueButton.setTextColor(getResources().getColor(R.color.decline_button_text));
            enterQueueButton.setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.decline_button));
            return;
        }
        enterQueueButton.setText(R.string.enter_queue);
        enterQueueButton.setTextColor(getResources().getColor(R.color.ghost_button_text));
        enterQueueButton.setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.ghost_button));
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

    protected void deleteTicket(final Callback<Void> callback) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("TICKETS")
                .whereEqualTo("userid", id)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    callback.onSuccess();
                                })
                                .addOnFailureListener(callback::onFailure);
                    }
                });
    }

    /*
        START THE LISTENERS FOR DIALOG BUTTONS
     */

    protected void startQueueButton() {
        enterQueueButton.setOnClickListener(view ->{
            if (enterQueueButton.getText().equals("Leave Queue")) {
                startLeaveQueue();
                return;
            }
            dialogPWD.show();
            showPWDForm();
        });
    }

    protected void startLeaveQueue() {
        dialogLeaveQueue.show();

        leaveQueueConfirmButton.setOnClickListener(view -> {
            deleteTicket(new Callback<Void>() {
                @Override
                public void onSuccess() {
                    isInQueue = true;
                    updateQueueNumber();
                }
                @Override
                public void onFailure(Exception e) {
                    Log.w("FIREBASE", "Error deleting ticket:" + e);
                }
            });
            dialogLeaveQueue.dismiss();
        });

        leaveQueueDeclineButton.setOnClickListener(view -> {
            dialogLeaveQueue.dismiss();
        });
    }

    protected void showPWDForm() {
        PWDConfirmButton.setOnClickListener(view -> {
            showSelectQueueForm();
            isPWD = true;
            dialogPWD.dismiss();
        });

        PWDDeclineButton.setOnClickListener(view -> {
            showSelectQueueForm();
            isPWD = false;
            dialogPWD.dismiss();
        });

        PWDCloseButton.setOnClickListener(view -> {
            dialogPWD.dismiss();
        });
    }

    protected void showSelectQueueForm() {
        dialogSelectQueue.show();
        selectQueueNextButton.setOnClickListener(view -> {
            selectedQueueType = spinnerSelectQueue.getSelectedItem().toString().toLowerCase();
            showSelectFormType();
            dialogSelectQueue.dismiss();
        });

        selectQueueCloseButton.setOnClickListener(view -> {
            isPWD = false;
            dialogSelectQueue.dismiss();
        });
    }

    protected void showSelectFormType() {
        dialogSelectForm.show();

        selectFormNextButton.setOnClickListener(view -> {
            if (spinnerSelectForm.getSelectedItem().toString().equalsIgnoreCase("None")) {
                updateQueueNumber();
                updateEnterQueueButton();
                showSuccessQueueForm();
                dialogSelectForm.dismiss();
            }

            if (spinnerSelectForm.getSelectedItem().toString().equalsIgnoreCase("Scholarship Application Form")) {
                dialogSelectForm.dismiss();
                sharedPreferences = getSharedPreferences("HomePreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isPWD", isPWD)
                        .putBoolean("isInQueue", isInQueue)
                        .putString("userid", id)
                        .putString("queueType",selectedQueueType)
                        .apply();
                startActivity(new Intent(HomeActivity.this, saf_page1.class));
            }

            if (spinnerSelectForm.getSelectedItem().toString().equalsIgnoreCase("Scholarship Renewal Form")) {
                dialogSelectForm.dismiss();
                sharedPreferences = getSharedPreferences("HomePreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isPWD", isPWD)
                        .putBoolean("isInQueue", isInQueue)
                        .putString("userid", id)
                        .putString("queueType",selectedQueueType)
                        .apply();
                startActivity(new Intent(HomeActivity.this, srf_page1.class));
            }
        });

        selectFormCloseButton.setOnClickListener(view -> {
            dialogSelectForm.dismiss();
        });
    }

    protected void showSuccessQueueForm() {
        dialogSuccessForm.show();

        successQueueCloseButton.setOnClickListener(view -> {
            dialogSuccessForm.dismiss();
        });

        successQueueCloseImageButton.setOnClickListener(view -> {
            dialogSuccessForm.dismiss();
        });
    }

    /*
        SET THE ENTIRE FRONTEND
     */

    protected void setDialogsAndButtons() {
        enterQueueButton = findViewById(R.id.enter_the_queue_button);

        dialogPWD = new Dialog(HomeActivity.this);
        dialogPWD.setContentView(R.layout.pop_up_pwd_form);
        dialogPWD.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogPWD.setCancelable(true);

        dialogLeaveQueue = new Dialog(HomeActivity.this);
        dialogLeaveQueue.setContentView(R.layout.pop_up_leave_queue_form);
        dialogLeaveQueue.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogLeaveQueue.setCancelable(true);

        leaveQueueConfirmButton = dialogLeaveQueue.findViewById(R.id.confirm_button);
        leaveQueueDeclineButton = dialogLeaveQueue.findViewById(R.id.decline_button);

        PWDConfirmButton = dialogPWD.findViewById(R.id.confirm_button);
        PWDDeclineButton = dialogPWD.findViewById(R.id.decline_button);
        PWDCloseButton = dialogPWD.findViewById(R.id.close_button);

        dialogSelectQueue = new Dialog(HomeActivity.this);
        dialogSelectQueue.setContentView(R.layout.pop_up_select_queue_form);
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

        dialogSuccessForm = new Dialog(HomeActivity.this);
        dialogSuccessForm.setContentView(R.layout.pop_up_success_queue_form);
        dialogSuccessForm.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogSuccessForm.setCancelable(true);

        successQueueCloseImageButton = dialogSuccessForm.findViewById(R.id.close_button);
        successQueueNumber = dialogSuccessForm.findViewById(R.id.queue_number);
        successQueueCloseButton = dialogSuccessForm.findViewById(R.id.queue_close_button);
    }

    protected void setCategories() {
        admission = findViewById(R.id.admission_queue);
        registrar = findViewById(R.id.registrar_queue);
        cashier = findViewById(R.id.cashier_queue);

        View admissionDivider = admission.findViewById(R.id.divider);
        View registrarDivider = registrar.findViewById(R.id.divider);
        View cashierDivider = cashier.findViewById(R.id.divider);

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

        cashierCurrentCounter = cashier.findViewById(R.id.queue_current_counter);
        cashierCurrentQueueNumber = cashier.findViewById(R.id.queue_current_number);
        cashierCurrentCutOff = cashier.findViewById(R.id.queue_current_cut_off);

        registrarCurrentCounter = registrar.findViewById(R.id.queue_current_counter);
        registrarCurrentQueueNumber = registrar.findViewById(R.id.queue_current_number);
        registrarCurrentCutOff = registrar.findViewById(R.id.queue_current_cut_off);

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

    /*
        MISC
     */
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            shouldShowQueueSuccessPopup = intent.getBooleanExtra("shouldShowQueueSuccessPopup", false);
        }
        
        if (shouldShowQueueSuccessPopup) {
            showSuccessQueueForm();
            updateQueueNumber();
            updateEnterQueueButton();
            shouldShowQueueSuccessPopup = false;
        }
    }

    interface Callback<T> {
        void onSuccess();

        void onFailure(Exception e);
    }
}