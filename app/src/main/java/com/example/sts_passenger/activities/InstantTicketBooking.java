package com.example.sts_passenger.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.apiservices.response.BusStops;
import com.example.sts_passenger.fragments.AvailableBusScheduleFragment;
import com.example.sts_passenger.fragments.DestinationSearchFrag;
import com.example.sts_passenger.model.Halts;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstantTicketBooking extends AppCompatActivity {

    EditText etDestination;
    TextView tvDatePicker, activityTitle,tvSource;
    Button btnSearchBus;


    // for fused location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Double currentLat;
    private Double currentLong;

    public Double getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(Double currentLat) {
        this.currentLat = currentLat;
    }

    public Double getCurrentLong() {
        return currentLong;
    }

    public void setCurrentLong(Double currentLong) {
        this.currentLong = currentLong;
    }

    // Source halt api call
    List<Halts> sources;

    // for current date and date
    private int year, month, day;
    String paramsDate;

    // source and destination
    String destinationName;
    Integer destinationId;

    String sourceName;
    Integer sourceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_ticket_booking);

        initViews();
    }

    // onStart
    @Override
    protected void onStart() {
        super.onStart();

        // get location
        // fusedLocationInstanceAndRequest();
        // request location permission
        requestLocationPermission();
        // get users current source
        getCurrentUserSource();

        tvDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        etDestination.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DestinationSearchFrag frag = new DestinationSearchFrag();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout_booking, frag);
                transaction.commit();

                // hide views
                hideViewsOnFragTransaction();
            }
        });

        userSelectedDestination();

        btnSearchBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AvailableBusScheduleFragment fragment = new AvailableBusScheduleFragment();

                // Create a Bundle to store the data
                Bundle args = new Bundle();
                args.putInt("sourceId", sourceId);
                Log.i("DATA PASS", "source-id: " + sourceId);
                args.putString("source", sourceName);
                Log.i("DATA PASS", "source-name: " + sourceName);
                args.putInt("destinationId", destinationId);
                args.putString("destination", destinationName);
                args.putString("date", paramsDate);
                Log.i("DATA PASS", "onClick: Date " + paramsDate);

                // set the arguments to the fragment
                fragment.setArguments(args);

                // frag transaction
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout_booking, fragment);
                transaction.commit();

                // hide views
                hideViewsOnFragTransaction();
            }
        });
    }

    // function to initialize views
    public void initViews() {

        // required to get the current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        activityTitle = findViewById(R.id.textView_searchBus_activityTitle);
        tvSource = findViewById(R.id.tv_source_stop);
        etDestination = findViewById(R.id.et_destination_stop);
        tvDatePicker = findViewById(R.id.tv_datePicker);
        btnSearchBus = findViewById(R.id.button_search_buses_available);

//        btnSearchBus.setText(Consts.BUTTON_SEARCH_BUS);

        // Initialize the year, month, and day variables with the current date
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        tvDatePicker.setText(getCurrentDate(year, month, day));

        etDestination.setInputType(InputType.TYPE_NULL);    // disables keyboard input
    }

    // function to handle views on fragment transaction
    public void hideViewsOnFragTransaction() {
        // Hide the views in the current activity
        tvSource.setVisibility(View.GONE);
        etDestination.setVisibility(View.GONE);
        tvDatePicker.setVisibility(View.GONE);
        btnSearchBus.setVisibility(View.GONE);
        activityTitle.setVisibility(View.GONE);
    }


    // function : date picker
    public void showDatePickerDialog(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            // get the month name
            String monthName = "";
            DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
            String[] months = dateFormatSymbols.getMonths();

            if (month >= 0 && month <= 11) {
                monthName = months[month];
            }

            // set the selected date in the TextView
            String selectedDate = dayOfMonth + " " + monthName + ", " + year;

            Log.i("TAG", "onDateSet: selected date " +selectedDate );
            tvDatePicker.setText(selectedDate);
        }
    };

    // function to get current date
    private String getCurrentDate(int year, int month, int day) {
        String monthName = "";
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        String[] months = dateFormatSymbols.getMonths();

        // db pass data
        // Increment the month value by 1
        int actualMonth = month + 1;
        paramsDate = year + "-" + actualMonth + "-" + day;
        Log.i("DATA PASS", "onDateSet: " + paramsDate);

        if (month >= 0 && month <= 11) {
            monthName = months[month];
        }

        return day + " " + monthName + ", " + year;
    }


    // function to get user destination
    public void userSelectedDestination() {
        Intent intent = getIntent();
        destinationId = intent.getIntExtra("destinationId", 0);
        destinationName = intent.getStringExtra("destinationName");
        etDestination.setText(destinationName);
    }


    // instance of FusedLocationProviderClient and request permission to access the user's location
    public void requestLocationPermission() {
        Log.i("TAG", "requestLocationPermission: ");
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    InstantTicketBooking.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Consts.LOCATION_REQUEST_CODE
            );

            Log.i("TAG", "requestLocationPermission: if-block called");
        } else {
            getCurrentLocation();
            Log.i("TAG", "requestLocationPermission: else");
        }
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted
            Log.i("TAG", "getCurrentLocation: permission not granted");
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            setCurrentLat(location.getLatitude());
                            setCurrentLong(location.getLongitude());

                            Log.i("LOCATION", location.getLatitude() + " " + location.getLongitude());
                            // Do something with the current location
                        } else {
                            Toast.makeText(getApplicationContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.i("TAG", "onRequestPermissionsResult: " + requestCode);
        if (requestCode == Consts.LOCATION_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted
            getCurrentLocation();
        } else {
            Toast.makeText(getParent(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void getCurrentUserSource() {
        Call<BusStops> busStopsResponseCall = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getBusStops();
        busStopsResponseCall.enqueue(new Callback<BusStops>() {
            @Override
            public void onResponse(Call<BusStops> call, Response<BusStops> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sources = response.body().getResult();
                    String sourceBusStop = getNearbySourceStop(sources, getCurrentLat(), getCurrentLong());
                    tvSource.setText(sourceBusStop);
                } else {
                    if (response.body() != null) {
                        Log.i("SOURCE", "onResponse : " + response.body().getStatus() + response.body().getSuccess());
                    }
                }
            }

            @Override
            public void onFailure(Call<BusStops> call, Throwable t) {
                Log.i("SOURCE", "onFailure : " + t.getLocalizedMessage() + Arrays.toString(t.getStackTrace()));
            }
        });
    }


    // getNearbySourceStop function
    public String getNearbySourceStop(List<Halts> sourceStops, Double currentLat, Double currentLong) {
        if (sourceStops == null || sourceStops.isEmpty()) {
            return "no bus stops found";
        }

        Location currentLocation = new Location("");
        currentLocation.setLatitude(currentLat);
        currentLocation.setLongitude(currentLong);

        for (int i = 0; i < sourceStops.size(); i++) {
            double lat = Double.parseDouble(sourceStops.get(i).getLatitude());
            double lng = Double.parseDouble(sourceStops.get(i).getLongitude());

            Location busStopLocation = new Location("");
            busStopLocation.setLatitude(lat);
            busStopLocation.setLongitude(lng);

            float distance = currentLocation.distanceTo(busStopLocation);
            Log.i("COMPARE LOCATION", currentLat + " " + currentLong + " " + lat + " " + lng);
            if (distance <= Consts.LOCATION_THRESHOLD) {
                sourceId = sourceStops.get(i).getId();
                sourceName = sourceStops.get(i).getName();
                return sourceStops.get(i).getName();
            }
        }
        return "no nearby bus stops found";
    }
}