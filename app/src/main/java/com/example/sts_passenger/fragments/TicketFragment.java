package com.example.sts_passenger.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sts_passenger.R;

public class TicketFragment extends Fragment {
    Button btnInstantTicket, btnPreTicket, btnPass;




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

        // initialize views
        initViews(rootView);
    }

    // function to init views
    private void initViews(View view) {
        btnInstantTicket = view.findViewById(R.id.btn_instant_ticket);
        btnPreTicket = view.findViewById(R.id.btn_pre_ticket_booking);
        btnPass = view.findViewById(R.id.btn_generate_pass);
    }


    // on fragment start
    @Override
    public void onStart() {
        super.onStart();

        // on instant button clicked
        btnInstantTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // instance of search ticket frag
                SearchInstantTicketFragment fragment = new SearchInstantTicketFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout_booking_container, fragment);
                transaction.commit();

                // hide views
                hideViewsOnFragTransaction();
            }
        });


    }




    // function to hide views on fragment call
    private void hideViewsOnFragTransaction() {
        btnInstantTicket.setVisibility(View.GONE);
        btnPreTicket.setVisibility(View.GONE);
        btnPass.setVisibility(View.GONE);
    }

}