package com.example.stiqueuingapp.activities.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stiqueuingapp.R;
import com.example.stiqueuingapp.activities.enums.QueueType;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {

    private View admission, registrar, cashier, admissionDivider, registrarDivider, cashierDivider;

    private Button enterQueueButton, PWDConfirmButton, PWDDeclineButton, selectQueueNextButton;

    private ImageButton  PWDCloseButton, selectQueueCloseButton;

    private Dialog dialogPWD, dialogSelectQueue;

    private Spinner spinnerSelectQueue;

    private boolean isPWD = false;

    private ArrayList<QueueType> queueTypes = new ArrayList<>();

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
        setCategories();
        setDialogs();
        startQueueButton();
    }

    protected void startQueueButton() {
        enterQueueButton = findViewById(R.id.enter_the_queue_button);
        enterQueueButton.setOnClickListener(view ->{
            dialogPWD.show();
            startPWD();
        });
    }

    protected void startSelectQueue() {
        selectQueueNextButton = dialogSelectQueue.findViewById(R.id.queue_next_button);
        selectQueueCloseButton = dialogSelectQueue.findViewById(R.id.close_button);

        dialogSelectQueue.show();

        spinnerSelectQueue = dialogSelectQueue.findViewById(R.id.spinner_select_queue);
        queueTypes.addAll(Arrays.asList(QueueType.values()));
        ArrayAdapter<QueueType> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, queueTypes);
        spinnerSelectQueue.setAdapter(adapter);

        selectQueueNextButton.setOnClickListener(view -> {

        });

        selectQueueCloseButton.setOnClickListener(view -> {

        });
    }

    protected void startPWD() {
        PWDConfirmButton = dialogPWD.findViewById(R.id.confirm_button);
        PWDDeclineButton = dialogPWD.findViewById(R.id.decline_button);
        PWDCloseButton = dialogPWD.findViewById(R.id.close_button);

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

    protected void setDialogs() {
        dialogPWD = new Dialog(HomeActivity.this);
        dialogPWD.setContentView(R.layout.pop_up_pwd);
        dialogPWD.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogPWD.setCancelable(true);

        dialogSelectQueue = new Dialog(HomeActivity.this);
        dialogSelectQueue.setContentView(R.layout.pop_up_select_queue);
        dialogSelectQueue.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogSelectQueue.setCancelable(true);


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
}