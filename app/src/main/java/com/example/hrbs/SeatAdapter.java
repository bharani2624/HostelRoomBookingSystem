package com.example.hrbs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import androidx.core.content.ContextCompat;
public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {
    private final List<Seat> seatList;
    private final OnSeatClickListener seatClickListener;

    // Constructor
    public SeatAdapter(List<Seat> seatList, OnSeatClickListener seatClickListener) {
        this.seatList = seatList;
        this.seatClickListener = seatClickListener;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each seat
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seatList.get(position);
        holder.seatNumber.setText(seat.getSeatNumber());

        // Set background color based on seat availability and selection
        if (!seat.isAvailable()) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_light)); // Unavailable seat
        } else if (seat.isSelected()) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_green_light)); // Selected seat
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.darker_gray)); // Available seat
        }

        // Handle seat click to toggle selection
        holder.itemView.setOnClickListener(v -> {
            if (seat.isAvailable()) {
                seat.setSelected(!seat.isSelected());
                seatClickListener.onSeatClick(seat);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    // ViewHolder class to bind seat view
    public static class SeatViewHolder extends RecyclerView.ViewHolder {
        TextView seatNumber;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            seatNumber = itemView.findViewById(R.id.seatNumber);
        }
    }

    // Interface to handle seat clicks
    public interface OnSeatClickListener {
        void onSeatClick(Seat seat);
    }
}
