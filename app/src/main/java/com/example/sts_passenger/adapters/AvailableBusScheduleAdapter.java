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

import java.util.List;


public class AvailableBusScheduleAdapter extends RecyclerView.Adapter<AvailableBusScheduleAdapter.ViewHolder> {

    // instances
    List<BusScheduleResult> busScheduleResults;
    Context context;


    public AvailableBusScheduleAdapter(List<BusScheduleResult> busScheduleResults, Context context) {
        this.busScheduleResults = busScheduleResults;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.available_bus_schedule_item,
                        parent,
                        false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvBusRegNum.setText(busScheduleResults.get(position).getBus().getRegNo());
        holder.tvBusType.setText(busScheduleResults.get(position).getBus().getType());
    }

    @Override
    public int getItemCount() {
        return busScheduleResults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Views
        TextView tvBusRegNum, tvBusType;
        CardView cardViewBusScheduleItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialize views
            initViews(itemView);
        }

        public void initViews(View view) {
            cardViewBusScheduleItem = view.findViewById(R.id.cardView_bus_schedule);
            tvBusRegNum = view.findViewById(R.id.textView_bus_reg_no);
            tvBusType = view.findViewById(R.id.textView_bus_type);
        }
    }
}

