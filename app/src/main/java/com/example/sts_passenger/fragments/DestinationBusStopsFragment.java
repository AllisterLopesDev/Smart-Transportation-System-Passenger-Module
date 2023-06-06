package com.example.sts_passenger.fragments;

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
import com.example.sts_passenger.adapters.DestinationAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.BusStops;
import com.example.sts_passenger.model.Halts;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DestinationBusStopsFragment extends Fragment {

    RecyclerView recyclerViewDestinationBusStops;

    // List of Halts for destination bus stops
    List<Halts> destinationBusStopsList;

    // onBusStop item click listener
    DestinationAdapter.OnItemClickListener clickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_destination_bus_stops, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        showBusStopsForDestinationStop();
    }

    @Override
    public void onStart() {
        super.onStart();

        // init click listener
        clickListener = new DestinationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Integer destinationBusStopId, String destinationBusStopName) {

            }
        };
    }

    private void initViews(View view) {
        recyclerViewDestinationBusStops = view.findViewById(R.id.recyclerView_destination_bus_stop_list);
        recyclerViewDestinationBusStops.setHasFixedSize(true);
        recyclerViewDestinationBusStops.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void showBusStopsForDestinationStop() {
        Call<BusStops> busStopsApiCall = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getBusStops();
        busStopsApiCall.enqueue(new Callback<BusStops>() {
            @Override
            public void onResponse(Call<BusStops> call, Response<BusStops> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("TAG", "onResponse: response is successful" + response.body().getResult());
                    destinationBusStopsList = response.body().getResult();
                    recyclerViewDestinationBusStops.setAdapter(new DestinationAdapter(destinationBusStopsList, getContext(), new DestinationAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Integer destinationBusStopId, String destinationBusStopName) {
                            setDestinationAndStartSearchInstantTicketFrag(destinationBusStopId, destinationBusStopName);
                        }
                    }));
                }
            }

            @Override
            public void onFailure(Call<BusStops> call, Throwable t) {

            }
        });
    }

    public void setDestinationAndStartSearchInstantTicketFrag(Integer busStopId, String busStopName) {
        // instance of search ticket frag
        SearchInstantTicketFragment fragment = new SearchInstantTicketFragment();

        // create bundle to store data and pass
        Bundle args = new Bundle();
        args.putInt("destinationBusStopId", busStopId);
        args.putString("destinationBusStopName", busStopName);

        // log data passed
        Log.i("TAG", "setDestinationAndStartSearchInstantTicketFrag: " + busStopId + " " + busStopName);
        fragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();  // call getParentFragmentManager when calling fragment from another fragment
        transaction.replace(R.id.ticketContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}