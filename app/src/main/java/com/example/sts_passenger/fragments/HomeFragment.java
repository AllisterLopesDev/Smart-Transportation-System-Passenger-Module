package com.example.sts_passenger.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.adapters.ValidTicketAdapter;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.response.TicketDetailsResponse;
import com.example.sts_passenger.model.Session;
import com.example.sts_passenger.model.result.TicketBooking;
import com.example.sts_passenger.sharedpref.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    List<TicketBooking> bookingList;

    // SharedPrefManager
    SharedPrefManager sharedPrefManager;
    private Session savedSession;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        cv_ticket = view.findViewById(R.id.cardView_ticket);
//        cv_pass = view.findViewById(R.id.cardView_pass);
//        tv_title = view.findViewById(R.id.tv_ticket_title);
//        tv_busRegNo = view.findViewById(R.id.bus_regNo);
//        tv_busType = view.findViewById(R.id.bus_type);
//        tv_source = view.findViewById(R.id.source);
//        tv_destination = view.findViewById(R.id.destination);
//        getTicketDetails();

        // init sharedPrefManager for passenger session
        setSharedPrefManager();


        recyclerView = view.findViewById(R.id.recycleView_ticketList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        getTicketDetails();
    }

    // SharedPrefManager function
    public void setSharedPrefManager() {
        sharedPrefManager = new SharedPrefManager(requireContext());
        savedSession = sharedPrefManager.getSavedSessionOnLogin();
    }

    private void getTicketDetails() {
        int passengerId = savedSession.getPassenger().getPassengerId();
        Log.i("TAG", "getTicketDetails: passenger-id " + passengerId);
        Call<TicketDetailsResponse> call = Client.getInstance(Consts.BASE_URL_BOOKING).getRoute().getAllTicketDetails(savedSession.getPassenger().getPassengerId());
        call.enqueue(new Callback<TicketDetailsResponse>() {
            @Override
            public void onResponse(Call<TicketDetailsResponse> call, Response<TicketDetailsResponse> response) {
                if (response.isSuccessful()){
                    if (response.body()  != null && response.body().getStatus() == 200){
                        bookingList = response.body().getTicketBookingList();

                        // filter booked tickets
                        List<TicketBooking> filterBookedTickets = new ArrayList<>();
                        for (TicketBooking bookedTicket : bookingList) {
                            if (bookedTicket.getTicket().getStatus().equals("Booked")) {
                                filterBookedTickets.add(bookedTicket);
                            }
                        }

                        // add filtered list to recyclerview
                        recyclerView.setAdapter(new ValidTicketAdapter(filterBookedTickets, getContext()));
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketDetailsResponse> call, Throwable t) {

            }
        });
    }

}