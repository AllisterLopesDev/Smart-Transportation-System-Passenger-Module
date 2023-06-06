package com.example.sts_passenger.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sts_passenger.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TicketFragment extends Fragment {
//    Button btnInstantTicket, btnPreTicket;
BottomNavigationView ticketNav;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_ticket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        // initialize views
        initViews(rootView);
    }

    // function to init views
    private void initViews(View view) {
//        btnInstantTicket = view.findViewById(R.id.btn_instant_ticket);
//        btnPreTicket = view.findViewById(R.id.btn_pre_ticket_booking);
        ticketNav = view.findViewById(R.id.ticketBView);
    }


    // on fragment start
    @Override
    public void onStart() {
        super.onStart();





        ticketNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id =  item.getItemId();

                if (id == R.id.instant_ticket){
                    loadFrag(new SearchInstantTicketFragment(), false);
                } else if (id == R.id.pre_booking) {
                    loadFrag(new PreBookingFrag(),false);
                }

                return true;
            }
        });

        ticketNav.setSelectedItemId(R.id.instant_ticket);
    }

    public  void loadFrag(Fragment fragment, Boolean flag){
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag)
            ft.add(R.id.ticketContainer, fragment);
        else
            ft.replace(R.id.ticketContainer, fragment);
        ft.commit();
    }





        // on instant button clicked
//        btnInstantTicket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // instance of search ticket frag
//                SearchInstantTicketFragment fragment = new SearchInstantTicketFragment();
//                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                transaction.replace(R.id.frameLayout_booking_container, fragment);
//                transaction.commit();
//
//                // hide views
//                hideViewsOnFragTransaction();
//            }
//        });
//
//
//        // Ticket pre-booking
//
//        btnPreTicket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // instance of search ticket frag
//                SearchInstantTicketFragment fragment = new SearchInstantTicketFragment();
//                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                transaction.replace(R.id.frameLayout_booking_container, fragment);
//                transaction.commit();
//
//                // hide views
//                hideViewsOnFragTransaction();
//            }
//        });


//    }




    // function to hide views on fragment call
    private void hideViewsOnFragTransaction() {
//        btnInstantTicket.setVisibility(View.GONE);
//        btnPreTicket.setVisibility(View.GONE);
    }

}