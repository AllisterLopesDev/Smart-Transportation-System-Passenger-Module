package com.example.sts_passenger.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.adapters.ValidTicketAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.TicketDetailsResponse;
import com.example.sts_passenger.model.Session;
import com.example.sts_passenger.model.result.TicketBooking;
import com.example.sts_passenger.sharedpref.SharedPrefManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements LocationListener{

    RecyclerView recyclerView;
    List<TicketBooking> bookingList;

    // SharedPrefManager
    SharedPrefManager sharedPrefManager;
    private Session savedSession;


    private MapView map;
    IMapController mapController;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_home);
        map = findViewById(R.id.map);
        // init sharedPrefManager for passenger session
        setSharedPrefManager();

        recyclerView = findViewById(R.id.recycleView_ticketList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        getTicketDetails();






        map.setTileSource(TileSourceFactory.MAPNIK); // render
        map.setBuiltInZoomControls(true); // zoomable
        GeoPoint startPoint = new GeoPoint(15.18365, 74.09815);
        mapController = map.getController();
        mapController.setZoom(18.0);
        mapController.setCenter(startPoint);

        ArrayList<OverlayItem> items = new ArrayList<>();
        OverlayItem home = new OverlayItem("point A", " A ", new GeoPoint(15.18365, 74.09815));
        Drawable m = home.getMarker(0);
        items.add(home);
        items.add(new OverlayItem("Resto", "chez babar", new GeoPoint(15.30974, 74.00210)));

        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getApplicationContext(),
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

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);






    }


    /* --------------------------------- location ---------------------------------*/

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates((LocationListener) this);
        map.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        }

        map.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        GeoPoint currentLocation = new GeoPoint(latitude, longitude);
        mapController.setCenter(currentLocation);
        mapController.setZoom(18.0);

        OverlayItem currentLocationItem = new OverlayItem("Current Location", "You are here", currentLocation);
        Drawable marker = getResources().getDrawable(R.drawable.home);
        currentLocationItem.setMarker(marker);

        ArrayList<OverlayItem> items = new ArrayList<>();
        items.add(currentLocationItem);

        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<>(
                getApplicationContext(), items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
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
        map.invalidate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }













    // SharedPrefManager function
    public void setSharedPrefManager() {
        sharedPrefManager = new SharedPrefManager(this);
        savedSession = sharedPrefManager.getSavedSessionOnLogin();
    }

    private void getTicketDetails() {
        int passengerId = savedSession.getPassenger().getPassengerId();
        Log.i("TAG", "getTicketDetails: passenger-id " + passengerId);
        Call<TicketDetailsResponse> call = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getAllTicketDetails(savedSession.getPassenger().getPassengerId());
        call.enqueue(new Callback<TicketDetailsResponse>() {
            @Override
            public void onResponse(Call<TicketDetailsResponse> call, Response<TicketDetailsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getStatus() == 200) {
                        bookingList = response.body().getTicketBookingList();

                        // filter booked tickets
                        List<TicketBooking> filterBookedTickets = new ArrayList<>();
                        for (TicketBooking bookedTicket : bookingList) {
                            if (bookedTicket.getTicket().getStatus().equals("Booked")) {
                                filterBookedTickets.add(bookedTicket);
                            }
                        }

                        // add filtered list to recyclerview
                        recyclerView.setAdapter(new ValidTicketAdapter(filterBookedTickets, Home.this));
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketDetailsResponse> call, Throwable t) {

            }
        });
    }
}
