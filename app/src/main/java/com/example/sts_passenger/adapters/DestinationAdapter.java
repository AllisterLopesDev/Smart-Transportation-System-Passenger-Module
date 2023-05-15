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

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {
    List<Halts> destinationList;
    Context context;

    // Field for the listener
    public OnItemClickListener itemClickListener;

    public DestinationAdapter(Context context, List<Halts> destinationList, OnItemClickListener listener) {
        this.context = context;
        this.destinationList = destinationList;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.destination_item,
                        parent,
                        false);

        return new ViewHolder(view, itemClickListener, destinationList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvDestinationName.setText(destinationList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // use getAdapterPosition instead of position everytime
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                    Halts clickedDestination = destinationList.get(position);
                    String destinationName = clickedDestination.getName();
                    int destinationId = clickedDestination.getId();
                    itemClickListener.onItemClick(destinationId, destinationName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Views
        TextView tvDestinationName;

        ViewHolder(@NonNull View itemView, final OnItemClickListener listener, final List<Halts> destinationList) {
            super(itemView);

            // init the views
            tvDestinationName = itemView.findViewById(R.id.textView_list_destination_name);
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Integer destinationId, String destinationName);
    }
}
