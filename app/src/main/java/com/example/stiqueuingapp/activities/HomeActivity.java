package com.example.stiqueuingapp.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stiqueuingapp.R;

public class HomeActivity extends AppCompatActivity {

    private View admission, registrar, cashier, admissionDivider, registrarDivider, cashierDivider;

    private Button enterQueueButton, PWDconfirmButton, PWDdeclineButton;

    private Dialog dialogPWD;


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


        admission = findViewById(R.id.admission_queue);
        registrar = findViewById(R.id.registrar_queue);
        cashier = findViewById(R.id.cashier_queue);

        admissionDivider = admission.findViewById(R.id.divider);
        registrarDivider = registrar.findViewById(R.id.divider);
        cashierDivider = cashier.findViewById(R.id.divider);

        admissionDivider.setBackgroundColor(getResources().getColor(R.color.blue, null));
        registrarDivider.setBackgroundColor(getResources().getColor(R.color.red, null));
        cashierDivider.setBackgroundColor(getResources().getColor(R.color.green, null));

        dialogPWD = new Dialog(HomeActivity.this);
        dialogPWD.setContentView(R.layout.pop_up_pwd);
        dialogPWD.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogPWD.setCancelable(true);



        enterQueueButton = findViewById(R.id.enter_the_queue_button);
        PWDconfirmButton = dialogPWD.findViewById(R.id.confirm_button);
        PWDdeclineButton = dialogPWD.findViewById(R.id.decline_button);

        enterQueueButton.setOnClickListener(view ->{
            dialogPWD.show();
        });
    }

    protected void startPWD() {

        PWDconfirmButton.setOnClickListener(view -> {
            Log.d("HI","HO");
        });

        PWDdeclineButton.setOnClickListener(view -> {
            Log.d("HI", "HI");
        });
    }
}