package com.example.sts_passenger.fragments;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sts_passenger.Client;
import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.ticketbooking.BusStops;
import com.example.sts_passenger.ticketbooking.ResponseBusStrand;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketFragment extends Fragment {

    private FusedLocationProviderClient fusedLocationProviderClient;
    TextView tvId, tvLat, tvLong, tvName;
    Button btn1, btn2, btn3;
    String busStandName;

    List<BusStops> busStopsList;

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

        fusedLocationInstanceAndRequest();

        // Request location permission
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Consts.REQUEST_CODE);
        } else {
            // Permission already granted
            // Do your location-related task here
            getCurrentLocation();
        }

//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getBusStands();
//            }
//        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusAvailableFragment fragment = new BusAvailableFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
    }

    // instance of FusedLocationProviderClient and request permission to access the user's location
    public void fusedLocationInstanceAndRequest() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }


    private void navigateToInstantBookingFragment(String sourceBusStop) {
        TicketInstantBookingFragment instantBookingFragment = new TicketInstantBookingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("standName", sourceBusStop);
        Log.i(TAG, "navigateToInstantBookingFragment: " + sourceBusStop);
        instantBookingFragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container, instantBookingFragment)
                .commit();
    }

    /*
     * //-------------------- get bus stands from database -----------------------
     * */
    public void getBusStands() {
        Call<ResponseBusStrand> call = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getBusStands();
        call.enqueue(new Callback<ResponseBusStrand>() {
            @Override
            public void onResponse(Call<ResponseBusStrand> call, Response<ResponseBusStrand> response) {
                // Handle the successful response here
                if (response.isSuccessful() && response.body() != null) {
                    busStopsList = response.body().getResult();
                    String sourceBusStop = getNearbySourceStop(busStopsList, getCurrentLat(), getCurrentLong());
                    tvName.setText(sourceBusStop);
                    navigateToInstantBookingFragment(sourceBusStop);
                }

            }

            @Override
            public void onFailure(Call<ResponseBusStrand> call, Throwable t) {
                // Handle the failure here
            }
        });
    }

    /*
     * // ------------------------ compare current long n lat with getBusStand() long and lat-----------
     * */
    public String getNearbySourceStop(List<BusStops> sourceStops, Double currentLat, Double currentLong) {
        for (int i = 0; i < sourceStops.size(); i++) {
            double lat = Double.parseDouble(sourceStops.get(i).getLat());
            double lng = Double.parseDouble(sourceStops.get(i).getLongitude());
            double distance = calculateDistance(currentLat, currentLong, lat, lng);
            Log.i("COMPARE LOCATION", currentLat + " " + currentLong + " " + lat + " " + lng);
            if (distance <= Consts.LOCATION_THRESHOLD) {
                return sourceStops.get(i).getName();
            }
        }

//        for (int i=0; i<sourceStops.size(); i++) {
//            if (sourceStops.get(i).getLongitude().equals() && sourceStops.get(i).getLat().equals()) {
//                Log.i(TAG, "getSourceCheckPoint: " + sourceStops.get(i).getName());
//                return sourceStops.get(i).getName();
//            }
//          }
        return null;
    }

    // calc location within radius
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }


    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            setCurrentLat(location.getLatitude());
                            setCurrentLong(location.getLongitude());

                            Log.i("LOCATION", location.getLatitude() + " " + location.getLongitude());
                            // Do something with the current location
                        } else {
                            Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted
            getCurrentLocation();
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}