package com.example.sts_passenger.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sts_passenger.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PassFragment extends Fragment {

    FloatingActionButton createPassBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createPassBtn = view.findViewById(R.id.create_pass);



        createPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneratePass frag = new GeneratePass();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout_pass_container, frag);
                transaction.commit();
                Toast.makeText(requireContext(), "hello", Toast.LENGTH_LONG).show();
                hideViewsOnFragTransaction();
            }
        });
    }

    // function to hide views on fragment call
    private void hideViewsOnFragTransaction() {
        createPassBtn.setVisibility(View.GONE);
    }
}