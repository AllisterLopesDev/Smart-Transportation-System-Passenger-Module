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

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.adapters.AvailableBusScheduleSearchAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.request.InstantTicketBooking;
import com.example.sts_passenger.apiservices.response.BusScheduleSearch;
import com.example.sts_passenger.apiservices.response.SeatAvailability;
import com.example.sts_passenger.model.Bus;
import com.example.sts_passenger.model.CalendarDate;
import com.example.sts_passenger.model.Halts;
import com.example.sts_passenger.model.Route;
import com.example.sts_passenger.model.Schedule;
import com.example.sts_passenger.model.ScheduleInfo;
import com.example.sts_passenger.model.result.BusScheduleResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AvailableBusScheduleSearchListFragment extends Fragment {

    // Views
    RecyclerView recyclerViewAvailableBusSchedules;

    // instances
    List<BusScheduleResult> busScheduleResultList;

    // model class instance to set and get halts & date
    Halts source;
    Halts destination;
    CalendarDate date;
    int passengerCount = 0;
    ScheduleInfo scheduleInfo;
    Route route;

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
            public void onItemClick(Integer busId, Integer scheduleInfoId, Integer scheduleId, String date, String routeFare, String routeDistance) {

            }
        };

    }


    // view initialisation
    private void initViews(View view) {
        recyclerViewAvailableBusSchedules = view.findViewById(R.id.recyclerView_available_bus_schedule_search_list);
        recyclerViewAvailableBusSchedules.setHasFixedSize(true);
        recyclerViewAvailableBusSchedules.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();

        availableBusSchedule();
    }

    private void availableBusSchedule() {
        // get the query params from previous frag and pass in the call
        getBusScheduleSearchQueryParams();

        Call<BusScheduleSearch> busScheduleSearchCall = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getAllAvailableBuses(source.getId(), destination.getId(), date.getDate());
        busScheduleSearchCall.enqueue(new Callback<BusScheduleSearch>() {
            @Override
            public void onResponse(Call<BusScheduleSearch> call, Response<BusScheduleSearch> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("TAG", "onResponse: getting data");
                    busScheduleResultList = response.body().getResult();
                    recyclerViewAvailableBusSchedules.setAdapter(new AvailableBusScheduleSearchAdapter(busScheduleResultList, getContext(), new AvailableBusScheduleSearchAdapter.OnAvailableBusClickListener() {
                        @Override
                        public void onItemClick(Integer busId, Integer scheduleInfoId, Integer scheduleId, String date, String routeFare, String routeDistance) {
                            Log.i("TAG", "onItemClick: store data" + scheduleInfoId + " " + routeFare + " " +routeDistance);
                            route = new Route();
                            scheduleInfo = new ScheduleInfo();

                            route.setFare(routeFare);
                            scheduleInfo.setId(scheduleInfoId);
                            route.setDistance(routeDistance);
                            checkSeatAvailability(busId, passengerCount, scheduleInfoId, scheduleId, date);
                        }
                    }));
                }
            }

            @Override
            public void onFailure(Call<BusScheduleSearch> call, Throwable t) {

            }
        });
    }

    // set query params
    private void getBusScheduleSearchQueryParams() {
        Bundle data = getArguments();

        source = new Halts();
        destination = new Halts();
        date = new CalendarDate();

        if (data != null) {
            source.setId(data.getInt("sourceId"));
            destination.setId(data.getInt("destinationId"));
            date.setDate(data.getString("currentDate"));
            passengerCount = data.getInt("passengerCount");

            Log.i("TAG", "searchQueryParams: " + "source-id " + source.getId() + " destination-id " + destination.getId() + " current-date " + date.getDate() + " count " + passengerCount);
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
                                Log.i("TAG", "onResponse: checkSeatAvailability " + seatAvailabilityResponse.getAvailableSeats());
                                Double totalFare = calculateTotalFareAmount(route.getFare(), passengerCount);
                                Log.i("TAG", "get info for ticket from instance Route & ScheduleInfo " + "scheduleInfo-id " + scheduleInfo.getId() + " distance " + route.getDistance() + " fare " + route.getFare());
                                createInstantTicket(instantTicketBooking(scheduleInfoId, totalFare, route.getDistance(), passengerCount, source.getId(), destination.getId(), 3));
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


    // request data for instant ticket
    private InstantTicketBooking instantTicketBooking(Integer scheduleInfoId, Double routeFare, String routeDistance, Integer passengerCount, Integer sourceId, Integer destinationId, Integer passengerId) {
        InstantTicketBooking instantTicketRequest = new InstantTicketBooking();
        instantTicketRequest.setBookingDate(getCurrentAppTimeStamp());
        instantTicketRequest.setTotalFareAmount(routeFare);
        instantTicketRequest.setDistance(routeDistance);
        instantTicketRequest.setPassengerCount(passengerCount);
        instantTicketRequest.setSourceId(sourceId);
        instantTicketRequest.setDestinationId(destinationId);
        instantTicketRequest.setPassengerId(passengerId);
        instantTicketRequest.setBusScheduleId(scheduleInfoId);

        return instantTicketRequest;
    }

    private void createInstantTicket(InstantTicketBooking instantTicketBooking) {
        Call<com.example.sts_passenger.apiservices.response.InstantTicketBooking> instantTicketBookingCall = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().instantTicket(instantTicketBooking);
        instantTicketBookingCall.enqueue(new Callback<com.example.sts_passenger.apiservices.response.InstantTicketBooking>() {
            @Override
            public void onResponse(Call<com.example.sts_passenger.apiservices.response.InstantTicketBooking> call, Response<com.example.sts_passenger.apiservices.response.InstantTicketBooking> response) {
                if (response.isSuccessful()) {
                    com.example.sts_passenger.apiservices.response.InstantTicketBooking ticketResponse = response.body();
                    if (ticketResponse != null) {
                        Log.i("TAG", "onResponse: " + ticketResponse.getResult().getTicket());
                        Toast.makeText(getContext(), "Instant Ticket Successfully created", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<com.example.sts_passenger.apiservices.response.InstantTicketBooking> call, Throwable t) {

            }
        });

    }

    // function to get current app time-stamp
    private String getCurrentAppTimeStamp() {
        long currentTimeMillis = System.currentTimeMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTimeStamp = simpleDateFormat.format(new Date(currentTimeMillis));

        Log.i("TAG", "getCurrentAppTimeStamp: " + currentTimeStamp);
        return currentTimeStamp;
    }


    // function to get total fare amount
    private Double calculateTotalFareAmount(String fare, Integer passengerCount) {
        Double actualFare = Double.valueOf(fare);
        return actualFare * passengerCount;
    }
}