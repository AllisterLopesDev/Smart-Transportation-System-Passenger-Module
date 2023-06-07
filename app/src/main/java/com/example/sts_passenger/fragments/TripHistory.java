package com.example.sts_passenger.fragments;

import static com.example.sts_passenger.R.drawable.no_results;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.adapters.TripHistoryAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.TripHistoryResponse;
import com.example.sts_passenger.model.Session;
import com.example.sts_passenger.sharedpref.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TripHistory extends Fragment {

    RecyclerView recyclerView;
    AppCompatImageView no_trip_data;
    List<com.example.sts_passenger.model.TripHistory> tripHistoryList;
    SharedPrefManager sharedPrefManager;
    private Session session;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setSharedPrefManager();

        no_trip_data = view.findViewById(R.id.no_trip_history);

        recyclerView = view.findViewById(R.id.recycleView_tripHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        getTripHistory();
    }

    private void getTripHistory() {
        Integer passengerId  = session.getPassenger().getPassengerId();


        Call<TripHistoryResponse> tripHistoryResponseCall = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getTripHistory(passengerId);

        tripHistoryResponseCall.enqueue(new Callback<TripHistoryResponse>() {
            @Override
            public void onResponse(Call<TripHistoryResponse> call, Response<TripHistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    tripHistoryList  = response.body().getBookings();

                    // update ui based on filtered list
                    if (tripHistoryList.isEmpty()) {
                        Log.i("TAG", "onResponse: if seats are > 0");
                        // hide recycler view and show no results image
                        recyclerView.setVisibility(View.GONE);
                        no_trip_data.setVisibility(View.VISIBLE);

                        // Use Glide to load the image into the ImageView
                        Glide.with(requireContext())
                                .load(no_results)
                                .into(no_trip_data);
                    }else {
                        no_trip_data.setVisibility(View.GONE);
                        recyclerView.setAdapter(new TripHistoryAdapter(tripHistoryList,getContext()));
                    }


                }
            }

            @Override
            public void onFailure(Call<TripHistoryResponse> call, Throwable t) {

            }
        });
    }

    // SharedPrefManager function
    public void setSharedPrefManager() {
        sharedPrefManager = new SharedPrefManager(requireContext());
        session = sharedPrefManager.getSavedSessionOnLogin();
    }
}