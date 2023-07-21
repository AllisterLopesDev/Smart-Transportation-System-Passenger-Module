package com.example.sts_passenger.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.activities.PassQrCode;
import com.example.sts_passenger.adapters.AllPassengerPassDetailsAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.PassengerPassDetailsResponse;
import com.example.sts_passenger.model.Session;
import com.example.sts_passenger.model.result.PassDetails;
import com.example.sts_passenger.sharedpref.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassFragment extends Fragment {

    RecyclerView recyclerViewPassList;

    List<PassDetails> passDetailsList;
    AppCompatImageView noPassDataImageView;

    TextView textView13;
    // SharedPrefManager
    SharedPrefManager sharedPrefManager;
    private Session savedSession;
    AllPassengerPassDetailsAdapter.OnClickPassDetails onClickPassDetails;



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
        noPassDataImageView = view.findViewById(R.id.no_pass);
        textView13 = view.findViewById(R.id.textView13);


        // init sharedPrefManager for passenger session
        setSharedPrefManager();

        recyclerViewPassList = view.findViewById(R.id.passenger_pass_details_recyclerView);
        recyclerViewPassList.setHasFixedSize(true);
        recyclerViewPassList.setLayoutManager(new LinearLayoutManager(getContext()));



        createPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneratePass frag = new GeneratePass();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout_pass_container, frag);
                transaction.commit();
                hideViewsOnFragTransaction();
            }
        });
    }

    // function to hide views on fragment call
    private void hideViewsOnFragTransaction() {
        createPassBtn.setVisibility(View.GONE);
        recyclerViewPassList.setVisibility(View.GONE);
        noPassDataImageView.setVisibility(View.GONE);
        textView13.setVisibility(View.GONE);



    }

    // SharedPrefManager function
    public void setSharedPrefManager() {
        sharedPrefManager = new SharedPrefManager(requireContext());
        savedSession = sharedPrefManager.getSavedSessionOnLogin();
    }



    @Override
    public void onStart() {
        super.onStart();

        passengerPassDetails();

    }


    public void passengerPassDetails(){
        int passengerId = savedSession.getPassenger().getPassengerId();
        Log.i("TAG", "passengerPassDetails: passenger-id " + passengerId);
        Call<PassengerPassDetailsResponse> passengerPassDetailsResponseCall= Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getPassengerPassDetails(savedSession.getPassenger().getPassengerId());

        passengerPassDetailsResponseCall.enqueue(new Callback<PassengerPassDetailsResponse>() {
            @Override
            public void onResponse(Call<PassengerPassDetailsResponse> call, Response<PassengerPassDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    passDetailsList= response.body().getResult();


                    // update img if no pass data is available
                    if (passDetailsList.isEmpty()){
                        recyclerViewPassList.setVisibility(View.GONE);
                        noPassDataImageView.setVisibility(View.VISIBLE);

                        // USE GLIDE LIBRARY TO LOAD IMAGE ON IMAGE VIEW

                        Glide.with(requireContext())
                                .load(R.drawable.no_results)
                                .into(noPassDataImageView);
                    }else{
                        noPassDataImageView.setVisibility(View.GONE);

                        recyclerViewPassList.setAdapter(new AllPassengerPassDetailsAdapter(passDetailsList, getContext(), new AllPassengerPassDetailsAdapter.OnClickPassDetails() {
                            @Override
                            public void onClickItem(Integer passId) {
                                Intent i = new Intent(getContext(), PassQrCode.class);
                                i.putExtra("passId",passId);
                                startActivity(i);
                            }
                        }));
                    }
                }
            }

            @Override
            public void onFailure(Call<PassengerPassDetailsResponse> call, Throwable t) {

            }
        });
    }
}