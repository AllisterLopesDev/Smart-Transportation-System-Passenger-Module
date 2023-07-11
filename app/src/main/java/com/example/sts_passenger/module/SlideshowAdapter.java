package com.example.sts_passenger.module;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sts_passenger.R;

import java.util.List;

public class SlideshowAdapter extends RecyclerView.Adapter<SlideshowAdapter.SlideshowViewHolder> {

    private List<Integer> imageList;

    // Constructor


    public SlideshowAdapter(List<Integer> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public SlideshowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slideshow, parent, false);

        return new SlideshowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideshowViewHolder holder, int position) {
        int imageRes = imageList.get(position);
        holder.imageView.setImageResource(imageRes);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class SlideshowViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public SlideshowViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageview_slideShow);
        }
    }
}
