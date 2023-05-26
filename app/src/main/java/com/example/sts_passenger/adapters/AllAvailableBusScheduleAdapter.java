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
    OnClickScheduleDetails onClickScheduleDetails;

    public AllAvailableBusScheduleAdapter(List<BusScheduleResult> busScheduleSearchList, Context context, OnClickScheduleDetails onClickScheduleDetails) {
        this.busScheduleSearchList = busScheduleSearchList;
        this.context = context;
        this.onClickScheduleDetails = onClickScheduleDetails;
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
        holder.source.setText(busScheduleSearchList.get(position).getScheduleInfo().getSchedule().getDepartureStand());
        holder.destination.setText(busScheduleSearchList.get(position).getScheduleInfo().getSchedule().getArrivalStand());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && onClickScheduleDetails != null){
                    BusScheduleResult selectedSchedule = busScheduleSearchList.get(pos);
                    String busNo= selectedSchedule.getBus().getRegistrationNumber();
                    String busType = selectedSchedule.getBus().getType();
                    String arrivalStand = selectedSchedule.getScheduleInfo().getSchedule().getArrivalStand();
                    String departureStand = selectedSchedule.getScheduleInfo().getSchedule().getDepartureStand();
                    String arrivalTime= selectedSchedule.getScheduleInfo().getSchedule().getArrival();
                    String departureTime=selectedSchedule.getScheduleInfo().getSchedule().getDeparture();
                    String duration = selectedSchedule.getScheduleInfo().getSchedule().getDuration();
                    Integer seatAvailable =selectedSchedule.getScheduleInfo().getSeatsAvailable();

                    onClickScheduleDetails.onClickItem(busNo, busType,arrivalStand,departureStand,arrivalTime,departureTime,duration,seatAvailable);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return busScheduleSearchList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView busNo,source,destination,arrival,departure;

        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView_bus_schedule);

            busNo=itemView.findViewById(R.id.tv_Bus_no);
            source=itemView.findViewById(R.id.tv_Source);
            destination=itemView.findViewById(R.id.tv_Destination);
            arrival=itemView.findViewById(R.id.tv_Arrival);
            departure=itemView.findViewById(R.id.tv_Departure);
        }
    }

    public interface OnClickScheduleDetails{
        void onClickItem(String busNo,
                         String busType,
                         String arrivalStand,
                         String departureStand,
                         String arrivalTime,
                         String departureTime,
                         String duration,
                         Integer seatAvailable);
    }
}
