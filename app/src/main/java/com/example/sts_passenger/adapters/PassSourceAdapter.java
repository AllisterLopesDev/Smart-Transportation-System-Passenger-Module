package com.example.sts_passenger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.model.RouteInfo;

import java.util.List;

public class PassSourceAdapter extends RecyclerView.Adapter<PassSourceAdapter.ViewHolder> {
    List<RouteInfo> routeInfoList;
    Context context;

    OnPassRouteInfoClickListener onPassRouteInfoClickListener;

    public PassSourceAdapter(List<RouteInfo> routeInfoList, Context context, OnPassRouteInfoClickListener onPassRouteInfoClickListener) {
        this.routeInfoList = routeInfoList;
        this.context = context;
        this.onPassRouteInfoClickListener = onPassRouteInfoClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.search_pass_source_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String sourceName = routeInfoList.get(position).getSource().getName();
        String destinationName = routeInfoList.get(position).getDestination().getName();
        String routeName = sourceName + " to " + destinationName;
        holder.tvRouteInfoData.setText(routeName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION && onPassRouteInfoClickListener != null){
                    RouteInfo selectedRouteInfo = routeInfoList.get(pos);
                    // route-info-id
                    Integer routeId = selectedRouteInfo.getId();
                    // route-info-name
                    String sourceName = selectedRouteInfo.getSource().getName();
                    String destinationName = selectedRouteInfo.getDestination().getName();
                    String routeName = sourceName + " " + destinationName;
                    // route-info fare
                    String fare = selectedRouteInfo.getFare();
                    // route-info distance
                    String distance = selectedRouteInfo.getDistance();
                    onPassRouteInfoClickListener.onClickListener(routeId, routeName, fare, distance);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return routeInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvRouteInfoData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRouteInfoData = itemView.findViewById(R.id.textView_halt);
        }
    }

    public interface OnPassRouteInfoClickListener {
        void onClickListener(Integer routeId,String haltName, String fare, String distance);
    }
}
