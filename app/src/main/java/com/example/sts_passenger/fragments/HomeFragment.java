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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.adapters.CurrentBookedTicketAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.BusStops;
import com.example.sts_passenger.apiservices.response.LiveLocationResponse;
import com.example.sts_passenger.model.Halts;
import com.example.sts_passenger.model.LiveLocation;
import com.example.sts_passenger.apiservices.response.TicketDetailsResponse;
import com.example.sts_passenger.model.Session;
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
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//public class HomeFragment extends Fragment implements LocationListener {
public class HomeFragment extends Fragment{
// get current location of device
private static final int REQUEST_LOCATION_PERMISSION = 1;
    private LocationManager locationManager;
    private MapView map;
    // Flag to check if map is centered to user location
    private boolean isMapCentered = false;

    IMapController mapController;
    List<Halts> busStopsList;

    RecyclerView recyclerView;
    List<TicketResult> ticketResultList;

    // SharedPrefManager
    SharedPrefManager sharedPrefManager;
    private Session savedSession;

    // User Current Live Location instance
    ItemizedOverlayWithFocus<OverlayItem> mOverlay;

    // Greet and Name
    private TextView tvUserName, tvGreetUser;

    // Profile Image display
    private CircleImageView profileImageDisplay;

    // Button
    private TextView tvProceedToBookTicket;

    // LinearLayout showing no bookings view
    LinearLayout linearLayoutNoBookingsWhiteSpace;

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

        // Initialize views
        tvGreetUser = rootView.findViewById(R.id.tv_greet_user);
        tvUserName = rootView.findViewById(R.id.tv_hello_user);
        profileImageDisplay = rootView.findViewById(R.id.circle_imgView_home_profile);

        // TextView
//        tvProceedToBookTicket = rootView.findViewById(R.id.tv_proceed_to_booking);
        // LinearLayout
        linearLayoutNoBookingsWhiteSpace = rootView.findViewById(R.id.linearLayout_blankSpace);

        // init sharedPrefManager for passenger session
        setSharedPrefManager();
        displayProfileImage();
        greetUser();


        // RecyclerView displaying the booked tickets
        recyclerView = rootView.findViewById(R.id.recycleView_ticketList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        getTicketDetails();

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
            getBusStops();
        }


        map = rootView.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK); // render
        // Enable pinch-to-zoom
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(false); // zoomable
        // Display scale bar
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(map);
        map.getOverlays().add(scaleBarOverlay);
        mapController = map.getController();
        mapController.setZoom(18.0);

        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                getBusLiveLoc();
                handler.postDelayed(this, 3000); // Call the function every 30 seconds
            }
        };





