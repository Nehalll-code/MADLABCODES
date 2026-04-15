package com.example.hotelbooking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Booking booking);
    }

    public BookingAdapter(List<Booking> bookingList, OnItemClickListener listener) {
        this.bookingList = bookingList;
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
        Booking booking = bookingList.get(position);
        holder.tvItemName.setText(booking.getName());
        holder.tvItemRoomType.setText(booking.getRoomType());
        holder.tvItemBranch.setText(booking.getBranch());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(booking));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemRoomType, tvItemBranch;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemRoomType = itemView.findViewById(R.id.tvItemRoomType);
            tvItemBranch = itemView.findViewById(R.id.tvItemBranch);
        }
    }
}
