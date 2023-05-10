package com.example.sts_passenger.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sts_passenger.Client;
import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.adapters.AvailableBusAdapter;
import com.example.sts_passenger.buslistresponse.BusListResponse;
import com.example.sts_passenger.searchbuses.Results;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class BusAvailableFragment extends Fragment {

    RecyclerView recyclerViewAvailableBus;

    List<Results> busResultsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bus_available, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewAvailableBus = view.findViewById(R.id.rv_available_bus_list);
        recyclerViewAvailableBus.setHasFixedSize(true);
        recyclerViewAvailableBus.setLayoutManager(new LinearLayoutManager(getContext()));

        getAvailableBusesOnSearch();
    }

    public void getAvailableBusesOnSearch() {
        Call<BusListResponse> busListResponseCall = Client.getInstance(Consts.BASE_URL_BUS).getRoute().getBusList(1, 5, "2023-04-03");
        busListResponseCall.enqueue(new Callback<BusListResponse>() {
            @Override
            public void onResponse(Call<BusListResponse> call, Response<BusListResponse> response) {
                if (response.isSuccessful()) {
                    BusListResponse busListResponse = response.body();
//                    Log.d("TAG", "Response body: " + new Gson().toJson(busListResponse));
                    if (busListResponse.getStatus() == 200) {
                        recyclerViewAvailableBus.setAdapter(new AvailableBusAdapter(busResultsList, getContext()));
                    }
                } else {
                    Log.d("TAG", "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BusListResponse> call, Throwable t) {
                Toast.makeText(getContext(), "error in fetching data " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.i("TAG", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}