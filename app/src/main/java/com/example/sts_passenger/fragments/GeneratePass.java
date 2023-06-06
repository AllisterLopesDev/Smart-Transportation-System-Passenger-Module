package com.example.sts_passenger.fragments;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import android.widget.Toast;

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.request.PassDetailsRequest;
import com.example.sts_passenger.apiservices.response.PassDetailsResponse;
import com.example.sts_passenger.model.CalendarDate;
import com.example.sts_passenger.model.RouteInfo;
import com.example.sts_passenger.model.Session;
import com.example.sts_passenger.sharedpref.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneratePass extends Fragment {

    // SharedPrefManager
    SharedPrefManager sharedPrefManager;
    private Session savedSession;
    String selectedDate;
    String totalPassFaretoString;
    TextView tvDate, tvRoute, title,textView2,textView3,textView4,textView5,textView6,tv_pass_type,tv_total_fare;
    Spinner tvNoOfDays;
    Button createPassBtn;
    private int year, month, day;

    // route info data
    RouteInfo routeInfo;
    String routeName;

    CalendarDate calendarDays;

    // Fare
    double fare;
    double distance;
    double passPrice;

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

//        ----------------------- source -------------------------
        tvRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PassSourceStop frag = new PassSourceStop();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_generatePass,frag);
                transaction.addToBackStack(null);
                transaction.commit();
                hideViewsOnFragTransaction();
            }
        });
        getBundleData();

//        ----------------------- date---------------------------
        tvDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                getCurrentDate();
            }
        });

//        ----------------------- no of days --------------------

        passNoOfDays();

        /* -------------------- calculate total fare --------------------*/
        tv_total_fare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateFare(calendarDays.getNoOfDays());
            }
        });


        /* ------------------- submit button  ------------------------------*/

        createPassBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                addPassDetails(passDetails());
            }
        });



    }

    private void  getBundleData(){
        Bundle data = getArguments();
        if (data != null){
            routeInfo.setId(data.getInt("routeInfoId"));
            routeName = data.getString("routeInfoName");
            routeInfo.setFare(data.getString("routeInfoFare"));
            routeInfo.setDistance(data.getString("routeInfoDistance"));
        }

        if (routeName == null && routeInfo.getFare() == null){
            tvRoute.setText("click to select route");
        }else {
            tvRoute.setText(routeName);
            Log.i("TAG", "getBundleData: pass price " + passPrice);
        }


    }

    private double calculateFare(int noOfDays) {
        fare = Double.parseDouble(routeInfo.getFare());
        distance = Double.parseDouble(routeInfo.getDistance());
        Log.i("TAG", "calculateFare: distance is :  " +distance);
        double totalPassFare = distance * 2 * noOfDays;
             setTotalPassFaretoString(String.valueOf(totalPassFare));
        Log.i("TAG", "calculateFare: total fARE is :  " +totalPassFare);
        tv_total_fare.setText(getTotalPassFaretoString());

        return totalPassFare;
    }



    public void hideViewsOnFragTransaction() {
        // Hide the views in the current activity
        title.setVisibility(View.GONE);
        tvRoute.setVisibility(View.GONE);
        tvDate.setVisibility(View.GONE);
        tvNoOfDays.setVisibility(View.GONE);
        createPassBtn.setVisibility(View.GONE);
        tv_total_fare.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);
        textView4.setVisibility(View.GONE);
        textView6.setVisibility(View.GONE);
    }
    public void initComponets(View view) {
        tvDate = view.findViewById(R.id.textView_pass_start_date);
        tvNoOfDays = view.findViewById(R.id.no_of_days);
        tvRoute = view.findViewById(R.id.pass_route_id);
        createPassBtn = view.findViewById(R.id.create_pass_btn);
        title = view.findViewById(R.id.generate_pass_title);
        tv_total_fare = view.findViewById(R.id.tv_total_fare);

        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);
        textView4 = view.findViewById(R.id.textView4);
        textView6 = view.findViewById(R.id.textView6);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        routeInfo = new RouteInfo();
        calendarDays = new CalendarDate();

        // init sharedPrefManager for passenger session
        setSharedPrefManager();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getCurrentDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        setSelectedDate(year + "-" + (month + 1) + "-" + dayOfMonth);
                        tvDate.setText(getSelectedDate());
                        Log.i("TAG", "onDateSet: "+getSelectedDate());
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
                Log.i("TAG", "onItemSelected: selected item "+adapterView.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the case when no item is selected
            }
        });
    }



    // get date after days
    public String getDateAfterDays(String selectedDate, int noOfDays) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        try {
            Date date = sdf.parse(selectedDate);
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, noOfDays);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // SharedPrefManager function
    public void setSharedPrefManager() {
        sharedPrefManager = new SharedPrefManager(requireContext());
        savedSession = sharedPrefManager.getSavedSessionOnLogin();
    }


    // getter && setter
    public String getSelectedDate() {
        return selectedDate;
    }
    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getTotalPassFaretoString() {
        return totalPassFaretoString;
    }

    public void setTotalPassFaretoString(String totalPassFaretoString) {
        this.totalPassFaretoString = totalPassFaretoString;
    }

    /* pass data to the database */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public PassDetailsRequest passDetails(){
        PassDetailsRequest passData = new PassDetailsRequest();
        String endDate = getDateAfterDays(getSelectedDate(),calendarDays.getNoOfDays());
        passData.setValidDate(getSelectedDate());
        Log.i("TAG", "passDetails: start date - "+passData.getValidDate());
        passData.setValidTill(endDate);
        Log.i("TAG", "passDetails: end date - "+passData.getValidTill());
        passData.setRouteInfoId(routeInfo.getId());
        Log.i("TAG", "passDetails: route - "+routeInfo.getId());
        passData.setPrice(Double.valueOf(totalPassFaretoString));
        Log.i("TAG", "passDetails: total fare - "+totalPassFaretoString);
        return passData;
    }

    public void addPassDetails(PassDetailsRequest passData){
        Call<PassDetailsResponse> call = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().addPassengerPassDetails(savedSession.getPassenger().getPassengerId(),passData);
        call.enqueue(new Callback<PassDetailsResponse>() {
            @Override
            public void onResponse(Call<PassDetailsResponse> call, Response<PassDetailsResponse> response) {
                if (response.isSuccessful()){
                    if (response.body() !=null || response.body().getSuccess() == true){
                        Log.i("TAG", "onResponse: " +response.body());
                        PassFragment frag = new PassFragment();
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container_generatePass, frag);
                        ft.commit();
                        Toast.makeText(requireContext(), "Pass generated successfully", Toast.LENGTH_LONG).show();
                        hideViewsOnFragTransaction();
                    }
                }else{
                    Toast.makeText(getContext(), "Pass already exist with similar route", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PassDetailsResponse> call, Throwable t) {

            }
        });
    }
}