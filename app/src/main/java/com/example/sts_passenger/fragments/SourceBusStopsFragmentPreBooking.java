package com.example.sts_passenger.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.adapters.SourceAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.BusStops;
import com.example.sts_passenger.model.Halts;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SourceBusStopsFragmentPreBooking extends Fragment {

    SharedPreferences sharedPreferencesPreBooking;
    SharedPreferences.Editor editor;


    RecyclerView recyclerViewSourceBusStops;

    // List of Halts for destination bus stops
    List<Halts> sourceBusStopsList;

    // onBusStop item click listener
    SourceAdapter.OnSourceItemClickListener clickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_source_bus_stops, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        showBusStopsForSourceStop();
    }

    @Override
    public void onStart() {
        super.onStart();

        // init click listener
        clickListener = new SourceAdapter.OnSourceItemClickListener() {
            @Override
            public void onItemClick(Integer sourceBusStopId, String sourceBusStopName) {

            }
        };
    }

    private void initViews(View view) {
        sharedPreferencesPreBooking = getContext().getSharedPreferences("prebooking", Context.MODE_PRIVATE);
        recyclerViewSourceBusStops = view.findViewById(R.id.recyclerView_source_bus_stop_list_prebooking);
        recyclerViewSourceBusStops.setHasFixedSize(true);
        recyclerViewSourceBusStops.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /*----------------------------- source -----------------------------*/

    private void showBusStopsForSourceStop() {
        Call<BusStops> busStopsApiCall = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getBusStops();
        busStopsApiCall.enqueue(new Callback<BusStops>() {
            @Override
            public void onResponse(Call<BusStops> call, Response<BusStops> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("TAG", "onResponse: response is successful" + response.body().getResult());
                    sourceBusStopsList = response.body().getResult();
                    recyclerViewSourceBusStops.setAdapter(new SourceAdapter(sourceBusStopsList, getContext(), new SourceAdapter.OnSourceItemClickListener() {
                        @Override
                        public void onItemClick(Integer sourceBusStopId, String sourceBusStopName) {
//                            setSourceAndStartSearchInstantTicketFrag(sourceBusStopId, sourceBusStopName);
                       storeSourcePrebooking(sourceBusStopId,sourceBusStopName);
                        }
                    }));
                }
            }

            @Override
            public void onFailure(Call<BusStops> call, Throwable t) {

            }
        });
    }

    public void setSourceAndStartSearchInstantTicketFrag(Integer busStopId, String busStopName) {
        // instance of search ticket frag
        PreBookingFrag fragment = new PreBookingFrag();

        // create bundle to store data and pass
        Bundle args = new Bundle();
        args.putInt("sourceBusStopId", busStopId);
        args.putString("sourceBusStopName", busStopName);

        // log data passed
        Log.i("TAG", "setSourceAndStartSearchInstantTicketFrag: " + busStopId + " " + busStopName);
        fragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();  // call getParentFragmentManager when calling fragment from another fragment
        transaction.replace(R.id.ticketContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void storeSourcePrebooking(int id, String name) {
        editor = sharedPreferencesPreBooking.edit();
        editor.putInt("sourceIdPreBooking", id);
        editor.putString("sourceNamePreBooking", name);
        editor.apply();

        PreBookingFrag fragment = new PreBookingFrag();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();  // call getParentFragmentManager when calling fragment from another fragment
        transaction.replace(R.id.ticketContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



}