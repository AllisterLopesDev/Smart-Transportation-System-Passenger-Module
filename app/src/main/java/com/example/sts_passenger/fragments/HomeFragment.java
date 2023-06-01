package com.example.sts_passenger.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.adapters.ValidTicketAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.LiveLocationResponse;
import com.example.sts_passenger.model.LiveLocation;
import com.example.sts_passenger.apiservices.response.TicketDetailsResponse;
import com.example.sts_passenger.model.Session;
import com.example.sts_passenger.model.result.TicketBooking;
import com.example.sts_passenger.model.result.TicketResult;
import com.example.sts_passenger.sharedpref.SharedPrefManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//public class HomeFragment extends Fragment implements LocationListener {
public class HomeFragment extends Fragment{
// get current location of device
private static final int REQUEST_LOCATION_PERMISSION = 1;
    private LocationManager locationManager;
    private MapView map;

    IMapController mapController;

    RecyclerView recyclerView;
    List<TicketResult> ticketResultList;

    // SharedPrefManager
    SharedPrefManager sharedPrefManager;
    private Session savedSession;


    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // init sharedPrefManager for passenger session
        setSharedPrefManager();
        getTicketDetails();

        recyclerView = rootView.findViewById(R.id.recycleView_ticketList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));

        // Initialize location manager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            // Permission is granted, get current location
            getCurrentLocation();
        }


        map = rootView.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK); // render
        map.setBuiltInZoomControls(true); // zoomable
        GeoPoint startPoint = new GeoPoint(15.321083, 74.021654);
        mapController = map.getController();
        mapController.setZoom(18.0);
        mapController.setCenter(startPoint);
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                getBusLiveLoc();
                handler.postDelayed(this, 10000); // Call the function every 30 seconds
            }
        };





//        getBusLiveLoc();
        ArrayList<OverlayItem> items = new ArrayList<>();
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getContext(),
                items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return false;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }
        });

        mOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(mOverlay);
       handler.post(runnable);
        return rootView;

}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable); // Remove the callback when the view is destroyed
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
            }
        }
    }

    /* =============================== get location updates from server =========================*/

    public void getBusLiveLoc() {
        Call<LiveLocationResponse> liveLocationResponseCall = Client.getInstance(Consts.BASE_URL_LOCATION).getRoute().getBusLiveLocation();
        liveLocationResponseCall.enqueue(new Callback<LiveLocationResponse>() {
            @Override
            public void onResponse(Call<LiveLocationResponse> call, Response<LiveLocationResponse> response) {
                if (response.isSuccessful()) {
                    LiveLocationResponse liveLocationResponse = response.body();
                    List<LiveLocation> liveLocations = liveLocationResponse.getLive_locations();
                    Log.i("TAG", "onResponse: success");

                    Drawable markerDrawable = getResources().getDrawable(R.drawable.bus_pointer); // Replace with your marker drawable

                    ArrayList<Marker> markers = new ArrayList<>();
                    for (LiveLocation liveLocation : liveLocations) {
                        double latitude = liveLocation.getLatitude();
                        double longitude = liveLocation.getLongitude();
                        GeoPoint point = new GeoPoint(latitude, longitude);
                        Marker marker = new Marker(map);
                        marker.setPosition(point);
                        marker.setIcon(markerDrawable);
                        markers.add(marker);
                    }

                    for (Marker marker : markers) {
                        map.getOverlays().add(marker); // Add the markers to the map's OverlayManager
                    }

                    map.invalidate(); // Refresh the map view
                }
            }

            @Override
            public void onFailure(Call<LiveLocationResponse> call, Throwable t) {
                // Handle failure
                Log.i("TAG", "onFailure:  erroor  "+t);
            }
        });
    }








    // SharedPrefManager function
    public void setSharedPrefManager() {
        sharedPrefManager = new SharedPrefManager(requireContext());
        savedSession = sharedPrefManager.getSavedSessionOnLogin();
    }

    private void getTicketDetails() {
        int passengerId = savedSession.getPassenger().getPassengerId();
        Log.i("TAG", "getTicketDetails: passenger-id " + passengerId);
        Call<TicketDetailsResponse> call = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getCurrentTicket(savedSession.getPassenger().getPassengerId());
        call.enqueue(new Callback<TicketDetailsResponse>() {
            @Override
            public void onResponse(Call<TicketDetailsResponse> call, Response<TicketDetailsResponse> response) {
                if (response.isSuccessful()){
                    if (response.body()  != null && response.body().getStatus() == 200){
                        ticketResultList = response.body().getTicketResultList();

//                         filter booked tickets
                        List<TicketResult> filterBookedTickets = new ArrayList<>();
                        for (TicketResult bookedTicket : ticketResultList) {
                            if (bookedTicket.getTicket().getStatus().equals("Booked")) {
                                filterBookedTickets.add(bookedTicket);
                            }
                        }

                        // add filtered list to recyclerview
                        recyclerView.setAdapter(new ValidTicketAdapter(ticketResultList, getContext()));
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketDetailsResponse> call, Throwable t) {

            }
        });
    }

    // Get the current location of the device
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    // Handle the updated location here
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    // Do something with latitude and longitude
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {}

                @Override
                public void onProviderEnabled(@NonNull String provider) {}

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}
            });
        }
    }

}






