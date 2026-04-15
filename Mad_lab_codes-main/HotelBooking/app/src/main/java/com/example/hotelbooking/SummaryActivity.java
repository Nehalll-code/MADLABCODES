package com.example.hotelbooking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity {

    private TextView tvName, tvAge, tvEmail, tvRoom, tvBranch, tvAmenities, tvDateTime, tvSlot;
    private Button btnModify, btnSubmit;
    private Booking booking;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        booking = (Booking) getIntent().getSerializableExtra("booking");
        dbHelper = new DatabaseHelper(this);

        tvName = findViewById(R.id.tvSumName);
        tvAge = findViewById(R.id.tvSumAge);
        tvEmail = findViewById(R.id.tvSumEmail);
        tvRoom = findViewById(R.id.tvSumRoom);
        tvBranch = findViewById(R.id.tvSumBranch);
        tvAmenities = findViewById(R.id.tvSumAmenities);
        tvDateTime = findViewById(R.id.tvSumDateTime);
        tvSlot = findViewById(R.id.tvSumSlot);
        btnModify = findViewById(R.id.btnModify);
        btnSubmit = findViewById(R.id.btnSubmit);

        displaySummary();

        btnModify.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> {
            long id = dbHelper.addBooking(booking);
            if (id != -1) {
                Toast.makeText(this, "Booking Successful!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Booking Failed. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displaySummary() {
        tvName.setText("Name: " + booking.getName());
        tvAge.setText("Age: " + booking.getAge());
        tvEmail.setText("Email: " + booking.getEmail());
        tvRoom.setText("Room Type: " + booking.getRoomType());
        tvBranch.setText("Branch: " + booking.getBranch());
        tvAmenities.setText("Amenities: " + booking.getAmenities());
        tvDateTime.setText("Check-in: " + booking.getCheckInDate() + " at " + booking.getCheckInTime());
        tvSlot.setText("Slot: " + booking.getSlot());
    }
}
