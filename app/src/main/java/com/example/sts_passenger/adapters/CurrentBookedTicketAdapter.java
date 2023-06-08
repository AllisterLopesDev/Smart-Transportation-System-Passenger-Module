package com.example.sts_passenger.adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.assets.QRCodeGenerator;
import com.example.sts_passenger.model.result.TicketResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CurrentBookedTicketAdapter extends RecyclerView.Adapter<CurrentBookedTicketAdapter.ViewHolder> {
    List<TicketResult> ticketResultList;

    public CurrentBookedTicketAdapter(List<TicketResult> ticketResultList) {
        this.ticketResultList = ticketResultList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.current_booked_ticket_item,
                        parent,
                        false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvBusRegNo.setText(ticketResultList.get(position).getBus().getRegistrationNumber());
        holder.tvBusType.setText(ticketResultList.get(position).getBus().getType());
        holder.tvSource.setText(ticketResultList.get(position).getTicket().getSource());
        holder.tvDestination.setText(ticketResultList.get(position).getTicket().getDestination());
        holder.tvPassengerCount.setText(String.valueOf(ticketResultList.get(position).getTicket().getPassengerCount()));
        holder.tvFare.setText(String.valueOf(ticketResultList.get(position).getTicket().getFareAmount()));

        // Convert date time stamp to date
        String timeStamp = ticketResultList.get(position).getTicket().getDate();
        Log.i("DATE", "onBindViewHolder: date server " + timeStamp);
        String formattedDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDate = formatDate(timeStamp);
        }
        Log.i("DATE", "onBindViewHolder: date " +formattedDate);
        holder.tvDate.setText(formattedDate);

        // Qr code data
        String data = String.valueOf(ticketResultList.get(position).getTicket().getId());
        QRCodeGenerator.generateQRCode(data, holder.imgTicketQr);
    }

    @Override
    public int getItemCount() {
        if (ticketResultList == null) {
            return 0;
        }

        return ticketResultList.size();
    }


    // ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Variable for Views
        TextView tvBusRegNo, tvBusType, tvSource, tvDestination, tvPassengerCount, tvDate, tvFare;
        ImageView imgTicketQr;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBusRegNo = itemView.findViewById(R.id.tv_cbt_busNo);
            tvBusType = itemView.findViewById(R.id.tv_cbt_busType);
            tvDestination = itemView.findViewById(R.id.tv_cbt_destination);
            tvSource = itemView.findViewById(R.id.tv_cbt_source);
            tvPassengerCount = itemView.findViewById(R.id.tv_cbt_passengerCount);
            tvDate = itemView.findViewById(R.id.tv_cbt_date);
            tvFare = itemView.findViewById(R.id.tv_cbt_fare);

            imgTicketQr = itemView.findViewById(R.id.img_qrCode);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String formatDate(String timeStamp) {
        DateTimeFormatter inputFormat = null;
        DateTimeFormatter outputFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            inputFormat = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z");
            outputFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        }

        try {
            LocalDateTime dateTime = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dateTime = LocalDateTime.parse(timeStamp, inputFormat);

            }
            return dateTime.format(outputFormat);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
