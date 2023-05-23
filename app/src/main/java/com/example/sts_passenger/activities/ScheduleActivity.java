package com.example.sts_passenger.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.adapters.AllAvailableBusScheduleAdapter;
import com.example.sts_passenger.adapters.AvailableBusScheduleSearchAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.BusScheduleSearch;
import com.example.sts_passenger.model.Bus;
import com.example.sts_passenger.model.CalendarDate;
import com.example.sts_passenger.model.Halts;
import com.example.sts_passenger.model.result.BusScheduleResult;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleActivity extends AppCompatActivity {

//    TextView tvDatePicker;
//    RecyclerView recyclerView;
//
//    List<BusScheduleResult> busScheduleSearchDetails;
//
//    // for current date picker
//    CalendarDate queryParamsDate;
//    int year, month, day;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_schedule);
//
//        initViews();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        Log.i("TAG", "onStart: " + queryParamsDate.getDate());
//        getAllBusSchedule();
//    }
//
//    private void initViews() {
//        // date picker textview
//        tvDatePicker = findViewById(R.id.textView_search_date_picker);
//
//        // recycler view init
//        recyclerView = findViewById(R.id.rv_busDetails);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//
//        // init the year, month and day variables
//        Calendar calendar = Calendar.getInstance();
//        year = calendar.get(Calendar.YEAR);
//        month = calendar.get(Calendar.MONTH);
//        day = calendar.get(Calendar.DAY_OF_MONTH);
//        // set textView with date
//        tvDatePicker.setText(currentDate(year, month, day));
//    }
//
//
//    public void getAllBusSchedule(){
//        Call<BusScheduleSearch> call= Client.getInstance(Consts.BASE_URL_SCHEDULE).getRoute().getBusScheduleList(queryParamsDate.getDate());
//        call.enqueue(new Callback<BusScheduleSearch>() {
//            @Override
//            public void onResponse(Call<BusScheduleSearch> call, Response<BusScheduleSearch> response) {
//                if (response.isSuccessful() && response.body() != null){
//                        busScheduleSearchDetails = response.body().getResult();
//                        recyclerView.setAdapter(new AllAvailableBusScheduleAdapter(busScheduleSearchDetails, getApplicationContext()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BusScheduleSearch> call, Throwable t) {
//
//            }
//        });
//
//    }
//
//    private String currentDate(int year, int month, int day) {
//        String monthName = "";
//
//        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
//        String[] months = dateFormatSymbols.getMonths();
//
//        if (this.month >= 0 && this.month <= 11) {
//            monthName = months[this.month];
//        }
//
//        // convert month to actual month
//        int actualMonth = this.month + 1;
//        // set month to currentDate instance to pass in query
//        queryParamsDate = new CalendarDate();
//        queryParamsDate.setDate(this.year +"-"+actualMonth+"-"+ this.day);
//
//        return this.day + " " + monthName + ", " + this.year;
//    }
}