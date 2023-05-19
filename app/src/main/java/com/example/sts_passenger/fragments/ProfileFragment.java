package com.example.sts_passenger.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.Consts;
import com.example.sts_passenger.activities.LoginActivity;
import com.example.sts_passenger.R;
import com.example.sts_passenger.apiservices.request.LogoutPassenger;
import com.example.sts_passenger.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    Button logout,tripHistory;
    SharedPrefManager sharedPrefManager;
    TextView tv_title;
    private Context mContext;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logout = view.findViewById(R.id.logout);
        tripHistory = view.findViewById(R.id.tripHistory);
        tv_title = view.findViewById(R.id.tv1);

        tripHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// instance of search ticket frag
                TripHistory fragment = new TripHistory();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout_profile_container, fragment);
                transaction.commit();

                // hide views
                hideViewsOnFragTransaction();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(logoutRequest());
            }
        });
    }

    private void hideViewsOnFragTransaction() {
        tripHistory.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);
        tv_title.setVisibility(View.GONE);
    }

    //------------------------------logout-------------------------
    public LogoutPassenger logoutRequest(){
        LogoutPassenger logoutRequest=new LogoutPassenger();
        logoutRequest.setToken(getSessionToken());
        return logoutRequest;
    }



    public void logout(LogoutPassenger logoutRequest){
        Call<com.example.sts_passenger.apiservices.response.LogoutPassenger> logoutResponseCall= Client.getInstance(Consts.BASE_URL_PASSENGER_AUTH).getRoute().logout(logoutRequest);
        logoutResponseCall.enqueue(new Callback<com.example.sts_passenger.apiservices.response.LogoutPassenger>() {
            @Override
            public void onResponse(Call<com.example.sts_passenger.apiservices.response.LogoutPassenger> call, Response<com.example.sts_passenger.apiservices.response.LogoutPassenger> response) {
                com.example.sts_passenger.apiservices.response.LogoutPassenger logoutResponse=response.body();
                if (response.isSuccessful()){
//                    Toast.makeText(AdminDashboard.this, "Logout successful", Toast.LENGTH_SHORT).show();
                    if(logoutResponse != null && logoutResponse.getStatus() == 200){
                        sharedPrefManager.logout();
                        Toast.makeText(mContext, "Logout successful", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(mContext, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
//                        finish();
                    }
                } else {
                    Toast.makeText(getActivity(), "onResponse: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.example.sts_passenger.apiservices.response.LogoutPassenger> call, Throwable t) {
                Toast.makeText(mContext, "onFailure: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*
     * Custom functions required for this activity
     * */

    // get current user token from shared pref manager
    public String getSessionToken() {
        sharedPrefManager = new SharedPrefManager(mContext);
        return sharedPrefManager.getUser().getToken();
    }
}