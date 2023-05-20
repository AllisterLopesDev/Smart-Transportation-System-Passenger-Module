package com.example.sts_passenger.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.model.CalendarDate;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class GeneratePass extends Fragment {
    String selectedDate;
    TextView tvDate, tvSource, tvDestination;
    Spinner tvNoOfDays;
    Button createPassBtn;
    private int year, month, day;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_generate_pass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponets(view);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentDate();
            }
        });

    }

    public void initComponets(View view) {
        tvDate = view.findViewById(R.id.textView_pass_start_date);
        tvNoOfDays = view.findViewById(R.id.no_of_days);
        tvSource = view.findViewById(R.id.pass_source_id);
        tvDestination = view.findViewById(R.id.pass_destination_id);
        createPassBtn = view.findViewById(R.id.create_pass_btn);


        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


    }


    public void getCurrentDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        setSelectedDate(year + "-" + (month + 1) + "-" + dayOfMonth);
                        tvDate.setText(selectedDate);
                    }
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }


    // getter && setter


    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }
}