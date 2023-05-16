package com.example.sts_passenger.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.model.Bus;
import com.example.sts_passenger.model.Route;
import com.example.sts_passenger.model.Schedule;
import com.example.sts_passenger.model.ScheduleInfo;
import com.example.sts_passenger.model.result.BusScheduleResult;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AvailableBusScheduleSearchAdapter extends RecyclerView.Adapter<AvailableBusScheduleSearchAdapter.ViewHolder> {

    // instance of Bus Schedule Result list to store response body result
    List<BusScheduleResult> busScheduleResultList;

    Context context;


    // on click listener instance
    public OnAvailableBusClickListener onAvailableBusClickListener;

    public AvailableBusScheduleSearchAdapter(List<BusScheduleResult> busScheduleResultList, Context context, OnAvailableBusClickListener listener) {
        this.busScheduleResultList = busScheduleResultList;
        this.context = context;
        this.onAvailableBusClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.available_bus_schedule_item, parent, false);

        return new ViewHolder(view, onAvailableBusClickListener, busScheduleResultList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvBusRegNum.setText(busScheduleResultList.get(position).getBus().getRegistrationNumber());
        holder.tvBusType.setText(busScheduleResultList.get(position).getBus().getType());
        holder.tvSource.setText(busScheduleResultList.get(position).getRoute().getSource());
        holder.tvDestination.setText(busScheduleResultList.get(position).getRoute().getDestination());
        holder.tvDate.setText(busScheduleResultList.get(position).getScheduleInfo().getDate());
        holder.tvSeatsAvailable.setText(String.valueOf(busScheduleResultList.get(position).getScheduleInfo().getSeatsAvailable()));
        String setFareWithRs = "\u20B9 " + busScheduleResultList.get(position).getRoute().getFare();   // adding rupee symbol using unicode representation to fare price
        holder.tvFare.setText(setFareWithRs);

        // click handler
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION && onAvailableBusClickListener != null) {
                    // bus data
                    Bus clickedBus = busScheduleResultList.get(pos).getBus();
                    Integer busId = clickedBus.getId();
                    // schedule-info data
                    ScheduleInfo clickedScheduleInfo = busScheduleResultList.get(pos).getScheduleInfo();
                    Integer scheduleInfoId = clickedScheduleInfo.getId();
                    String date = clickedScheduleInfo.getDate();
                    // schedule data
                    Schedule clickedSchedule = busScheduleResultList.get(pos).getScheduleInfo().getSchedule();
                    Integer scheduleId = clickedSchedule.getId();
                    // route data
                    Route clickedRoute = busScheduleResultList.get(pos).getRoute();
                    String fare = clickedRoute.getFare();
                    String distance = clickedRoute.getDistance();

                    // pass data onItemClick
                    onAvailableBusClickListener.onItemClick(busId, scheduleInfoId, scheduleId, date, fare, distance);
                    Log.i("TAG", "Adapter onItemClick: " + busId + " " + scheduleInfoId + " " + scheduleId + " " + date);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return busScheduleResultList.size();
    }


    // public view holder inner class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBusRegNum, tvBusType, tvSource, tvDestination, tvDate, tvSeatsAvailable, tvFare;
        MaterialCardView mCardView;

        public ViewHolder(@NonNull View itemView, final OnAvailableBusClickListener listener, final List<BusScheduleResult> busScheduleResultList) {
            super(itemView);

            // init views here
            initViews(itemView);
        }

        // init views
        private void initViews(View view) {
            mCardView = view.findViewById(R.id.cardView_bus_schedule);

            tvBusRegNum = view.findViewById(R.id.textView_bus_reg_no);
            tvBusType = view.findViewById(R.id.textView_bus_type);
            tvSource = view.findViewById(R.id.textView_source);
            tvDestination = view.findViewById(R.id.textView_destination);
            tvDate = view.findViewById(R.id.textView_date);
            tvSeatsAvailable = view.findViewById(R.id.tv_seats_available);
            tvFare = view.findViewById(R.id.textView_fare);
        }
    }

    // on click handler interface for bus schedule item
    public interface OnAvailableBusClickListener {
        void onItemClick(Integer busId, Integer scheduleInfoId, Integer scheduleId, String date, String routeFare, String routeDistance);
    }
}
