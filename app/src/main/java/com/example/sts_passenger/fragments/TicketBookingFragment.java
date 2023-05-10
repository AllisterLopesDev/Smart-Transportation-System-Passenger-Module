package com.example.sts_passenger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sts_passenger.R;

public class TicketBookingFragment extends Fragment {

    private EditText source;
    private EditText destination;
    private Spinner dateSpinner;

    public TicketBookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ticket_booking, container, false);

        // Find the views by their IDs
        source = rootView.findViewById(R.id.source);
        destination = rootView.findViewById(R.id.destination);
        dateSpinner = rootView.findViewById(R.id.date_spinner);

        // Populate the date spinner with data
//        ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter.createFromResource(
//                getContext(), R.array.dates, android.R.layout.simple_spinner_item);
//        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dateSpinner.setAdapter(dateAdapter);



        return rootView;
    }

    // Methods to get user input from the UI
    public String getSource() {
        return source.getText().toString().trim();
    }

    public String getDestiantion() {
        return destination.getText().toString().trim();
    }

    public String getDate() {
        return dateSpinner.getSelectedItem().toString();
    }

}
