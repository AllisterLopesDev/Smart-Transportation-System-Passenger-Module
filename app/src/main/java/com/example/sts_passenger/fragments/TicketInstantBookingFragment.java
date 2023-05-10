package com.example.sts_passenger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sts_passenger.Client;
import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.ticketbooking.BusStops;
import com.example.sts_passenger.ticketbooking.ResponseBusStrand;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TicketInstantBookingFragment extends Fragment {

    private EditText etDate;
    private TextView etSource;
    private Button btnBook;
    private Spinner spinner_value;
    private List<BusStops> busStops;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_instant_booking, container, false);




        etSource = view.findViewById(R.id.et_source);
        spinner_value = view.findViewById(R.id.spinner_values);
        etDate = view.findViewById(R.id.et_date);
        btnBook = view.findViewById(R.id.btn_book);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String myValue = arguments.getString("standName");
            etSource.setText(myValue);
        }
        getDestinations();
        return view;
    }

    public  void  getDestinations(){
        Call<ResponseBusStrand> call = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getBusStands();
        call.enqueue(new Callback<ResponseBusStrand>() {
            @Override
            public void onResponse(Call<ResponseBusStrand> call, Response<ResponseBusStrand> response) {
                if (response.isSuccessful() && response.body() != null) {
                    busStops = response.body().getResult();
                    List<String> busStopNames = new ArrayList<>();
                    for (BusStops busStop : busStops) {
                        busStopNames.add(busStop.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, busStopNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_value.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseBusStrand> call, Throwable t) {
                // Handle error
            }
        });

    }

}