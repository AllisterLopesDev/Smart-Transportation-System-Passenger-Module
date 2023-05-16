package com.example.sts_passenger.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.BusScheduleSearch;

import retrofit2.Call;

public class ScheduleActivity extends AppCompatActivity {

    TextView busScheduleDetails;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        busScheduleDetails=findViewById(R.id.busScheduleDetails);
        recyclerView=findViewById(R.id.rv_busDetails);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



    }
//
//    public void getAllBusSchedule(){
//        Call<BusScheduleSearch> call= Client.getInstance(Consts.BASE_URL_BUS).getRoute().getAllAvailableBuses()
//    }
}