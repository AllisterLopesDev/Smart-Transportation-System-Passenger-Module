package com.example.sts_passenger.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sts_passenger.R;
import com.example.sts_passenger.fragments.InstantTicketFragment;
import com.example.sts_passenger.fragments.PreBookingFrag;
import com.example.sts_passenger.fragments.SearchInstantTicketFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TicketActivity extends AppCompatActivity {

    BottomNavigationView ticketNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        ticketNav = findViewById(R.id.ticketBView);


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
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag)
            ft.add(R.id.ticketContainer, fragment);
        else
            ft.replace(R.id.ticketContainer, fragment);
        ft.commit();
    }
}