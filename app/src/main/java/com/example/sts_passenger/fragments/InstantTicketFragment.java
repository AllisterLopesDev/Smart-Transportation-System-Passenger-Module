package com.example.sts_passenger.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.assets.QRCodeGenerator;
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

        // Restore the fragment state
        if (savedInstanceState != null) {
            Log.i("TAG", "onViewCreated: SavedInstanceState");
            restoreInstanceState(savedInstanceState);
        } else {
            // If no saved instance state, get the ticket bundle data
            getBundleData();
        }
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

        // ImageView to hold qr code
        qrCodeIV = view.findViewById(R.id.imageView_ticket_qr);

        // init models
        bus = new Bus();
        scheduleInfo = new ScheduleInfo();
        schedule = new Schedule();
        ticket = new Ticket();
    }

    // get the ticket booked data for pre-booking-confirmation-instant-frag
    private void getBundleData() {
        Bundle data = getArguments();

        if (data != null) {
            // Data required to show instant ticket
            bus.setRegistrationNumber(data.getString("busRegNo"));
            bus.setType(data.getString("busType"));
            ticket.setSource(data.getString("ticketSource"));
            ticket.setDestination(data.getString("ticketDestination"));
            ticket.setPassengerCount(data.getInt("passengerCount"));
            ticket.setFareAmount(data.getInt("ticketAmount"));
            ticket.setId(data.getInt("ticketId"));
            ticket.setDate(data.getString("ticketDate"));

            // Set data on TextView
            tvTicketNumber.setText(String.valueOf(ticket.getId()));
            tvDate.setText(ticket.getDate());
            tvBusRegNo.setText(bus.getRegistrationNumber());
            tvBusType.setText(bus.getType());
            tvSource.setText(ticket.getSource());
            tvDestination.setText(ticket.getDestination());
            tvPassengerCount.setText(String.valueOf(ticket.getPassengerCount()));
            tvPrice.setText(String.valueOf(ticket.getFareAmount()));

            // Create qr-code
            generateInstantTicketQr();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the fragment state to the bundle
        outState.putString("busRegNo", bus.getRegistrationNumber());
        outState.putString("busType", bus.getRegistrationNumber());
        outState.putString("ticketSource", ticket.getSource());
        outState.putString("ticketDestination", ticket.getDestination());
        outState.putInt("passengerCount", ticket.getPassengerCount());
        outState.putInt("ticketAmount", ticket.getFareAmount());
        outState.putInt("ticketId", ticket.getId());
        outState.putString("ticketDate", ticket.getDate());
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        // Restore the fragment state from the saved bundle
        bus.setRegistrationNumber(savedInstanceState.getString("busRegNo"));
        bus.setType(savedInstanceState.getString("busType"));
        ticket.setSource(savedInstanceState.getString("ticketSource"));
        ticket.setDestination(savedInstanceState.getString("ticketDestination"));
        ticket.setPassengerCount(savedInstanceState.getInt("passengerCount"));
        ticket.setFareAmount(savedInstanceState.getInt("ticketAmount"));
        ticket.setId(savedInstanceState.getInt("ticketId"));
        ticket.setDate(savedInstanceState.getString("ticketDate"));

        // Set the restored data on TextViews
        tvTicketNumber.setText(String.valueOf(ticket.getId()));
        tvDate.setText(ticket.getDate());
        tvBusRegNo.setText(bus.getRegistrationNumber());
        tvBusType.setText(bus.getType());
        tvSource.setText(ticket.getSource());
        tvDestination.setText(ticket.getDestination());
        tvPassengerCount.setText(String.valueOf(ticket.getPassengerCount()));
        tvPrice.setText(String.valueOf(ticket.getFareAmount()));
    }

    private void generateInstantTicketQr() {
        // Print in logs
        Log.i("TAG", "generateInstantTicketQr: creating qr");
        String ticketId = String.valueOf(ticket.getId());
        QRCodeGenerator.generateQRCode(ticketId, qrCodeIV);
        Log.i("TAG", "generateInstantTicketQr: qr created");
    }
}