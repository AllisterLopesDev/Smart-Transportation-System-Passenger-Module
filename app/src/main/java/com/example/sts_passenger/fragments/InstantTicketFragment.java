package com.example.sts_passenger.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.model.Bus;
import com.example.sts_passenger.model.Schedule;
import com.example.sts_passenger.model.ScheduleInfo;
import com.example.sts_passenger.model.Ticket;
import com.example.sts_passenger.model.result.TicketBooking;

public class InstantTicketFragment extends Fragment {

    // views
    TextView tvTicketNumber, tvDate, tvBusRegNo, tvBusType, tvSource, tvDestination, tvPassengerCount, tvPrice;

    // qr
    private ImageView qrCodeIV;
    Bitmap bitmap;

    // model
    Bus bus;
    ScheduleInfo scheduleInfo;
    Schedule schedule;
    Ticket ticket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instant_ticket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init views
        initViews(view);
        getBundleData();
    }

    private void initViews(View view) {
        tvTicketNumber = view.findViewById(R.id.tv_instant_ticket_number);
        tvDate = view.findViewById(R.id.tv_instant_ticket_date);
        tvBusRegNo = view.findViewById(R.id.tv_instant_ticket_bus_reg_no);
        tvBusType = view.findViewById(R.id.tv_instant_ticket_bus_type);
        tvSource = view.findViewById(R.id.tv_instant_ticket_source);
        tvDestination = view.findViewById(R.id.tv_instant_ticket_destination);
        tvPassengerCount = view.findViewById(R.id.tv_instant_ticket_passenger_count);
        tvPrice = view.findViewById(R.id.tv_instant_ticket_price);

        // init models
        bus = new Bus();
        scheduleInfo = new ScheduleInfo();
        schedule = new Schedule();
        ticket = new Ticket();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    // get the ticket booked data for pre-booking-confirmation-instant-frag
    private void getBundleData() {
        Bundle data = getArguments();

        /*// variable
        String busRegNo = "";
        String busType = "";
        String source  = "";
        String destination  = "";
        int pCount  = 0;
        int fareAmount  = 0;
        int tId  = 0;
        String date  = "";*/

        if (data != null) {
            /*busRegNo = data.getString("busRegNo");
            busType = data.getString("busType");
            source = data.getString("ticketSource");
            destination = data.getString("ticketDestination");
            pCount = data.getInt("passengerCount");
            fareAmount = data.getInt("ticketAmount");
            tId = data.getInt("ticketId");
            date = data.getString("ticketDate");*/


            bus.setRegistrationNumber(data.getString("busRegNo"));
            bus.setType(data.getString("busType"));
            ticket.setSource(data.getString("ticketSource"));
            ticket.setDestination(data.getString("ticketDestination"));
            ticket.setPassengerCount(data.getInt("passengerCount"));
            ticket.setFareAmount(data.getInt("ticketAmount"));
            ticket.setId(data.getInt("ticketId"));
            ticket.setDate(data.getString("ticketDate"));

            tvTicketNumber.setText(String.valueOf(ticket.getId()));
            tvDate.setText(ticket.getDate());
            tvBusRegNo.setText(bus.getRegistrationNumber());
            tvBusType.setText(bus.getType());
            tvSource.setText(ticket.getSource());
            tvDestination.setText(ticket.getDestination());
            tvPassengerCount.setText(String.valueOf(ticket.getPassengerCount()));
            tvPrice.setText(String.valueOf(ticket.getFareAmount()));
        }

        /*tvBusRegNo.setText(busRegNo);
        tvBusType.setText(busType);*/

    }
}