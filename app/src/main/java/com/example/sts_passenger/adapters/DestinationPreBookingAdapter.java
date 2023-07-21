package com.example.sts_passenger.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.model.Halts;

import java.util.List;

public class DestinationPreBookingAdapter extends RecyclerView.Adapter<DestinationPreBookingAdapter.ViewHolder> {

    List<Halts> destinationBusStopsList;
    Context context;

    // onItemClickListener instance
    public OnItemClickListener onItemClickListener;

    public DestinationPreBookingAdapter(List<Halts> destinationBusStopsList, Context context, OnItemClickListener onItemClickListener) {
        this.destinationBusStopsList = destinationBusStopsList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.destination_prebooking_item, parent, false);

        return new ViewHolder(view, destinationBusStopsList, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvBusStopName.setText(destinationBusStopsList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    Halts clickedBusStop = destinationBusStopsList.get(pos);
                    int busStopId = clickedBusStop.getId();
                    String busStopName = clickedBusStop.getName();
                    onItemClickListener.onItemClick(busStopId, busStopName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return destinationBusStopsList.size();
    }


    // ViewHolder inner class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBusStopName;

        public ViewHolder(@NonNull View itemView, final List<Halts> destinationBusStopsList, final OnItemClickListener listener) {
            super(itemView);

            tvBusStopName = itemView.findViewById(R.id.textView_list_prebooking_destination_name);
        }
    }


    // onItemClick listener interface for destination item clicked
    public interface OnItemClickListener {
        void onItemClick(Integer destinationBusStopId, String destinationBusStopName);
    }
}