package com.example.sts_passenger.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.model.CalendarDate;
import com.example.sts_passenger.model.RouteInfo;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class GeneratePass extends Fragment {
    String selectedDate,passNoOfDays;
    TextView tvDate, tvRoute, title,textView2,textView3,textView4,textView5,textView6,tv_pass_type,tv_total_fare;
    Spinner tvNoOfDays;
    Button createPassBtn;
    private int year, month, day;

    // route info data
    RouteInfo routeInfo;
    String routeName, routeFare, routeDistance;

    CalendarDate calendarDays;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_generate_pass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponets(view);


//        ----------------------- date---------------------------
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentDate();
            }
        });

//        ----------------------- no of days --------------------

        passNoOfDays();

//        ----------------------- source -------------------------
        tvRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PassSourceStop frag = new PassSourceStop();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_generatePass,frag);
                transaction.addToBackStack(null);
                transaction.commit();

                Log.i("TAG", "onClick: Driver Search Fragment opened" );

                hideViewsOnFragTransaction();
            }
        });
        getBundleData();

        double fare = Double.parseDouble(routeInfo.getFare().trim());
        double distance = Double.parseDouble(routeInfo.getDistance().trim());
        double passPrice = calculateFare(fare, distance);
        tv_total_fare.setText(String.valueOf(passPrice));
        Log.i("TAG", "getBundleData: pass price " + passPrice);



    }


    private void  getBundleData(){
        Bundle data = getArguments();
        if (data != null){
            routeInfo.setId(data.getInt("routeInfoId"));
            routeName = data.getString("routeInfoName");
            routeInfo.setFare(data.getString("routeInfoFare"));
            routeInfo.setDistance(data.getString("routeInfoDistance"));
//            routeFare = data.getString("routeInfoFare");
//            routeDistance = data.getString("routeInfoDistance");
        }

//        if (routeInfo.getFare() != null) {
//            double fare = Double.parseDouble(routeInfo.getFare().trim());
//            double distance = Double.parseDouble(routeInfo.getDistance().trim());
//            double passPrice = calculateFare(fare, distance);
//            tv_total_fare.setText(String.valueOf(passPrice));
//            Log.i("TAG", "getBundleData: pass price " + passPrice);
//        }

        if (routeName == null){
            tvRoute.setText("click to select route");
        }else {
            tvRoute.setText(routeName);
        }


    }

    private double calculateFare(Double fare, Double distance) {
        Log.i("TAG", "calculateFare: fare " + routeInfo.getDistance());
        return fare * distance * 2 * calendarDays.getNoOfDays();
    }



    public void hideViewsOnFragTransaction() {
        // Hide the views in the current activity
        title.setVisibility(View.GONE);
        tvRoute.setVisibility(View.GONE);
        tvDate.setVisibility(View.GONE);
        tvNoOfDays.setVisibility(View.GONE);
        createPassBtn.setVisibility(View.GONE);
        tv_total_fare.setVisibility(View.GONE);
        tv_pass_type.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);
        textView4.setVisibility(View.GONE);
        textView5.setVisibility(View.GONE);
        textView6.setVisibility(View.GONE);
    }
    public void initComponets(View view) {
        tvDate = view.findViewById(R.id.textView_pass_start_date);
        tvNoOfDays = view.findViewById(R.id.no_of_days);
        tvRoute = view.findViewById(R.id.pass_route_id);
        createPassBtn = view.findViewById(R.id.create_pass_btn);
        title = view.findViewById(R.id.generate_pass_title);
        tv_pass_type = view.findViewById(R.id.tv_pass_type);
        tv_total_fare = view.findViewById(R.id.tv_total_fare);

        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);
        textView4 = view.findViewById(R.id.textView4);
        textView5 = view.findViewById(R.id.textView5);
        textView6 = view.findViewById(R.id.textView6);


        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        routeInfo = new RouteInfo();
        calendarDays = new CalendarDate();
    }


    public void getCurrentDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        setSelectedDate(year + "-" + (month + 1) + "-" + dayOfMonth);
                        tvDate.setText(selectedDate);
                        Log.i("TAG", "onDateSet: "+selectedDate);
                    }
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

    // number of days pass shouuld be valid
    public void passNoOfDays(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pass_no_of_days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tvNoOfDays.setAdapter(adapter);

        tvNoOfDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        calendarDays.setNoOfDays(30);
                        break;
                    case 1:
                        calendarDays.setNoOfDays(90);
                        break;
                    case 2:
                        calendarDays.setNoOfDays(180);
                        break;
                    case 3:
                        calendarDays.setNoOfDays(360);
                        break;
                    default:
                        break;
                }

                /*setPassNoOfDays(adapterView.getItemAtPosition(position).toString());*/
                Log.i("TAG", "onItemSelected: " + calendarDays.getNoOfDays());
                Log.i("TAG", "onItemSelected: selected item "+adapterView.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the case when no item is selected
            }
        });
    }


    // getter && setter


    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getPassNoOfDays() {
        return passNoOfDays;
    }

    public void setPassNoOfDays(String passNoOfDays) {
        this.passNoOfDays = passNoOfDays;
    }
}