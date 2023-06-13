package com.example.sts_passenger.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sts_passenger.R;
import com.example.sts_passenger.assets.QRCodeGenerator;
import com.example.sts_passenger.model.Bus;
import com.example.sts_passenger.model.Schedule;
import com.example.sts_passenger.model.ScheduleInfo;
import com.example.sts_passenger.model.Ticket;
import com.example.sts_passenger.model.result.TicketBooking;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class InstantTicketFragment extends Fragment {

    // views
    TextView tvTicketNumber, tvDate, tvBusRegNo, tvBusType, tvSource, tvDestination, tvPassengerCount, tvPrice;

    // qr
    private ImageView qrCodeIV;
    private ImageView imgWhatsAppShare;

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

        imgWhatsAppShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTicketOnWhatsApp();
            }
        });
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

        // ImageView to share on whatsapp
        imgWhatsAppShare = view.findViewById(R.id.img_shareOn_whatsApp);

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

    // Share instant ticket on whatsapp
    @SuppressLint("QueryPermissionsNeeded")
    private void shareTicketOnWhatsApp() {
        // Create message with the ticket details
        String message = "Ticket Details:"
                + "\nTicket Number: " + ticket.getId()
                + "\nDate: " + ticket.getDate()
                + "\nBus Registration Number: " + bus.getRegistrationNumber()
                + "\nBus Type: " + bus.getType()
                + "\nSource: " + ticket.getSource()
                + "\nDestination: " + ticket.getDestination()
                + "\nPassenger Count: " + ticket.getPassengerCount()
                + "\nPrice: " + ticket.getFareAmount();

        // Generate QR code bitmap
        String ticketId = String.valueOf(ticket.getId());
        File qrCodeFile = QRCodeGenerator.generateQRCode(ticketId, requireContext());

        if (qrCodeFile != null) {
            // Get the content URI for the QR code image using FileProvider
            Uri qrCodeUri = FileProvider.getUriForFile(requireContext(), "com.example.sts_passenger.fileprovider", qrCodeFile);

            Log.i("TAG", "shareTicketOnWhatsApp: QR code generated");
            // Intent to Share on Whatsapp
            Intent intent = new Intent(Intent.ACTION_SEND);
            // intent.setType("text/plain");
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, qrCodeUri);
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // intent.setPackage("com.whatsapp");

            // Verify if WhatsApp is installed on the device
            PackageManager packageManager = requireActivity().getPackageManager();
            if (intent.resolveActivity(packageManager) != null) {
                // Start the activity
                startActivity(intent);
            } else {
                // WhatsApp is not installed, display an error message or redirect to the Play Store
                Toast.makeText(requireContext(), "WhatsApp is not installed", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle error when saving QR code image
            Toast.makeText(requireContext(), "Failed to generate QR code", Toast.LENGTH_SHORT).show();
        }
    }
}