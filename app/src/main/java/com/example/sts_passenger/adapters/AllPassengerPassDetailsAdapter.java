package com.example.sts_passenger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.apiservices.response.PassengerPassDetailsResponse;
import com.example.sts_passenger.model.result.PassDetails;

import java.util.List;

public class AllPassengerPassDetailsAdapter extends RecyclerView.Adapter<AllPassengerPassDetailsAdapter.ViewHolder> {

    List<PassDetails> passDetailsList;

    Context context;

    public AllPassengerPassDetailsAdapter(List<PassDetails> passDetailsList, Context context) {
        this.passDetailsList = passDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_pass_details_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.passDestination.setText(passDetailsList.get(position).getDestination());
        holder.passSource.setText(passDetailsList.get(position).getSource());
        holder.passValidFrom.setText(passDetailsList.get(position).getValid_from());
        holder.passValidTo.setText(passDetailsList.get(position).getValid_to());
        holder.passStatus.setText(passDetailsList.get(position).getStatus());
        holder.passPrice.setText(String.valueOf(passDetailsList.get(position).getPrice()));

    }

    @Override
    public int getItemCount() {
        return passDetailsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView passDestination,passId,passPrice,passSource,passStatus,passValidFrom,passValidTo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            passDestination= itemView.findViewById(R.id.tv_pass_destination);
            passSource = itemView.findViewById(R.id.tv_pass_source);
            passValidFrom = itemView.findViewById(R.id.tv_pass_validFrom);
            passValidTo = itemView.findViewById(R.id.tv_pass_validTo);
            passStatus = itemView.findViewById(R.id.tv_pass_status);
            passPrice = itemView.findViewById(R.id.tv_pass_price);

        }
    }
}
