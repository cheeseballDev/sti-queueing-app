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

public class FragmentAdmission extends Fragment {

    private HomeViewModel viewModel;

    private TextView admissionCurrentQueueNumber1, admissionCurrentCutOff1, admissionCurrentCounter1,
            admissionCurrentQueueNumber2, admissionCurrentCutOff2, admissionCurrentCounter2,
            admissionCurrentQueueNumber3, admissionCurrentCutOff3, admissionCurrentCounter3;

    public FragmentAdmission() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admission, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        View admission1 = view.findViewById(R.id.admission_queue_1);
        admissionCurrentQueueNumber1 = admission1.findViewById(R.id.queue_current_number);
        admissionCurrentCounter1 = admission1.findViewById(R.id.queue_current_counter);
        admissionCurrentCutOff1 = admission1.findViewById(R.id.queue_current_cut_off);

        View admission2 = view.findViewById(R.id.admission_queue_2);
        admissionCurrentQueueNumber2 = admission2.findViewById(R.id.queue_current_number);
        admissionCurrentCounter2 = admission2.findViewById(R.id.queue_current_counter);
        admissionCurrentCutOff2 = admission2.findViewById(R.id.queue_current_cut_off);

        View admission3 = view.findViewById(R.id.admission_queue_3);
        admissionCurrentQueueNumber3 = admission3.findViewById(R.id.queue_current_number);
        admissionCurrentCounter3 = admission3.findViewById(R.id.queue_current_counter);
        admissionCurrentCutOff3 = admission3.findViewById(R.id.queue_current_cut_off);

        View admissionDivider1 = admission1.findViewById(R.id.divider);
        View admissionDivider2 = admission2.findViewById(R.id.divider);
        View admissionDivider3 = admission3.findViewById(R.id.divider);

        admissionDivider1.setBackgroundColor(getResources().getColor(R.color.blue, null));
        admissionDivider2.setBackgroundColor(getResources().getColor(R.color.blue, null));
        admissionDivider3.setBackgroundColor(getResources().getColor(R.color.blue, null));

        viewModel.getAdmissionCounter1QueueNumber().observe(getViewLifecycleOwner(), queueNumber -> admissionCurrentQueueNumber1.setText(queueNumber));
        viewModel.getAdmissionCounter1Counter().observe(getViewLifecycleOwner(), counter -> admissionCurrentCounter1.setText(counter));
        viewModel.getAdmissionCounter1CutOff().observe(getViewLifecycleOwner(), cutOff -> admissionCurrentCutOff1.setText(cutOff));

        viewModel.getAdmissionCounter2QueueNumber().observe(getViewLifecycleOwner(), queueNumber -> admissionCurrentQueueNumber2.setText(queueNumber));
        viewModel.getAdmissionCounter2Counter().observe(getViewLifecycleOwner(), counter -> admissionCurrentCounter2.setText(counter));
        viewModel.getAdmissionCounter2CutOff().observe(getViewLifecycleOwner(), cutOff -> admissionCurrentCutOff2.setText(cutOff));

        viewModel.getAdmissionCounter3QueueNumber().observe(getViewLifecycleOwner(), queueNumber -> admissionCurrentQueueNumber3.setText(queueNumber));
        viewModel.getAdmissionCounter3Counter().observe(getViewLifecycleOwner(), counter -> admissionCurrentCounter3.setText(counter));
        viewModel.getAdmissionCounter3CutOff().observe(getViewLifecycleOwner(), cutOff -> admissionCurrentCutOff3.setText(cutOff));
    }
}