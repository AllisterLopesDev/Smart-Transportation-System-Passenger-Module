package com.example.sts_passenger.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.adapters.AllPassengerPassDetailsAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.PassengerPassDetailsResponse;
import com.example.sts_passenger.model.result.PassDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassFragment extends Fragment {

    RecyclerView recyclerView;

    List<PassDetails> passDetailsList;



    FloatingActionButton createPassBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createPassBtn = view.findViewById(R.id.create_pass);

        recyclerView = view.findViewById(R.id.passenger_pass_details_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));



        createPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneratePass frag = new GeneratePass();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout_pass_container, frag);
                transaction.commit();
                Toast.makeText(requireContext(), "hello", Toast.LENGTH_LONG).show();
                hideViewsOnFragTransaction();
            }
        });
    }

    // function to hide views on fragment call
    private void hideViewsOnFragTransaction() {
        createPassBtn.setVisibility(View.GONE);
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