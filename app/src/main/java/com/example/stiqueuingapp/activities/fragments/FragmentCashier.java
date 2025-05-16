package com.example.stiqueuingapp.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stiqueuingapp.R;
import com.example.stiqueuingapp.activities.models.HomeViewModel;

public class FragmentCashier extends Fragment {

    private HomeViewModel viewModel;

    private TextView cashierCurrentQueueNumber1, cashierCurrentCutOff1, cashierCurrentCounter1,
            cashierCurrentQueueNumber2, cashierCurrentCutOff2, cashierCurrentCounter2,
            cashierCurrentQueueNumber3, cashierCurrentCutOff3, cashierCurrentCounter3;


    public FragmentCashier() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cashier, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        View cashier1 = view.findViewById(R.id.cashier_queue_1);
        cashierCurrentQueueNumber1 = cashier1.findViewById(R.id.queue_current_number);
        cashierCurrentCounter1 = cashier1.findViewById(R.id.queue_current_counter);
        cashierCurrentCutOff1 = cashier1.findViewById(R.id.queue_current_cut_off);

        View cashier2 = view.findViewById(R.id.cashier_queue_2);
        cashierCurrentQueueNumber2 = cashier2.findViewById(R.id.queue_current_number);
        cashierCurrentCounter2 = cashier2.findViewById(R.id.queue_current_counter);
        cashierCurrentCutOff2 = cashier2.findViewById(R.id.queue_current_cut_off);

        View cashier3 = view.findViewById(R.id.cashier_queue_3);
        cashierCurrentQueueNumber3 = cashier3.findViewById(R.id.queue_current_number);
        cashierCurrentCounter3 = cashier3.findViewById(R.id.queue_current_counter);
        cashierCurrentCutOff3 = cashier3.findViewById(R.id.queue_current_cut_off);

        View cashierDivider1 = cashier1.findViewById(R.id.divider);
        View cashierDivider2 = cashier2.findViewById(R.id.divider);
        View cashierDivider3 = cashier3.findViewById(R.id.divider);

        cashierDivider1.setBackgroundColor(getResources().getColor(R.color.green, null));
        cashierDivider2.setBackgroundColor(getResources().getColor(R.color.green, null));
        cashierDivider3.setBackgroundColor(getResources().getColor(R.color.green, null));

        viewModel.getCashierCounter1QueueNumber().observe(getViewLifecycleOwner(), queueNumber -> cashierCurrentQueueNumber1.setText(queueNumber));
        viewModel.getCashierCounter1Counter().observe(getViewLifecycleOwner(), counter -> cashierCurrentCounter1.setText(counter));
        viewModel.getCashierCounter1CutOff().observe(getViewLifecycleOwner(), cutOff -> cashierCurrentCutOff1.setText(cutOff));

        viewModel.getCashierCounter2QueueNumber().observe(getViewLifecycleOwner(), queueNumber -> cashierCurrentQueueNumber2.setText(queueNumber));
        viewModel.getCashierCounter2Counter().observe(getViewLifecycleOwner(), counter -> cashierCurrentCounter2.setText(counter));
        viewModel.getCashierCounter2CutOff().observe(getViewLifecycleOwner(), cutOff -> cashierCurrentCutOff2.setText(cutOff));

        viewModel.getCashierCounter3QueueNumber().observe(getViewLifecycleOwner(), queueNumber -> cashierCurrentQueueNumber3.setText(queueNumber));
        viewModel.getCashierCounter3Counter().observe(getViewLifecycleOwner(), counter -> cashierCurrentCounter3.setText(counter));
        viewModel.getCashierCounter3CutOff().observe(getViewLifecycleOwner(), cutOff -> cashierCurrentCutOff3.setText(cutOff));
    }
}