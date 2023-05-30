package com.example.sts_passenger.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.sts_passenger.R;
import com.example.sts_passenger.fragments.HomeFragment;
import com.example.sts_passenger.fragments.PassFragment;
import com.example.sts_passenger.fragments.ProfileFragment;
import com.example.sts_passenger.fragments.ScheduleFragment;
import com.example.sts_passenger.fragments.TicketFragment;
import com.example.sts_passenger.model.Session;
import com.example.sts_passenger.sharedpref.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PassengerHomePage extends AppCompatActivity {

    BottomNavigationView bnView;

    // SharedPrefManager
    SharedPrefManager sharedPrefManager;
    private Session savedSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_home_page);

        // initialize views here
        initViews();


        bnView = findViewById(R.id.bnView);

        bnView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id =  item.getItemId();

                if (id == R.id.home){
                    loadFrag(new HomeFragment(),false);
                } else if (id == R.id.ticket) {
                    loadFrag(new TicketFragment(),false);
                } else if (id == R.id.schedule) {
                    loadFrag(new ScheduleFragment(),false);
                } else if (id == R.id.issues) {
                    loadFrag(new PassFragment(),false);
                }else {
                    loadFrag(new ProfileFragment(),true);
                }

                return true;
            }
        });
        bnView.setSelectedItemId(R.id.profile);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // init sharedPrefManager to get saved-session of passenger
        setSharedPrefManager();

        String sessionToken = savedSession.getToken();
        Log.i("TAG", "Passenger Home Page -> onStart: user-session-token" + sessionToken);
    }

    // SharedPrefManager function
    public void setSharedPrefManager() {
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        savedSession = sharedPrefManager.getSavedSessionOnLogin();
    }

    public  void loadFrag(Fragment fragment, Boolean flag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag)
            ft.add(R.id.container, fragment);
        else
            ft.replace(R.id.container, fragment);
        ft.commit();
    }


    public  void loadActivity() {
        Intent i = new Intent(getApplicationContext(), TicketActivity.class);
        startActivity(i);

    }


    // init views
    private void initViews() {

    }

}