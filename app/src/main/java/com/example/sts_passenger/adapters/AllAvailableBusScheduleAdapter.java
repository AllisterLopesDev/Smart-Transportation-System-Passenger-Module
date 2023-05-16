package com.example.sts_passenger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.model.Schedule;

import java.util.List;

public class AllAvailableBusScheduleAdapter extends RecyclerView.Adapter<AllAvailableBusScheduleAdapter.ViewHolder> {

    List<Schedule> scheduleList;
    Context context;

    public AllAvailableBusScheduleAdapter(@NonNull View itemView, List<Schedule> scheduleList, Context context) {
        this.scheduleList = scheduleList;
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

        holder.busNo.setText(scheduleList.get(position).getArrivalStand());
        holder.source.setText(scheduleList.get(position).getDepartureStand());

    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView busNo,source,destination,arrival,departure;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            busNo=itemView.findViewById(R.id.tv_Bus_no);
            source=itemView.findViewById(R.id.tv_Source);
            destination=itemView.findViewById(R.id.tv_Destination);
            arrival=itemView.findViewById(R.id.tv_Arrival);
            departure=itemView.findViewById(R.id.tv_Departure);
        }


    }
}
