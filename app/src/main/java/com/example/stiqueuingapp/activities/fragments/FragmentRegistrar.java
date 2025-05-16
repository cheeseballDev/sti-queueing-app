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

public class FragmentRegistrar extends Fragment {

    private HomeViewModel viewModel;

    private TextView registrarCurrentQueueNumber1, registrarCurrentCutOff1, registrarCurrentCounter1,
            registrarCurrentQueueNumber2, registrarCurrentCutOff2, registrarCurrentCounter2,
            registrarCurrentQueueNumber3, registrarCurrentCutOff3, registrarCurrentCounter3;


    public FragmentRegistrar() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registrar, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        View registrar1 = view.findViewById(R.id.registrar_queue_1);
        registrarCurrentQueueNumber1 = registrar1.findViewById(R.id.queue_current_number);
        registrarCurrentCounter1 = registrar1.findViewById(R.id.queue_current_counter);
        registrarCurrentCutOff1 = registrar1.findViewById(R.id.queue_current_cut_off);

        View registrar2 = view.findViewById(R.id.registrar_queue_2);
        registrarCurrentQueueNumber2 = registrar2.findViewById(R.id.queue_current_number);
        registrarCurrentCounter2 = registrar2.findViewById(R.id.queue_current_counter);
        registrarCurrentCutOff2 = registrar2.findViewById(R.id.queue_current_cut_off);

        View registrar3 = view.findViewById(R.id.registrar_queue_3);
        registrarCurrentQueueNumber3 = registrar3.findViewById(R.id.queue_current_number);
        registrarCurrentCounter3 = registrar3.findViewById(R.id.queue_current_counter);
        registrarCurrentCutOff3 = registrar3.findViewById(R.id.queue_current_cut_off);

        View registrarDivider1 = registrar1.findViewById(R.id.divider);
        View registrarDivider2 = registrar2.findViewById(R.id.divider);
        View registrarDivider3 = registrar3.findViewById(R.id.divider);

        registrarDivider1.setBackgroundColor(getResources().getColor(R.color.red, null));
        registrarDivider2.setBackgroundColor(getResources().getColor(R.color.red, null));
        registrarDivider3.setBackgroundColor(getResources().getColor(R.color.red, null));

        viewModel.getRegistrarCounter1QueueNumber().observe(getViewLifecycleOwner(), queueNumber -> registrarCurrentQueueNumber1.setText(queueNumber));
        viewModel.getRegistrarCounter1Counter().observe(getViewLifecycleOwner(), counter -> registrarCurrentCounter1.setText(counter));
        viewModel.getRegistrarCounter1CutOff().observe(getViewLifecycleOwner(), cutOff -> registrarCurrentCutOff1.setText(cutOff));

        viewModel.getRegistrarCounter2QueueNumber().observe(getViewLifecycleOwner(), queueNumber -> registrarCurrentQueueNumber2.setText(queueNumber));
        viewModel.getRegistrarCounter2Counter().observe(getViewLifecycleOwner(), counter -> registrarCurrentCounter2.setText(counter));
        viewModel.getRegistrarCounter2CutOff().observe(getViewLifecycleOwner(), cutOff -> registrarCurrentCutOff2.setText(cutOff));

        viewModel.getRegistrarCounter3QueueNumber().observe(getViewLifecycleOwner(), queueNumber -> registrarCurrentQueueNumber3.setText(queueNumber));
        viewModel.getRegistrarCounter3Counter().observe(getViewLifecycleOwner(), counter -> registrarCurrentCounter3.setText(counter));
        viewModel.getRegistrarCounter3CutOff().observe(getViewLifecycleOwner(), cutOff -> registrarCurrentCutOff3.setText(cutOff));
    }
}