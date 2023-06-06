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
import com.example.sts_passenger.adapters.PassSourceAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.RouteInfoResponse;
import com.example.sts_passenger.model.RouteInfo;
import com.example.sts_passenger.model.result.TicketBooking;
import com.example.sts_passenger.sharedpref.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassSourceStop extends Fragment {
    RecyclerView recyclerView;
    List<RouteInfo> routeInfoList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pass_source_stop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView_source_for_passbooking);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getRouteInfoForPass();
    }


    public void getRouteInfoForPass() {
        Call<RouteInfoResponse> call = Client.getInstance(Consts.BASE_URL_SCHEDULE).getRoute().getRouteInfo();
        call.enqueue(new Callback<RouteInfoResponse>() {
            @Override
            public void onResponse(Call<RouteInfoResponse> call, Response<RouteInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    routeInfoList = response.body().getResult();



//                    // filter route based on type "shuttle"
//                    List<RouteInfo> filterRoutes = new ArrayList<>();
//                    for (RouteInfo route : routeInfoList) {
//                        if (route.getType().equals("SHUTTLE")) {
//                            filterRoutes.add(route);
//                        }
//                    }



                    recyclerView.setAdapter(new PassSourceAdapter(routeInfoList, getContext(), new PassSourceAdapter.OnPassRouteInfoClickListener() {
                        @Override
                        public void onClickListener(Integer routeId,String haltName, String fare, String distance) {
                            Log.i("TAG", "onClickListener: selected Route-info" + routeId + " " + haltName);
                            setRouteInfoData(routeId, haltName, fare, distance);
                        }
                    }));
                }
            }

            @Override
            public void onFailure(Call<RouteInfoResponse> call, Throwable t) {

            }
        });
    }

    private void setRouteInfoData(int id, String name, String fare, String distance){
        GeneratePass frag = new GeneratePass();
        Bundle args = new Bundle();
        args.putInt("routeInfoId", id);
        args.putString("routeInfoName", name);
        args.putString("routeInfoFare", fare);
        args.putString("routeInfoDistance", distance);
        frag.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_generatePass,frag);
        transaction.commit();
    }


}