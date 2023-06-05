package com.example.sts_passenger.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.BusStops;
import com.example.sts_passenger.model.CalendarDate;
import com.example.sts_passenger.model.CurrentLocation;
import com.example.sts_passenger.model.Halts;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchInstantTicketFragment extends Fragment {


    // TextViews
    TextView tvSource, tvDestination, tvShowCurrentDate, tvPassengerCount;

    // ButtonViews
    AppCompatButton btnSearchBusSchedules;

    // ImageButton views for counter
    ImageButton imgBtnIncrementPassengerCount, imgBtnDecrementPassengerCount;
    // passenger counter
    Integer passengerCounter = 0;

    // For location
    private FusedLocationProviderClient fusedLocationProviderClient;
    CurrentLocation currentLocation = new CurrentLocation();

    // List of Halts for source bus stops
    List<Halts> sourceBusStops;
    Halts sourceBusStop;
    Halts destinationBusStop;

    // CalenderDate instance
    private int year, month, day;
    CalendarDate currentDate;

    // to store current source-stop for check
    private Halts halts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_instant_ticket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize views
        initViews(view);

        // get current bus stop of user
        getCurrentSourceBusStop();
    }

    @Override
    public void onStart() {
        super.onStart();

        // request location permission
        requestLocationPermission();


        tvDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // instance of search ticket frag
                DestinationBusStopsFragment fragment = new DestinationBusStopsFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();  // call getParentFragmentManager when calling fragment from another fragment
                transaction.replace(R.id.ticketContainer, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // set destination data
        setDestinationData();

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
                args.putString("currentDate", currentDate.getDate());
                args.putInt("passengerCount", passengerCounter);
                Log.i("TAG", "onClick: " + "source-id " + sourceBusStop.getId() + " destination-id " + destinationBusStop.getId() + " current-date " + currentDate.getDate() + " passenger-count " + passengerCounter);

                fragment.setArguments(args);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.ticketContainer, fragment);
                transaction.commit();
            }
        });

    }


    // init views
    private void initViews(View view) {
        // fused location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // text views
        tvSource = view.findViewById(R.id.textView_auto_source_stop);
        tvDestination = view.findViewById(R.id.textView_search_destination_stop);
        tvShowCurrentDate = view.findViewById(R.id.textView_show_current_date);
        tvPassengerCount = view.findViewById(R.id.textView_passenger_counter);

        // button view
        btnSearchBusSchedules = view.findViewById(R.id.button_search_buses_available);

        // Image button view
        imgBtnIncrementPassengerCount = view.findViewById(R.id.imgBtn_increment_passenger_counter);
        imgBtnDecrementPassengerCount = view.findViewById(R.id.imgBtn_decrement_passenger_counter);

        // halt instance
        halts = new Halts();

        sourceBusStop = new Halts();

        currentDate = new CalendarDate();

        // init the year, month and day variables
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        // set textView with date
        tvShowCurrentDate.setText(currentDate(year, month, day));

    }

    // instance of fused location provider client and request permission to access the user location
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, Consts.LOCATION_REQUEST_CODE
            );
            // request permission
        } else {
            // get current location
            getCurrentLocation();
        }
    }

    // function to get current location of the user
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted
            Log.i("TAG", "getCurrentLocation: permission not granted");
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation.setLatitude(location.getLatitude());
                            currentLocation.setLongitude(location.getLongitude());

                            Log.i("LOCATION", location.getLatitude() + " " + location.getLongitude());
                            // Do something with the current location
                        } else {
                            Toast.makeText(getContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // on request permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.i("TAG", "onRequestPermissionsResult: " + requestCode);
        if (requestCode == Consts.LOCATION_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted
            getCurrentLocation();
        } else {
            Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    // function to make api call to get sources for nearby bus stops
    private void getCurrentSourceBusStop() {
            Call<BusStops> busStopsApiCall = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getBusStops();
            busStopsApiCall.enqueue(new Callback<BusStops>() {
                @Override
                public void onResponse(Call<BusStops> call, Response<BusStops> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.i("TAG", "onResponse: API CALL MADE ");
                        sourceBusStops = response.body().getResult();
                        String sourceBusStopString = getNearbyBusStop(sourceBusStops, currentLocation.getLatitude(), currentLocation.getLongitude());
                        sourceBusStop.setId(halts.getId());
                        sourceBusStop.setName(sourceBusStopString);
                        Log.i("TAG", "onResponse: " + sourceBusStop.getId() + " " + sourceBusStop.getName());
                        tvSource.setText(sourceBusStop.getName());
                    }
                }

                @Override
                public void onFailure(Call<BusStops> call, Throwable t) {

                }
            });
    }


    // function to get nearby bus stop for user source
    private String getNearbyBusStop(List<Halts> busStops, Double currentLat, Double currentLong) {
        String noStopsText = "No nearby bus stops found";

        if (busStops == null || busStops.isEmpty()) {
            return noStopsText;
        }

        Location currentLocation = new Location("");

        if (currentLat != null && currentLong != null) {
            currentLocation.setLatitude(currentLat);
            currentLocation.setLongitude(currentLong);
        } else {
            return noStopsText;
        }


        for (int i = 0; i < busStops.size(); i++) {
            double lat = Double.parseDouble(busStops.get(i).getLatitude());
            double lng = Double.parseDouble(busStops.get(i).getLongitude());

            Location busStopLocation = new Location("");
            busStopLocation.setLatitude(lat);
            busStopLocation.setLongitude(lng);

            float distance = currentLocation.distanceTo(busStopLocation);

            if (distance <= Consts.LOCATION_THRESHOLD) {
                // set source bus stop id and name to halts instance
                halts.setId(busStops.get(i).getId());
                String sourceBusStopName = busStops.get(i).getName();
                halts.setName(sourceBusStopName);
                return sourceBusStopName;
            }
        }
        return noStopsText;
    }


    // Destination Bus Stop data from DestinationBusStopFrag
    private void setDestinationData() {
        Bundle data = getArguments();

        destinationBusStop = new Halts();

        if (data != null) {
            destinationBusStop.setId(data.getInt("destinationBusStopId"));
            destinationBusStop.setName(data.getString("destinationBusStopName"));

            Log.i("TAG", "setDestinationData: " + destinationBusStop.getName());
        }
            tvDestination.setText(destinationBusStop.getName());



    }



    // function to get current date
    private String currentDate(int year, int month, int day) {
        String monthName = "";

        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        String[] months = dateFormatSymbols.getMonths();

        if (month >= 0 && month <= 11) {
            monthName = months[month];
        }

        // convert month to actual month
        int actualMonth = month + 1;
        // set month to currentDate instance to pass in query
        currentDate.setDate(year+"-"+actualMonth+"-"+day);

        return day + " " + monthName + ", " + year;
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


}