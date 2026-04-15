package com.example.hotelbooking;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BookingDetailActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvRoom, tvBranch, tvAmenities, tvDateTime, tvSlot;
    private Button btnDelete;
    private Booking booking;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        booking = (Booking) getIntent().getSerializableExtra("booking");
        dbHelper = new DatabaseHelper(this);

        tvName = findViewById(R.id.tvDetName);
        tvEmail = findViewById(R.id.tvDetEmail);
        tvRoom = findViewById(R.id.tvDetRoom);
        tvBranch = findViewById(R.id.tvDetBranch);
        tvAmenities = findViewById(R.id.tvDetAmenities);
        tvDateTime = findViewById(R.id.tvDetDateTime);
        tvSlot = findViewById(R.id.tvDetSlot);
        btnDelete = findViewById(R.id.btnDelete);

        if (booking != null) {
            tvName.setText("Name: " + booking.getName());
            tvEmail.setText("Email: " + booking.getEmail());
            tvRoom.setText("Room Type: " + booking.getRoomType());
            tvBranch.setText("Branch: " + booking.getBranch());
            tvAmenities.setText("Amenities: " + booking.getAmenities());
            tvDateTime.setText("Check-in: " + booking.getCheckInDate() + " at " + booking.getCheckInTime());
            tvSlot.setText("Slot: " + booking.getSlot());
        }

        btnDelete.setOnClickListener(v -> {
            dbHelper.deleteBooking(booking.getId());
            Toast.makeText(this, "Booking Deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
