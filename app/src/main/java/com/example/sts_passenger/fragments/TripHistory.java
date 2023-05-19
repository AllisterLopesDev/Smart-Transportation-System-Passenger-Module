package com.example.sts_passenger.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.adapters.TripHistoryAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.TripHistoryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TripHistory extends Fragment {

    RecyclerView recyclerView;
    List<com.example.sts_passenger.model.TripHistory> tripHistoryList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.recycleView_tripHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        getTripHistory();
    }

    private void getTripHistory() {
        Call<TripHistoryResponse> tripHistoryResponseCall = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getTripHistory(3);
        tripHistoryResponseCall.enqueue(new Callback<TripHistoryResponse>() {
            @Override
            public void onResponse(Call<TripHistoryResponse> call, Response<TripHistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    tripHistoryList  = response.body().getBookings();
                    recyclerView.setAdapter(new TripHistoryAdapter(tripHistoryList,getContext()));
                }
            }

            @Override
            public void onFailure(Call<TripHistoryResponse> call, Throwable t) {

            }
        });
    }
}