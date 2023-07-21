package com.example.sts_passenger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.model.result.BusScheduleResult;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AllAvailableBusScheduleAdapter extends RecyclerView.Adapter<AllAvailableBusScheduleAdapter.ViewHolder> {

    List<BusScheduleResult> busScheduleSearchList;
    Context context;

    public AllAvailableBusScheduleAdapter(List<BusScheduleResult> busScheduleSearchList, Context context) {
        this.busScheduleSearchList = busScheduleSearchList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context)
                .inflate(R.layout.bus_details_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.busNo.setText(busScheduleSearchList.get(position).getBus().getRegistrationNumber());
        holder.busType.setText(busScheduleSearchList.get(position).getBus().getType());

        holder.source.setText(busScheduleSearchList.get(position).getScheduleInfo().getSchedule().getDepartureStand());
        holder.destination.setText("To "+busScheduleSearchList.get(position).getScheduleInfo().getSchedule().getArrivalStand());
        holder.arrival.setText(" at "+busScheduleSearchList.get(position).getScheduleInfo().getSchedule().getArrival());
        holder.departure.setText(" at "+busScheduleSearchList.get(position).getScheduleInfo().getSchedule().getDeparture());

    }

    @Override
    public int getItemCount() {
        return busScheduleSearchList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView busNo,source,destination,arrival,departure, busType;

        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView_bus_schedule);

            busNo=itemView.findViewById(R.id.tv_Bus_no);
            busType = itemView.findViewById(R.id.bus_type);

            source=itemView.findViewById(R.id.tv_Source);
            destination=itemView.findViewById(R.id.tv_Destination);
            arrival=itemView.findViewById(R.id.tv_arrival);
            departure=itemView.findViewById(R.id.tv_departure);
        }


    }
}
