package com.example.sts_passenger.fragments;

import static com.example.sts_passenger.R.drawable.no_results;

import android.graphics.drawable.Drawable;
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

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.adapters.AvailableBusScheduleSearchAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.BusScheduleSearch;
import com.example.sts_passenger.apiservices.response.SeatAvailability;
import com.example.sts_passenger.model.Bus;
import com.example.sts_passenger.model.CalendarDate;
import com.example.sts_passenger.model.Halts;
import com.example.sts_passenger.model.Route;
import com.example.sts_passenger.model.ScheduleInfo;
import com.example.sts_passenger.model.Ticket;
import com.example.sts_passenger.model.result.BusScheduleResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AvailableBusScheduleSearchListFragment extends Fragment {

    // Views
    RecyclerView recyclerViewAvailableBusSchedules;

    // Image view
    AppCompatImageView noBusScheduleAvailableImg;

    // instances
    List<BusScheduleResult> busScheduleResultList;

    // model class instance to set and get halts & date
    Halts source;
    Halts destination;
    CalendarDate date;
    int passengerCount = 0;
    Ticket ticket;
    ScheduleInfo scheduleInfo;
    Route route;
    Bus bus;

    // on bus schedule item clicked handler
    AvailableBusScheduleSearchAdapter.OnAvailableBusClickListener onAvailableBusClickListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_available_bus_schedule_search_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        onAvailableBusClickListener = new AvailableBusScheduleSearchAdapter.OnAvailableBusClickListener() {
            @Override
            public void onItemClick(Integer busId, String busRegNo, String busType, Integer scheduleInfoId, Integer scheduleId, String source, String destination, String date, String routeFare, String routeDistance) {

            }
        };

    }


    // view initialisation
    private void initViews(View view) {
        noBusScheduleAvailableImg = view.findViewById(R.id.appCompatImageView_no_bus_schedule);
        noBusScheduleAvailableImg.setVisibility(View.GONE);

        recyclerViewAvailableBusSchedules = view.findViewById(R.id.recyclerView_available_bus_schedule_search_list);
        recyclerViewAvailableBusSchedules.setHasFixedSize(true);
        recyclerViewAvailableBusSchedules.setLayoutManager(new LinearLayoutManager(getContext()));


        source = new Halts();
        destination = new Halts();
        date = new CalendarDate();
        route = new Route();
        scheduleInfo = new ScheduleInfo();
        ticket = new Ticket();
        bus = new Bus();
    }


    // onStart method
    @Override
    public void onStart() {
        super.onStart();

        availableBusSchedule();
    }


    // api call function to get available bus schedule
    private void availableBusSchedule() {
        // get the query params from previous frag and pass in the call
        getBusScheduleSearchQueryParams();

        // Create a new list for filtered bus schedules
        List<BusScheduleResult> filteredBusScheduleList = new ArrayList<>();

        Call<BusScheduleSearch> busScheduleSearchCall = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getAllAvailableBuses(source.getId(), destination.getId(), date.getDate());
        busScheduleSearchCall.enqueue(new Callback<BusScheduleSearch>() {
            @Override
            public void onResponse(Call<BusScheduleSearch> call, Response<BusScheduleSearch> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("TAG", "onResponse: getting data");
                    busScheduleResultList = response.body().getResult();

                    // iterate through results
                    for (BusScheduleResult busScheduleResult : busScheduleResultList) {
                        // if seats available is greater than 0 show buses
                        if (busScheduleResult.getScheduleInfo().getSeatsAvailable() > 0) {
                            // add busScheduleList to filtered List
                            filteredBusScheduleList.add(busScheduleResult);
                        }
                    }

                    // update ui based on filtered list
                    if (filteredBusScheduleList.isEmpty()) {
                        Log.i("TAG", "onResponse: if seats are > 0");
                        // hide recycler view and show no results image
                        noBusScheduleAvailableImg.setVisibility(View.VISIBLE);
                        noBusScheduleAvailableImg.setImageResource(no_results);
                    } else {
                        // show buses with available seats
                        noBusScheduleAvailableImg.setVisibility(View.INVISIBLE);
                        recyclerViewAvailableBusSchedules.setAdapter(new AvailableBusScheduleSearchAdapter(filteredBusScheduleList, getContext(), new AvailableBusScheduleSearchAdapter.OnAvailableBusClickListener() {
                            @Override
                            public void onItemClick(Integer busId, String busRegNo, String busType, Integer scheduleInfoId, Integer scheduleId, String source, String destination, String date, String routeFare, String routeDistance) {
                                Log.i("TAG", "onItemClick: store data" + scheduleInfoId + " " + routeFare + " " +routeDistance + " " +source + " " +destination);

                                // store data for further processing
                                route.setFare(routeFare);
                                route.setDistance(routeDistance);
                                route.setSource(source);
                                route.setDestination(destination);
                                scheduleInfo.setId(scheduleInfoId);
                                bus.setRegistrationNumber(busRegNo);
                                bus.setType(busType);

                                // check seat availability
                                checkSeatAvailability(busId, ticket.getPassengerCount(), scheduleInfoId, scheduleId, date);
                            }
                        }));
                    }
                }
            }

            @Override
            public void onFailure(Call<BusScheduleSearch> call, Throwable t) {

            }
        });
    }


    // start booking status fragment to confirm ticket
    private void showBookingStatusFrag() {
        PreBookingConfirmationInstantFragment fragment = new PreBookingConfirmationInstantFragment();

        // bundle to store data to pass
        Bundle args = new Bundle();
        // api call data @instant ticket
        args.putString("fare", route.getFare());
        args.putString("distance", route.getDistance());
        args.putInt("passengerCount", ticket.getPassengerCount());
        args.putInt("sourceId", source.getId());
        args.putInt("destinationId", destination.getId());
        args.putInt("busScheduleId", scheduleInfo.getId());

        // views data @Booking confirmation
        args.putString("busRegNo", bus.getRegistrationNumber());
        args.putString("busType", bus.getType());
        args.putString("source", route.getSource());
        args.putString("destination", route.getDestination());

        // set arguments
        fragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();  // call getParentFragmentManager when calling fragment from another fragment
        transaction.replace(R.id.frameLayout_booking_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



    // set query params
    private void getBusScheduleSearchQueryParams() {
        Bundle data = getArguments();

        if (data != null) {
            source.setId(data.getInt("sourceId"));
            destination.setId(data.getInt("destinationId"));
            date.setDate(data.getString("currentDate"));
            ticket.setPassengerCount(data.getInt("passengerCount"));

            Log.i("TAG", "searchQueryParams: " + "source-id " + source.getId() + " destination-id " + destination.getId() + " current-date " + date.getDate() + " count " + ticket.getPassengerCount());
        }
    }



    // function to check availability of seat and book ticket if available
    private void checkSeatAvailability(Integer busId, Integer passengerCount, Integer scheduleInfoId, Integer scheduleId, String date) {
        Log.i("TAG", "onItemClick: store data" + route.getDistance() + " " + route.getFare() + " " + scheduleInfo.getId());

        Call<SeatAvailability> seatAvailabilityCall = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().checkSeatAvailability(busId, passengerCount, date, scheduleInfoId, scheduleId);
        seatAvailabilityCall.enqueue(new Callback<SeatAvailability>() {
            @Override
            public void onResponse(Call<SeatAvailability> call, Response<SeatAvailability> response) {
                if (response.isSuccessful()) {
                    SeatAvailability seatAvailabilityResponse = response.body();
                    if (seatAvailabilityResponse != null) {
                        int statusCode = seatAvailabilityResponse.getStatus();
                        // switch for different response code
                        switch (statusCode) {
                            case 200:
                                // successful response
                                showBookingStatusFrag();
                                break;
                            case 400:
                                // handle case 400
                                String errorMessage = seatAvailabilityResponse.getMessage();
                                Log.i("TAG", "onResponse: " + errorMessage);
                                break;
                            default:
                                // Handle unknown status code
                                Log.i("TAG", "onResponse: " + statusCode);
                                break;
                        }
                    }
                } else {
                    Log.i("TAG", "onResponse: request unsuccessful ");
                }
            }

            @Override
            public void onFailure(Call<SeatAvailability> call, Throwable t) {

            }
        });

    }

}