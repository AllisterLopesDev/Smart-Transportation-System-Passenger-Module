package com.example.sts_passenger.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sts_passenger.R;
import com.example.sts_passenger.activities.InstantTicketBooking;

public class TicketFragment extends Fragment {
    Button btn1, btn2, btn3;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ticket, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        btn1 = rootView.findViewById(R.id.button1);
        btn2 = rootView.findViewById(R.id.button2);
        btn3 = rootView.findViewById(R.id.button3);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getContext(), InstantTicketBooking.class);
                startActivity(i);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}