package com.example.hotelbooking;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookingListActivity extends AppCompatActivity {

    private RecyclerView rvBookings;
    private DatabaseHelper dbHelper;
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        rvBookings = findViewById(R.id.rvBookings);
        dbHelper = new DatabaseHelper(this);

        rvBookings.setLayoutManager(new LinearLayoutManager(this));
        loadBookings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookings();
    }

    private void loadBookings() {
        List<Booking> bookingList = dbHelper.getAllBookings();
        adapter = new BookingAdapter(bookingList, booking -> {
            Intent intent = new Intent(BookingListActivity.this, BookingDetailActivity.class);
            intent.putExtra("booking", booking);
            startActivity(intent);
        });
        rvBookings.setAdapter(adapter);
    }
}