//        getBusLiveLoc();
        ArrayList<OverlayItem> items = new ArrayList<>();
        mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getContext(),
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
                    getBusStops();
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

                    // Clear existing markers
                    map.getOverlays().clear();

                    for (LiveLocation liveLocation : liveLocations) {
                        double latitude = liveLocation.getLatitude();
                        double longitude = liveLocation.getLongitude();
                        int busScheduleID = liveLocation.getBusScheduleId();


                        GeoPoint point = new GeoPoint(latitude, longitude);
                        Marker marker = new Marker(map);
                        marker.setPosition(point);
                        marker.setIcon(markerDrawable);
                        marker.setTitle(String.valueOf(busScheduleID));
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



    /* =============================== get bus stops from server =========================*/

    public void getBusStops() {
        Call<BusStops> busStopsCall = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getBusStops();
        busStopsCall.enqueue(new Callback<BusStops>() {
            @Override
            public void onResponse(Call<BusStops> call, Response<BusStops> response) {
                if (response.isSuccessful()) {
                    BusStops busStop = response.body();
                    busStopsList = busStop.getResult();

                    Log.i("TAG", "onResponse: success");

                    Drawable markerDrawable = getResources().getDrawable(R.drawable.location_pointer); // Replace with your marker drawable

                    ArrayList<Marker> markers = new ArrayList<>();
                    for (Halts halts : busStopsList) {
                        double latitude = Double.parseDouble(halts.getLatitude());
                        double longitude = Double.parseDouble(halts.getLongitude());
                        String haltName = halts.getName();

                        GeoPoint point = new GeoPoint(latitude, longitude);
                        Marker marker = new Marker(map);
                        marker.setPosition(point);
                        marker.setIcon(markerDrawable);
                        marker.setTitle(haltName); // Set halt name
                        markers.add(marker);
                    }

                    for (Marker marker : markers) {
                        map.getOverlays().add(marker); // Add the markers to the map's OverlayManager
                    }

                    map.invalidate(); // Refresh the map view
                }
            }

            @Override
            public void onFailure(Call<BusStops> call, Throwable t) {
                // Handle failure
                Log.i("TAG", "onFailure:  error  "+t);
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

                        // filter booked tickets
                        List<TicketResult> filterBookedTickets = new ArrayList<>();
                        for (TicketResult bookedTicket : ticketResultList) {
                            if (bookedTicket.getTicket().getStatus().equals("Booked")) {
                                filterBookedTickets.add(bookedTicket);
                            }
                        }

                        if (filterBookedTickets.isEmpty()) {
                            // Show blank screen with ticket booking button
                            recyclerView.setVisibility(View.GONE);
                            linearLayoutNoBookingsWhiteSpace.setVisibility(View.VISIBLE);
//                            tvProceedToBookTicket.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    openTicketBookingFragment();
//                                    Log.i("TAG", "onClick: book ticket");
//                                }
//                            });
                        } else {
                            // Populate recyclerview with booked tickets
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(new CurrentBookedTicketAdapter(filterBookedTickets));
                            // Hide LinearLayout containing no booking space
                            linearLayoutNoBookingsWhiteSpace.setVisibility(View.GONE);
                        }
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
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    if (!isMapCentered) {
                        // Handle the updated location here
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        // Do something with latitude and longitude

                        // Update the map view with the current location
                        GeoPoint startPoint = new GeoPoint(latitude, longitude);
                        mapController.setCenter(startPoint);

                        // Mark the Flag as true on centering to user's current location
                        isMapCentered = true;
                    }
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

    /*
    * Load profile Image of User on Home page method
    *
    * */
    private void displayProfileImage() {
        // Check if profile image exists
        if (sharedPrefManager.getPassengerPhotoFileName() != null) {
            // Photo file name as stored in sharedpref manager
            String filename = sharedPrefManager.getPassengerPhotoFileName();
            // Construct photo url
            String uri = Consts.BASE_URL_PASSENGER_AUTH + Consts.ENDPOINT_GET_PROFILE_PIC + filename;
            // Add filename to the uri at the end
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.profile);
            // Call the api and display the image
            Glide.with(requireContext())
                    .load(uri)
                    .apply(requestOptions)
                    .into(profileImageDisplay);
        }
    }

    /*
    * Greet User and say his name function
    * */
    private void greetUser() {
        // User first name from the session
        String userFirstName = savedSession.getPassenger().getFirstname();
        // Make name bold using html tag
        String boldFirstName = "<b>" + userFirstName + "</b>";
        // Display Hello message along with user first name
        String displayMessage = "Hello " + boldFirstName + "!";

        tvUserName.setText(Html.fromHtml(displayMessage));

        /*
        * Greet user good morning, afternoon or evening based on time of the day
        * */
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay < 12) {
            String message = Consts.STRING_GOOD_MORNING + ", " + Consts.STRING_WELCOME_BACK;
            tvGreetUser.setText(message);
        } else if (timeOfDay < 17) {
            String message = Consts.STRING_GOOD_AFTERNOON + ", " + Consts.STRING_WELCOME_BACK;
            tvGreetUser.setText(message);
        } else {
            String message = Consts.STRING_GOOD_EVENING + ", " + Consts.STRING_WELCOME_BACK;
            tvGreetUser.setText(message);
        }
    }

    /*
    * function to open instant ticket booking fragment
    * TicketFragment.java
    * */

    private void openTicketBookingFragment(){
        Fragment ticketFragment = new TicketFragment();
        loadFragment(ticketFragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

}