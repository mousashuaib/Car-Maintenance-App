package com.example.car_main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.car_main.Booking;

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingViewHolder> {

    private List<Booking> bookingList;

    public BookingsAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @Override
    public BookingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.tvPartName.setText(booking.getPartName());
        holder.tvRepairDate.setText(booking.getRepairDate());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvPartName, tvRepairDate;

        BookingViewHolder(View itemView) {
            super(itemView);
            tvPartName = itemView.findViewById(android.R.id.text1);
            tvRepairDate = itemView.findViewById(android.R.id.text2);
        }
    }
}
