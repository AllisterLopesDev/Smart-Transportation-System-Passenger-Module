package com.example.sts_passenger.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.activities.InstantTicketBooking;
import com.example.sts_passenger.adapters.DestinationAdapter;
import com.example.sts_passenger.apiservices.response.BusStops;
import com.example.sts_passenger.model.Halts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DestinationSearchFrag extends Fragment {

    RecyclerView recyclerViewDestinations;
    SearchView searchViewDestination;
    List<Halts> destinations;

    DestinationAdapter.OnItemClickListener itemClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_destination_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        getDestinations();

        itemClickListener = new DestinationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Integer destinationId, String destinationName) {
            }
        };

        searchViewDestination.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterDestinations(newText);
                return false;
            }
        });
    }

    public void initViews(View view) {
        searchViewDestination = view.findViewById(R.id.searchView_destination);

        recyclerViewDestinations = view.findViewById(R.id.recyclerView_destinations);
        recyclerViewDestinations.setHasFixedSize(true);
        recyclerViewDestinations.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    public void getDestinations() {
        Call<BusStops> busStopsResponseCall = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getBusStops();
        busStopsResponseCall.enqueue(new Callback<BusStops>() {
            @Override
            public void onResponse(Call<BusStops> call, Response<BusStops> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        destinations = response.body().getResult();
                        recyclerViewDestinations.setAdapter(new DestinationAdapter(getContext(), destinations, new DestinationAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Integer destinationId, String destinationName) {
                                setUserDestination(destinationId, destinationName);
                            }
                        }));
                    }
                } else {
                    if (response.body() != null) {
                        Log.i("DESTINATIONS", "onResponse : " + response.body().getStatus() + response.body().getSuccess());
                    }
                }
            }

            @Override
            public void onFailure(Call<BusStops> call, Throwable t) {
                Log.i("DESTINATIONS", "onFailure : " + t.getLocalizedMessage() + Arrays.toString(t.getStackTrace()));
            }
        });
    }


    // filter search query function for destination search
    private void filterDestinations(String query) {
        List<Halts> filterDestinations = new ArrayList<>();

        for (Halts destination : destinations) {
            if (destination.getName().toLowerCase().contains(query.toLowerCase())) {
                filterDestinations.add(destination);
            }
        }
        recyclerViewDestinations.setAdapter(new DestinationAdapter(getContext(), filterDestinations, new DestinationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Integer destinationId, String destinationName) {
                Log.i("DESTINATION SELECTED FILTERED", "onItemClick : " + destinationName);
            }
        }));
    }

    public void setUserDestination(int destinationId, String destinationName) {
        Intent intent = new Intent(getContext(), InstantTicketBooking.class);
        intent.putExtra("destinationId", destinationId);
        intent.putExtra("destinationName", destinationName);
        startActivity(intent);
    }
}