//package com.example.sts_passenger.fragments;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.graphics.drawable.Drawable;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.fragment.app.Fragment;
//import androidx.preference.PreferenceManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.sts_passenger.Consts;
//import com.example.sts_passenger.R;
//import com.example.sts_passenger.adapters.ValidTicketAdapter;
//import com.example.sts_passenger.apiservices.Client;
//import com.example.sts_passenger.apiservices.response.LiveLocationResponse;
//import com.example.sts_passenger.model.LiveLocation;
//import com.example.sts_passenger.apiservices.response.TicketDetailsResponse;
//import com.example.sts_passenger.model.Session;
//import com.example.sts_passenger.model.result.TicketBooking;
//import com.example.sts_passenger.sharedpref.SharedPrefManager;
//
//import org.osmdroid.api.IMapController;
//import org.osmdroid.config.Configuration;
//import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
//import org.osmdroid.util.GeoPoint;
//import org.osmdroid.views.MapView;
//import org.osmdroid.views.overlay.ItemizedIconOverlay;
//import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
//import org.osmdroid.views.overlay.OverlayItem;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class HomeFragment extends Fragment implements LocationListener {
//
//    List<LiveLocation> liveLocations;
//
//
//    private MapView map;
//    IMapController mapController;
//    private LocationManager locationManager;
//
//    RecyclerView recyclerView;
//    List<TicketBooking> bookingList;
//
//    // SharedPrefManager
//    SharedPrefManager sharedPrefManager;
//    private Session savedSession;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
//
//
//        // init sharedPrefManager for passenger session
//        setSharedPrefManager();
//        getTicketDetails();
//
//
//        recyclerView = rootView.findViewById(R.id.recycleView_ticketList);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
//        getTicketDetails();
//
//        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
//
//        map = rootView.findViewById(R.id.map);
//
//        map.setTileSource(TileSourceFactory.MAPNIK); // render
//        map.setBuiltInZoomControls(true); // zoomable
////        GeoPoint startPoint = new GeoPoint(15.18365, 74.09815);
//        mapController = map.getController();
//        mapController.setZoom(18.0);
////        mapController.setCenter(startPoint);
//
//        ArrayList<OverlayItem> items = new ArrayList<>();
////        OverlayItem home = new OverlayItem("point A", " A ", new GeoPoint(15.18365, 74.09815));
////        Drawable m = home.getMarker(0);
////        items.add(home);
////        items.add(new OverlayItem("Resto", "chez babar", new GeoPoint(15.30974, 74.00210)));
//
//        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getContext(),
//                items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
//            @Override
//            public boolean onItemSingleTapUp(int index, OverlayItem item) {
//                return false;
//            }
//
//            @Override
//            public boolean onItemLongPress(int index, OverlayItem item) {
//                return false;
//            }
//        });
//
//        mOverlay.setFocusItemsOnTap(true);
//        map.getOverlays().add(mOverlay);
//
//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//
//        return rootView;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        locationManager.removeUpdates((android.location.LocationListener) this);
//        map.onPause();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//        } else {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    100);
//        }
//        map.onResume();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
////        getBusLiveLoc();
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//
//        GeoPoint currentLocation = new GeoPoint(latitude, longitude);
//        mapController.setCenter(currentLocation);
//        mapController.setZoom(18.0);
//
//        OverlayItem currentLocationItem = new OverlayItem("Current Location", "You are here", currentLocation);
//        Drawable marker = getResources().getDrawable(R.drawable.home);
//        currentLocationItem.setMarker(marker);
//
//        ArrayList<OverlayItem> items = new ArrayList<>();
//        items.add(currentLocationItem);
//
//        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<>(
//                getContext(), items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
//            @Override
//            public boolean onItemSingleTapUp(int index, OverlayItem item) {
//                return false;
//            }
//
//            @Override
//            public boolean onItemLongPress(int index, OverlayItem item) {
//                return false;
//            }
//        });
//
//        mOverlay.setFocusItemsOnTap(true);
//        map.getOverlays().add(mOverlay);
//        map.invalidate();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 100) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//            }
//        }
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//    }
//
//
//
///*====================  ticket module ================================================*/
//
//    // SharedPrefManager function
//    public void setSharedPrefManager() {
//        sharedPrefManager = new SharedPrefManager(requireContext());
//        savedSession = sharedPrefManager.getSavedSessionOnLogin();
//    }
//
//    private void getTicketDetails() {
//        int passengerId = savedSession.getPassenger().getPassengerId();
//        Log.i("TAG", "getTicketDetails: passenger-id " + passengerId);
//        Call<TicketDetailsResponse> call = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getAllTicketDetails(savedSession.getPassenger().getPassengerId());
//        call.enqueue(new Callback<TicketDetailsResponse>() {
//            @Override
//            public void onResponse(Call<TicketDetailsResponse> call, Response<TicketDetailsResponse> response) {
//                if (response.isSuccessful()){
//                    if (response.body()  != null && response.body().getStatus() == 200){
//                        bookingList = response.body().getTicketBookingList();
//
//                        // filter booked tickets
//                        List<TicketBooking> filterBookedTickets = new ArrayList<>();
//                        for (TicketBooking bookedTicket : bookingList) {
//                            if (bookedTicket.getTicket().getStatus().equals("Booked")) {
//                                filterBookedTickets.add(bookedTicket);
//                            }
//                        }
//
//                        // add filtered list to recyclerview
//                        recyclerView.setAdapter(new ValidTicketAdapter(filterBookedTickets, getContext()));
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TicketDetailsResponse> call, Throwable t) {
//
//            }
//        });
//    }
//
//
//    /* =============================== get location updates from server =========================*/
//
//
//public void getBusLiveLoc(){
//    Call<LiveLocationResponse> liveLocationResponseCall = Client.getInstance(Consts.BASE_URL_LOCATION).getRoute().getBusLiveLocation();
//    liveLocationResponseCall.enqueue(new Callback<LiveLocationResponse>() {
//        @Override
//        public void onResponse(Call<LiveLocationResponse> call, Response<LiveLocationResponse> response) {
//            if (response.isSuccessful()){
//                if (response.body() != null && response.body().getStatus() == 200){
//                    liveLocations = (List<LiveLocation>) response.body().getLiveLocation();
//                }
//            }
//
//        }
//
//        @Override
//        public void onFailure(Call<LiveLocationResponse> call, Throwable t) {
//
//        }
//    });
//}
//
//}