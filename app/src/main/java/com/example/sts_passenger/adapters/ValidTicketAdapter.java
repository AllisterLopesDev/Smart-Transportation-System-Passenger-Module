package com.example.sts_passenger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.assets.QRCodeGenerator;
import com.example.sts_passenger.model.result.TicketBooking;
import com.example.sts_passenger.model.result.TicketResult;

import java.util.List;

public class ValidTicketAdapter extends RecyclerView.Adapter<ValidTicketAdapter.ViewHolder> {
    List<TicketResult> ticketResultList;
    Context context;

    public ValidTicketAdapter(List<TicketResult> bookings, Context context) {
        this.ticketResultList = bookings;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.home_ticket_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_busRegNo.setText(ticketResultList.get(position).getBus().getRegistrationNumber());
        holder.tv_busType.setText(ticketResultList.get(position).getBus().getType());
        holder.tv_source.setText(ticketResultList.get(position).getTicket().getSource());
        holder.tv_destination.setText(ticketResultList.get(position).getTicket().getDestination());
        String data = String.valueOf(ticketResultList.get(position).getTicket().getId());

        holder.ticket_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//        QRCodeGenerator.generateQRCode(data, holder.imageViewQR);
    }

    @Override
    public int getItemCount() {
        if (ticketResultList == null) {
            return 0;
        }
        return ticketResultList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //    CardView cv_ticket,cv_pass;
    TextView tv_title,tv_busRegNo,tv_busType,tv_source, tv_destination;
    AppCompatButton ticket_complete;
        ImageView imageViewQR;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//        tv_title = itemView.findViewById(R.id.tv_ticket_title);
        tv_busRegNo = itemView.findViewById(R.id.bus_regNo);
        tv_busType = itemView.findViewById(R.id.bus_type);
        tv_source = itemView.findViewById(R.id.source);
        tv_destination = itemView.findViewById(R.id.destination);
//        imageViewQR = itemView.findViewById(R.id.ticketQr);
            ticket_complete = itemView.findViewById(R.id.ticket_complete_btn);



        }
        public void ticketComplete(){

        }
    }
}
