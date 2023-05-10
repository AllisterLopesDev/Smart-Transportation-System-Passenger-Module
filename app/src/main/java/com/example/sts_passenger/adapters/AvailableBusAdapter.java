package com.example.sts_passenger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.searchbuses.Results;

import java.util.List;

public class AvailableBusAdapter extends RecyclerView.Adapter<AvailableBusAdapter.ViewHolder> {

    List<Results> busResultsList;
    Context context;

    public AvailableBusAdapter(List<Results> busResultsList, Context context) {
        this.busResultsList = busResultsList;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.search_available_bus_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.tvBusType.setText(busResultsList.get(position).getBusList().);
//        holder.tvBusRegNo.setText(busResultsList.get(position).getBusList().get(position).getRegNo());
    }

    @Override
    public int getItemCount() {
        return busResultsList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBusRegNo, tvBusType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBusRegNo = itemView.findViewById(R.id.tv_regNo);
            tvBusType = itemView.findViewById(R.id.tv_type);
        }
    }
}
