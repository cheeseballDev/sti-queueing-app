package com.example.stiqueuingapp.activities.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stiqueuingapp.R;

public class HomeActivity extends AppCompatActivity {

    private View admission, registrar, cashier, admissionDivider, registrarDivider, cashierDivider;

    private Button enterQueueButton, PWDConfirmButton, PWDDeclineButton;

    private ImageButton  PWDCloseButton;

    private Dialog dialogPWD, dialogSelectQueue;

    private boolean isPWD = false;

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
        dialogSelectQueue.show();
    }

    protected void startPWD() {
        PWDConfirmButton = dialogPWD.findViewById(R.id.confirm_button);
        PWDDeclineButton = dialogPWD.findViewById(R.id.decline_button);
        PWDCloseButton = dialogPWD.findViewById(R.id.close_button);

        PWDConfirmButton.setOnClickListener(view -> {
            startSelectQueue();
            isPWD = true;
        });

        PWDDeclineButton.setOnClickListener(view -> {
            startSelectQueue();
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