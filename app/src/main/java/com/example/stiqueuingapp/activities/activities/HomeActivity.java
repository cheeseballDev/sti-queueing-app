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
import android.widget.FrameLayout;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.stiqueuingapp.R;
import com.example.stiqueuingapp.activities.enums.Forms;
import com.example.stiqueuingapp.activities.enums.QueueType;
import com.example.stiqueuingapp.activities.forms.saf_page1;
import com.example.stiqueuingapp.activities.forms.srf_page1;
import com.example.stiqueuingapp.activities.fragments.FragmentAdmission;
import com.example.stiqueuingapp.activities.fragments.FragmentCashier;
import com.example.stiqueuingapp.activities.fragments.FragmentRegistrar;
import com.example.stiqueuingapp.activities.models.HomeViewModel;
import com.google.android.material.tabs.TabLayout;
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

    private Button
            enterQueueButton,
            PWDConfirmButton, PWDDeclineButton,
            leaveQueueConfirmButton, leaveQueueDeclineButton,
            selectQueueNextButton,
            selectFormNextButton,
            successQueueCloseButton,
            infoCloseButton,
            notificationCloseButton;

    private ImageButton
            PWDCloseButton,
            selectQueueCloseButton,
            selectFormCloseButton,
            successQueueCloseImageButton,
            infoButton,
            infoCloseImageButton,
            notificationCloseImageButton;

    private TextView
            userNumber,
            successQueueNumber,
            infoQueueNumber, infoQueueDate, infoQueueId,
            notificationQueueServiceType, notificationCounterNumber;

    private HomeViewModel viewModel;

    private Dialog dialogPWD, dialogLeaveQueue, dialogSelectQueue, dialogSelectForm, dialogSuccessForm, dialogInfo, dialogNotification;

    private TabLayout tabLayout;

    private FrameLayout frameLayout;

    private Spinner spinnerSelectQueue, spinnerSelectForm;

    private boolean isPWD = false, isForm = false, isNewUser = false, isInQueue = false, shouldShowQueueSuccessPopup = false;

    private String id = "", ticketQueueType = null, selectedQueueType;

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
        setSpinner();
        updateQueue();
        updateEnterQueueButton();
        startQueueButton();
        startTabButtons();
        checkIfUserIsServing();
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

    /*
        LISTENERS
     */

    protected void setUserSelectedQueueType() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("TICKETS")
                .whereEqualTo("userid", id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty())
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                            selectedQueueType = document.getString("service");
                });
    }

    protected void updateQueue() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                                ticketQueueType = document.getString("service").toUpperCase();
                                if (ticketNumber != null) {
                                    updateUserNumberType(ticketQueueType, isTicketPWD, formattedNumber);
                                    infoQueueDate.setText(document.getDate("createdAt").toString());
                                    infoQueueId.setText(document.getId());
                                    isInQueue = true;
                                    updateEnterQueueButton();

                                    String currentServingNumber = getCurrentServingNumber(ticketQueueType);
                                    if (currentServingNumber != null && currentServingNumber.equals(userNumber.getText().toString())) {
                                        notificationCounterNumber.setText(new StringBuilder().append(ticketNumber));
                                        notificationQueueServiceType.setText(new StringBuilder().append(ticketQueueType));
                                        showNotification();
                                    }
                                    return;
                                }

                            }
                        } else {
                            userNumber.setText("N/A");
                            infoQueueNumber.setText("N/A");
                            infoQueueDate.setText("N/A");
                            infoQueueId.setText("N/A");
                            isInQueue = false;
                            updateEnterQueueButton();
                        }
                    }
                });
    }

    private void checkIfUserIsServing() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("TICKETS")
                .whereEqualTo("userid", id)
                .whereEqualTo("status", "serving")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (!snapshot.isEmpty()) {
                            for (DocumentSnapshot document : snapshot.getDocuments()) {
                                notificationQueueServiceType.setText(new StringBuilder().append(document.getString("service")));
                                notificationCounterNumber.setText(new StringBuilder().append(document.getString("number")));
                                showNotification();
                                return;
                            }
                        }
                    }
                });
    }

    private String getCurrentServingNumber(String queueType) {
        switch (queueType) {
            case "ADMISSION":
                if (viewModel.getAdmissionCounter1QueueNumber().getValue() != null && !viewModel.getAdmissionCounter1QueueNumber().getValue().equals("ON-BRK"))
                    return viewModel.getAdmissionCounter1QueueNumber().getValue();
                if (viewModel.getAdmissionCounter2QueueNumber().getValue() != null && !viewModel.getAdmissionCounter2QueueNumber().getValue().equals("ON-BRK"))
                    return viewModel.getAdmissionCounter2QueueNumber().getValue();
                if (viewModel.getAdmissionCounter3QueueNumber().getValue() != null && !viewModel.getAdmissionCounter3QueueNumber().getValue().equals("ON-BRK"))
                    return viewModel.getAdmissionCounter3QueueNumber().getValue();
                break;
            case "CASHIER":
                if (viewModel.getCashierCounter1QueueNumber().getValue() != null && !viewModel.getCashierCounter1QueueNumber().getValue().equals("ON-BRK"))
                    return viewModel.getCashierCounter1QueueNumber().getValue();
                if (viewModel.getCashierCounter2QueueNumber().getValue() != null && !viewModel.getCashierCounter2QueueNumber().getValue().equals("ON-BRK"))
                    return viewModel.getCashierCounter2QueueNumber().getValue();
                if (viewModel.getCashierCounter3QueueNumber().getValue() != null && !viewModel.getCashierCounter3QueueNumber().getValue().equals("ON-BRK"))
                    return viewModel.getCashierCounter3QueueNumber().getValue();
                break;
            case "REGISTRAR":
                if (viewModel.getRegistrarCounter1QueueNumber().getValue() != null && !viewModel.getRegistrarCounter1QueueNumber().getValue().equals("ON-BRK"))
                    return viewModel.getRegistrarCounter1QueueNumber().getValue();
                if (viewModel.getRegistrarCounter2QueueNumber().getValue() != null && !viewModel.getRegistrarCounter2QueueNumber().getValue().equals("ON-BRK"))
                    return viewModel.getRegistrarCounter2QueueNumber().getValue();
                if (viewModel.getRegistrarCounter3QueueNumber().getValue() != null && !viewModel.getRegistrarCounter3QueueNumber().getValue().equals("ON-BRK"))
                    return viewModel.getRegistrarCounter3QueueNumber().getValue();
                break;
            default:
                return null;
        }
        return null;
    }
    protected void updateUserNumberType(String ticketQueueType, boolean isTicketPWD, String formattedNumber) {
        if (isTicketPWD) {
            switch (ticketQueueType) {
                case "ADMISSION":
                    userNumber.setText(new StringBuilder().append("A-P-").append(formattedNumber));
                    infoQueueNumber.setText(new StringBuilder().append("A-P-").append(formattedNumber));
                    successQueueNumber.setText(new StringBuilder().append("A-P-").append(formattedNumber));
                    return;
                case "CASHIER":
                    userNumber.setText(new StringBuilder().append("C-P-").append(formattedNumber));
                    infoQueueNumber.setText(new StringBuilder().append("C-P-").append(formattedNumber));
                    successQueueNumber.setText(new StringBuilder().append("C-P-").append(formattedNumber));
                    return;
                case "REGISTRAR":
                    userNumber.setText(new StringBuilder().append("R-P-").append(formattedNumber));
                    infoQueueNumber.setText(new StringBuilder().append("R-P-").append(formattedNumber));
                    successQueueNumber.setText(new StringBuilder().append("R-P-").append(formattedNumber));
            }
        } else {
            switch (ticketQueueType) {
                case "ADMISSION":
                    userNumber.setText(new StringBuilder().append("A-").append(formattedNumber));
                    infoQueueNumber.setText(new StringBuilder().append("A-").append(formattedNumber));
                    successQueueNumber.setText(new StringBuilder().append("A-").append(formattedNumber));
                    return;
                case "CASHIER":
                    userNumber.setText(new StringBuilder().append("C-").append(formattedNumber));
                    infoQueueNumber.setText(new StringBuilder().append("C-").append(formattedNumber));
                    successQueueNumber.setText(new StringBuilder().append("C-").append(formattedNumber));
                    return;
                case "REGISTRAR":
                    userNumber.setText(new StringBuilder().append("R-").append(formattedNumber));
                    infoQueueNumber.setText(new StringBuilder().append("R-").append(formattedNumber));
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

    /*
        CRUD OPERATIONS
     */

    protected void createNewTicket(FirebaseFirestore db, long newNumber) {
        db.runTransaction(  transaction -> {
            Map<String, Object> ticket = new HashMap<>();
            ticket.put("createdAt", FieldValue.serverTimestamp());
            ticket.put("isPWD", isPWD);
            ticket.put("isForm", isForm);
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

        infoButton.setOnClickListener(view -> {
            showInfoForm();
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
            if (selectedQueueType.equals("registrar") || selectedQueueType.equals("admission")) {
                showSelectFormType();
                dialogSelectQueue.dismiss();
                return;
            }
            updateQueueNumber();
            updateEnterQueueButton();
            showSuccessQueueForm();
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
                        .putBoolean("isForm", true)
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
                        .putBoolean("isForm", true)
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

    protected void showInfoForm() {
        dialogInfo.show();

        infoCloseImageButton.setOnClickListener(view -> {
            dialogInfo.dismiss();
        });

        infoCloseButton.setOnClickListener(view -> {
            dialogInfo.dismiss();
        });
    }

    protected void showNotification() {
        dialogNotification.show();

        notificationCloseImageButton.setOnClickListener(view -> {
            dialogNotification.dismiss();
        });

        notificationCloseButton.setOnClickListener(view -> {
            dialogNotification.dismiss();
        });
    }

    /*
        SET THE ENTIRE FRONTEND
     */

    protected void startTabButtons() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new FragmentAdmission())
                .addToBackStack(null)
                .commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch(tab.getPosition()) {
                    case 0:
                        fragment = new FragmentAdmission();
                        break;
                    case 1:
                        fragment = new FragmentCashier();
                        break;
                    case 2:
                        fragment = new FragmentRegistrar();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    protected void setDialogsAndButtons() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        userNumber = findViewById(R.id.user_number);
        frameLayout = findViewById(R.id.frameLayout);
        tabLayout = findViewById(R.id.tabLayout);

        enterQueueButton = findViewById(R.id.enter_the_queue_button);
        infoButton = findViewById(R.id.user_queue_info);

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

        dialogInfo = new Dialog(HomeActivity.this);
        dialogInfo.setContentView(R.layout.pop_up_queue_info);
        dialogInfo.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogInfo.setCancelable(true);

        infoCloseImageButton = dialogInfo.findViewById(R.id.close_button);
        infoCloseButton = dialogInfo.findViewById(R.id.info_close_button);
        infoQueueDate = dialogInfo.findViewById(R.id.user_queue_date);
        infoQueueId = dialogInfo.findViewById(R.id.user_queue_id);
        infoQueueNumber = dialogInfo.findViewById(R.id.user_queue_number);

        dialogNotification = new Dialog(HomeActivity.this);
        dialogNotification.setContentView(R.layout.pop_up_notification);
        dialogNotification.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogNotification.setCancelable(true);

        notificationCloseButton = dialogNotification.findViewById(R.id.notification_close_button);
        notificationCloseImageButton = dialogNotification.findViewById(R.id.close_button);
        notificationQueueServiceType = dialogNotification.findViewById(R.id.notification_queue_service_type);
        notificationCounterNumber = dialogNotification.findViewById(R.id.notification_number_type);
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
            sharedPreferences = getSharedPreferences("HomePreferences", MODE_PRIVATE);
            isPWD = sharedPreferences.getBoolean("isPWD", isPWD);
            isInQueue = sharedPreferences.getBoolean("isInQueue", isInQueue);
            isForm = sharedPreferences.getBoolean("isForm", isForm);
            id = sharedPreferences.getString("userid", id);
            selectedQueueType = sharedPreferences.getString("queueType",selectedQueueType);
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