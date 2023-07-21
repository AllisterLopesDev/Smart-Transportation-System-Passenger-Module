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

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.ViewHolder> {

    List<Halts> sourceBusStopsList;
    Context context;

    // onItemClickListener instance
    public OnSourceItemClickListener onItemClickListener;


    public SourceAdapter(List<Halts> sourceBusStopsList, Context context, OnSourceItemClickListener onItemClickListener) {
        this.sourceBusStopsList = sourceBusStopsList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.source_prebooking_item, parent, false);

        return new ViewHolder(view, sourceBusStopsList, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvBusStopName.setText(sourceBusStopsList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    Halts clickedBusStop = sourceBusStopsList.get(pos);
                    int busStopId = clickedBusStop.getId();
                    String busStopName = clickedBusStop.getName();
                    onItemClickListener.onItemClick(busStopId, busStopName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sourceBusStopsList.size();
    }


    // ViewHolder inner class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBusStopName;

        public ViewHolder(@NonNull View itemView, final List<Halts> destinationBusStopsList, final OnSourceItemClickListener listener) {
            super(itemView);

            tvBusStopName = itemView.findViewById(R.id.textView_list_prebooking_source_name);
        }
    }


    // onItemClick listener interface for destination item clicked
    public interface OnSourceItemClickListener {
        void onItemClick(Integer sourceBusStopId, String sourceBusStopName);
    }
}

