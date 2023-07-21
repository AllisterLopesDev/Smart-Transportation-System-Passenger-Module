package com.example.sts_passenger.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.model.CalendarDate;
import com.example.sts_passenger.model.Halts;
import com.example.sts_passenger.module.SlideshowAdapter;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class PreBookingFrag extends Fragment {

    SharedPreferences sharedPreferencesPreBooking;
    SharedPreferences.Editor editor;
//_prebooking

    String selectedDate;

    // TextViews
    TextView tvSource, tvDestination, tvShowCurrentDate, tvPassengerCount;

    // ButtonViews
    AppCompatButton btnSearchBusSchedules;

    // ImageButton views for counter
    ImageButton imgBtnIncrementPassengerCount, imgBtnDecrementPassengerCount;
    // passenger counter
    Integer passengerCounter = 1;

    // List of Halts for source bus stops
    Halts sourceBusStop;
    Halts destinationBusStop;

    // CalenderDate instance
    private int year, month, dayOfMonth;
    CalendarDate currentDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pre_booking, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize views
        initViews(view);




    }

    @Override
    public void onStart() {
        super.onStart();


        // source
        tvSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // instance of search ticket frag
                SourceBusStopsFragmentPreBooking fragment = new SourceBusStopsFragmentPreBooking();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();  // call getParentFragmentManager when calling fragment from another fragment
                transaction.replace(R.id.ticketContainer, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
//        setSourceData();
getSharedPrefData();

// destination
        tvDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // instance of search ticket frag
                DestinationBusStopsFragmentPreBooking fragment = new DestinationBusStopsFragmentPreBooking();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();  // call getParentFragmentManager when calling fragment from another fragment
                transaction.replace(R.id.ticketContainer, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        // set destination data
//        setDestinationData();


        tvShowCurrentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserSelectedDate();
            }
        });

        // increment passenger count
        imgBtnIncrementPassengerCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int counter = incrementPassengerCounter();
                tvPassengerCount.setText(String.valueOf(incrementPassengerCounter()));
            }
        });

        // decrement passenger count
        imgBtnDecrementPassengerCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int counter = decrementPassengerCounter();
                tvPassengerCount.setText(String.valueOf(decrementPassengerCounter()));
            }
        });


        btnSearchBusSchedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AvailableBusScheduleSearchListFragment fragment = new AvailableBusScheduleSearchListFragment();

                // create bundle to store data and pass
                Bundle args = new Bundle();
                args.putInt("sourceId", sourceBusStop.getId());
                args.putInt("destinationId", destinationBusStop.getId());
                args.putString("currentDate", getSelectedDate());
                args.putInt("passengerCount", passengerCounter);
                Log.i("TAG", "onClick: " + "source-id " + sourceBusStop.getId() + " destination-id " + destinationBusStop.getId() + " current-date " + currentDate.getDate() + " passenger-count " + passengerCounter);

                fragment.setArguments(args);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.ticketContainer, fragment);
                transaction.commit();
                clearRouteSharedPref();
            }
        });

    }
    // init views
    private void initViews(View view) {

        sharedPreferencesPreBooking = getContext().getSharedPreferences("prebooking", Context.MODE_PRIVATE);

        // text views
        tvSource = view.findViewById(R.id.textView_auto_source_stop_prebooking);
        tvDestination = view.findViewById(R.id.textView_search_destination_stop_prebooking);
        tvShowCurrentDate = view.findViewById(R.id.textView_show_current_date_prebooking);
        tvPassengerCount = view.findViewById(R.id.textView_passenger_counter_prebooking);

        tvPassengerCount.setText(passengerCounter.toString());

        // button view
        btnSearchBusSchedules = view.findViewById(R.id.button_search_buses_available_prebooking);

        // Image button view
        imgBtnIncrementPassengerCount = view.findViewById(R.id.imgBtn_increment_passenger_counter_prebooking);
        imgBtnDecrementPassengerCount = view.findViewById(R.id.imgBtn_decrement_passenger_counter_prebooking);

        currentDate = new CalendarDate();

        // init the year, month and day variables
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);;

    }




    // Destination Bus Stop data from DestinationBusStopFrag
//    private void setDestinationData() {
//        Bundle data = getArguments();
//
//        destinationBusStop = new Halts();
//
//        String destination = "";
//
//        if (data != null) {
//            destinationBusStop.setId(data.getInt("destinationBusStopId"));
//            destinationBusStop.setName(data.getString("destinationBusStopName"));
//
//            destination = destinationBusStop.getName();
//
//            Log.i("TAG", "setDestinationData: " + destinationBusStop.getName());
//        } else {
//            destination = "Choose destination";
//        }
//
//        tvDestination.setText(destination);
//    }

    // Source Bus Stop data from sourceBusStopFrag
//    private void setSourceData() {
//        Bundle sourceData = getArguments();
//
//        sourceBusStop = new Halts();
//
//        String source = "";
//
//        if (sourceData != null) {
//            sourceBusStop.setId(sourceData.getInt("sourceBusStopId"));
//            sourceBusStop.setName(sourceData.getString("sourceBusStopName"));
//
//            source = sourceBusStop.getName();
//
//            Log.i("TAG", "setSourceData: " + sourceBusStop.getName());
//        }
//
//        if (sourceData == null){
//            source = "Select source";
//        }
//
//        tvSource.setText(source);
//    }

    private void getSharedPrefData(){
        sourceBusStop = new Halts();
        destinationBusStop = new Halts();
        sourceBusStop.setId(sharedPreferencesPreBooking.getInt("sourceIdPreBooking", 0));
        sourceBusStop.setName(sharedPreferencesPreBooking.getString("sourceNamePreBooking", ""));
        destinationBusStop.setId(sharedPreferencesPreBooking.getInt("destinationIdPreBooking", 0));
        destinationBusStop.setName(sharedPreferencesPreBooking.getString("destinationNamePreBooking", ""));
        if (sourceBusStop.getName().isEmpty()){
            tvSource.setText("Select Source");
        }else{
            tvSource.setText(sourceBusStop.getName());
        }

        if (destinationBusStop.getName().isEmpty()){
            tvDestination.setText("Select destination");
        }else{
            tvDestination.setText(destinationBusStop.getName());
        }

    }



    // passenger counter
    private Integer incrementPassengerCounter() {
        return ++passengerCounter;
    }

    private Integer decrementPassengerCounter() {
        if (passengerCounter > 0) {
            return --passengerCounter;
        }
        return 0;
    }


    // Clear Route SharedPref
    public void clearRouteSharedPref() {
        editor = sharedPreferencesPreBooking.edit();
        editor.clear();
        editor.apply();
    }


    public void getUserSelectedDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        setSelectedDate(year + "-" + (month + 1) + "-" + dayOfMonth);
                        tvShowCurrentDate.setText(""+getSelectedDate());
//                        Log.i("TAG", "onDateSet: start date " +selectedDate);
                    }
                },
                year,
                month,
                dayOfMonth
        );
        datePickerDialog.show();
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }
}