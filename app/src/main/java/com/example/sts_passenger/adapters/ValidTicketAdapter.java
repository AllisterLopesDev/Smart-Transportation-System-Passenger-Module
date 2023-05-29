package com.example.sts_passenger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.model.result.TicketBooking;

import java.util.List;

public class ValidTicketAdapter extends RecyclerView.Adapter<ValidTicketAdapter.ViewHolder> {
    List<TicketBooking> bookings;
    Context context;

    public ValidTicketAdapter(List<TicketBooking> bookings, Context context) {
        this.bookings = bookings;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.home_ticket_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_busRegNo.setText(bookings.get(position).getBus().getRegistrationNumber());
        holder.tv_busType.setText(bookings.get(position).getBus().getType());
        holder.tv_source.setText(bookings.get(position).getTicket().getSource());
        holder.tv_destination.setText(bookings.get(position).getTicket().getDestination());

    }

    @Override
    public int getItemCount() {
        if (bookings == null) {
            return 0;
        }
        return bookings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //    CardView cv_ticket,cv_pass;
    TextView tv_title,tv_busRegNo,tv_busType,tv_source, tv_destination;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//        tv_title = itemView.findViewById(R.id.tv_ticket_title);
        tv_busRegNo = itemView.findViewById(R.id.bus_regNo);
        tv_busType = itemView.findViewById(R.id.bus_type);
        tv_source = itemView.findViewById(R.id.source);
        tv_destination = itemView.findViewById(R.id.destination);
        }
    }
}
