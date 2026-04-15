package com.example.megacombined;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookings;
    private OnDeleteClickListener listener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Booking booking);
    }

    public BookingAdapter(List<Booking> bookings, OnDeleteClickListener listener) {
        this.bookings = bookings;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.tvMain.setText(booking.getName() + " - " + booking.getMainInfo());
        holder.tvDate.setText(booking.getDate());
        holder.tvSub.setText(booking.getType() + " - " + booking.getExtraInfo());
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(booking));
    }

    @Override
    public int getItemCount() { return bookings.size(); }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvMain, tvDate, tvSub;
        Button btnDelete;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMain = itemView.findViewById(R.id.tvMainInfo);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSub = itemView.findViewById(R.id.tvSubInfo);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
