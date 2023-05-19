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
import com.example.sts_passenger.adapters.AllPassengerPassDetailsAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.PassengerPassDetailsResponse;
import com.example.sts_passenger.model.result.PassDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssueReportingFragment extends Fragment {


    RecyclerView recyclerView;

    List<PassDetails> passDetailsList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_issue_reporting, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.passenger_pass_details_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));




    }

    @Override
    public void onStart() {
        super.onStart();

        passengerPassDetails();

    }

    public void passengerPassDetails(){
        Call<PassengerPassDetailsResponse> passengerPassDetailsResponseCall= Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getPassengerPassDetails(7);

        passengerPassDetailsResponseCall.enqueue(new Callback<PassengerPassDetailsResponse>() {
            @Override
            public void onResponse(Call<PassengerPassDetailsResponse> call, Response<PassengerPassDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    passDetailsList= response.body().getResult();
                    recyclerView.setAdapter(new AllPassengerPassDetailsAdapter(passDetailsList,getContext()));

                }
            }

            @Override
            public void onFailure(Call<PassengerPassDetailsResponse> call, Throwable t) {

            }
        });
    }
}