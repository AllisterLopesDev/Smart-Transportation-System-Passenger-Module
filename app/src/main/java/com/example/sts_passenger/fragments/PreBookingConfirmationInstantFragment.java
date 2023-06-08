package com.example.sts_passenger.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.request.InstantTicketBooking;
import com.example.sts_passenger.model.Bus;
import com.example.sts_passenger.model.Halts;
import com.example.sts_passenger.model.Route;
import com.example.sts_passenger.model.ScheduleInfo;
import com.example.sts_passenger.model.Session;
import com.example.sts_passenger.model.Ticket;
import com.example.sts_passenger.model.result.TicketBooking;
import com.example.sts_passenger.sharedpref.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PreBookingConfirmationInstantFragment extends Fragment {
    // SharedPrefManager
    SharedPrefManager sharedPrefManager;
    private Session savedSession;

    // views defining
    TextView tvBusRegistrationNumber, tvBusType, tvSource, tvDestination, tvPassengerCount, tvTicketAmount;
    AppCompatButton btnBookInstantTicket;

    // models defining
    Route route;
    Halts source;
    Halts destination;
    Ticket ticket;
    ScheduleInfo scheduleInfo;
    Bus bus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pre_booking_confirmation_instant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    @Override
    public void onStart() {
        super.onStart();

        btnBookInstantTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // calculate fare
                double fare = Double.parseDouble(route.getFare());
                int passengerCount = ticket.getPassengerCount();
                double fareAmount = calculateTotalFareAmount(fare, passengerCount);


                createInstantTicket(instantTicketBooking(scheduleInfo.getId(),
                        fareAmount,
                        route.getDistance(),
                        ticket.getPassengerCount(),
                        source.getId(),
                        destination.getId(),
                        3));
            }
        });
    }

    private void initView(View view) {

        // init views
        tvBusRegistrationNumber = view.findViewById(R.id.tv_bus_registration_no);
        tvBusType = view.findViewById(R.id.tv_bus_type);
        tvSource = view.findViewById(R.id.tv_source);
        tvDestination = view.findViewById(R.id.tv_destination);
        tvPassengerCount = view.findViewById(R.id.tv_passenger_count);
        tvTicketAmount = view.findViewById(R.id.tv_ticket_amount);

        // button
        btnBookInstantTicket = view.findViewById(R.id.appCompatButton_bookTicket);

        // models
        source = new Halts();
        destination = new Halts();
        route = new Route();
        scheduleInfo = new ScheduleInfo();
        ticket = new Ticket();
        bus = new Bus();

        getBundleData();
        setSharedPrefManager();

        // show data on views
        tvBusRegistrationNumber.setText(bus.getRegistrationNumber());
        tvBusType.setText(bus.getType());
        tvSource.setText(route.getSource());
        tvDestination.setText(route.getDestination());
        tvPassengerCount.setText(String.valueOf(ticket.getPassengerCount()));

        // calculate fare and show
        double fare = Double.parseDouble(route.getFare());
        int passengerCount = ticket.getPassengerCount();
        double fareAmount = calculateTotalFareAmount(fare, passengerCount);
        tvTicketAmount.setText(String.valueOf(fareAmount));

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
        instantTicketRequest.setPassengerId(savedSession.getPassenger().getPassengerId());
        instantTicketRequest.setBusScheduleId(scheduleInfoId);

        return instantTicketRequest;
    }

    // api call to create instant ticket
    private void createInstantTicket(InstantTicketBooking instantTicketBooking) {
        Call<com.example.sts_passenger.apiservices.response.InstantTicketBooking> instantTicketBookingCall = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().instantTicket(instantTicketBooking);
        instantTicketBookingCall.enqueue(new Callback<com.example.sts_passenger.apiservices.response.InstantTicketBooking>() {
            @Override
            public void onResponse(Call<com.example.sts_passenger.apiservices.response.InstantTicketBooking> call, Response<com.example.sts_passenger.apiservices.response.InstantTicketBooking> response) {
                if (response.isSuccessful()) {
                    com.example.sts_passenger.apiservices.response.InstantTicketBooking ticketResponse = response.body();
                    if (ticketResponse != null) {
                        Log.i("TAG", "onResponse: " + ticketResponse.getResult().getTicket());
                        TicketBooking instantTicket = ticketResponse.getResult();
                        Toast.makeText(getContext(), "Instant Ticket Successfully created", Toast.LENGTH_SHORT).show();
                        // set data for next frag/activity
                        if (instantTicket != null) {
                            String bType = instantTicket.getBus() != null ? instantTicket.getBus().getBusType() : "";
                            String bRegNo = instantTicket.getBus() != null ? instantTicket.getBus().getRegistrationNumber() : "";
                            String source = instantTicket.getTicket() != null ? instantTicket.getTicket().getSource() : "";
                            String destination = instantTicket.getTicket() != null ? instantTicket.getTicket().getDestination() : "";
                            int pCount = instantTicket.getTicket() != null ? instantTicket.getTicket().getPassengerCount() : 0;
                            int fare = instantTicket.getTicket() != null ? instantTicket.getTicket().getAmount() : 0;
                            int tId = instantTicket.getTicket() != null ? instantTicket.getTicket().getId() : 0;
                            String date = instantTicket.getTicket() != null ? instantTicket.getTicket().getDate() : "";

                            // switch fragment to ticket booked
                            setBundleDateToTicketConfirmationFrag(tId, pCount, fare, bType, bRegNo, source, destination, date);
                        }
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
    private Double calculateTotalFareAmount(Double fare, Integer passengerCount) {
        return fare * passengerCount;
    }


    // set query params
    private void getBundleData() {
        Bundle data = getArguments();

        if (data != null) {
            route.setFare(data.getString("fare"));
            route.setDistance(data.getString("distance"));
            route.setSource(data.getString("source"));
            route.setDestination(data.getString("destination"));
            ticket.setPassengerCount(data.getInt("passengerCount"));
            source.setId(data.getInt("sourceId"));
            destination.setId(data.getInt("destinationId"));
            scheduleInfo.setId(data.getInt("busScheduleId"));
            bus.setRegistrationNumber(data.getString("busRegNo"));
            bus.setType(data.getString("busType"));
        }

        Log.i("TAG", "getBundleData: " + route.getSource() + " " +route.getDestination());
    }


    private void setBundleDateToTicketConfirmationFrag(int ticketId, int pCount, int fare, String bType, String bRegNo, String source, String destination, String date) {
        InstantTicketFragment fragment = new InstantTicketFragment();

        // add data to bundle args
        Bundle args = new Bundle();
        args.putString("busType", bType);
        args.putString("busRegNo", bRegNo);
        args.putString("ticketDestination", destination);
        args.putString("ticketSource", source);
        args.putInt("passengerCount", pCount);
        args.putInt("ticketAmount", fare);
        args.putInt("ticketId", ticketId);
        args.putString("ticketDate", date);
        // set args to frag
        fragment.setArguments(args);

        // transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.ticketContainer, fragment);
        transaction.commit();
    }


    // SharedPrefManager function
    public void setSharedPrefManager() {
        sharedPrefManager = new SharedPrefManager(requireContext());
        savedSession = sharedPrefManager.getSavedSessionOnLogin();
    }
}