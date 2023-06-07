package com.example.sts_passenger.fragments;

import static com.example.sts_passenger.R.drawable.no_results;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.adapters.AllAvailableBusScheduleAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.BusScheduleSearch;
import com.example.sts_passenger.model.CalendarDate;
import com.example.sts_passenger.model.result.BusScheduleResult;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleFragment extends Fragment {

    TextView tvDatePicker;
    RecyclerView recyclerViewScheduleList;
    AppCompatImageView no_schedule_data_image;

    List<BusScheduleResult> busScheduleSearchDetails;

    // for current date picker
    CalendarDate queryParamsDate;
    int year, month, day;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        no_schedule_data_image = view.findViewById(R.id.no_schedule_data);
        tvDatePicker = view.findViewById(R.id.textView_search_date_picker);

        // recycler view init
        recyclerViewScheduleList = view.findViewById(R.id.rv_busDetails);
        recyclerViewScheduleList.setHasFixedSize(true);
        recyclerViewScheduleList.setLayoutManager(new LinearLayoutManager(getContext()));

        // init the year, month and day variables
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        // set textView with date
        tvDatePicker.setText(currentDate(year, month, day));

        getAllBusSchedule();
    }



    public void getAllBusSchedule(){
        Call<BusScheduleSearch> call= Client.getInstance(Consts.BASE_URL_SCHEDULE).getRoute().getBusScheduleList(queryParamsDate.getDate());
        call.enqueue(new Callback<BusScheduleSearch>() {
            @Override
            public void onResponse(Call<BusScheduleSearch> call, Response<BusScheduleSearch> response) {
                if (response.isSuccessful() && response.body() != null){
                    busScheduleSearchDetails = response.body().getResult();

                    // ui changes if not bus schedule available
                    if (busScheduleSearchDetails.isEmpty()){
                        recyclerViewScheduleList.setVisibility(View.GONE);
                        no_schedule_data_image.setVisibility(View.VISIBLE);

                        // Use Glide to load the image into the ImageView
                        Glide.with(requireContext())
                                .load(no_results)
                                .into(no_schedule_data_image);
                    }else {
                        no_schedule_data_image.setVisibility(View.GONE);
                        recyclerViewScheduleList.setAdapter(new AllAvailableBusScheduleAdapter(busScheduleSearchDetails, getContext()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BusScheduleSearch> call, Throwable t) {

            }
        });

    }

    private String currentDate(int year, int month, int day) {
        String monthName = "";

        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        String[] months = dateFormatSymbols.getMonths();

        if (this.month >= 0 && this.month <= 11) {
            monthName = months[this.month];
        }

        // convert month to actual month
        int actualMonth = this.month + 1;
        // set month to currentDate instance to pass in query
        queryParamsDate = new CalendarDate();
        queryParamsDate.setDate(this.year +"-"+actualMonth+"-"+ this.day);

        return this.day + " " + monthName + ", " + this.year;
    }
}