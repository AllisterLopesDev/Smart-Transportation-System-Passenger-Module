package com.example.sts_passenger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.model.TripHistory;

import java.util.List;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.ViewHolder> {
    List<TripHistory> tripHistoryList;
    Context context;

    public TripHistoryAdapter(List<TripHistory> tripHistoryList, Context context) {
        this.tripHistoryList = tripHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.trip_history_items,
                        parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.busRegNumber.setText();
//        holder.source.setText();

    }

    @Override
    public int getItemCount() {
        return tripHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

TextView busRegNumber,busType, source,destnation, tv_date, seatCount, fare;

       public ViewHolder(@NonNull View itemView) {
           super(itemView);

           busRegNumber = itemView.findViewById(R.id.textView_bus_reg_no);
           busType = itemView.findViewById(R.id.textView_bus_type);
           source = itemView.findViewById(R.id.textView_source);
           destnation = itemView.findViewById(R.id.textView_destination);
           tv_date = itemView.findViewById(R.id.textView_date);
           seatCount = itemView.findViewById(R.id.tv_seats_booked);
           fare = itemView.findViewById(R.id.textView_fare);
       }
   }
}
