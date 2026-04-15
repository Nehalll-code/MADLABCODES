package com.example.movie;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BookingSummaryActivity extends AppCompatActivity {

    TextView tvSummaryDetails;
    Button btnOptionsMenu, btnBackSummary;
    DatabaseHelper dbHelper;
    String bookingId;
    String category, date, time, ticketType, extras, mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        Toolbar toolbar = findViewById(R.id.toolbar_summary);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);
        tvSummaryDetails = findViewById(R.id.tvSummaryDetails);
        btnOptionsMenu = findViewById(R.id.btnOptionsMenu);
        btnBackSummary = findViewById(R.id.btnBackSummary);

        bookingId = getIntent().getStringExtra("ID");
        loadBookingDetails();

        btnOptionsMenu.setOnClickListener(v -> showPopupMenu());
        btnBackSummary.setOnClickListener(v -> finish());
    }

    private void loadBookingDetails() {
        Cursor cursor = dbHelper.getBookingById(bookingId);
        if (cursor != null && cursor.moveToFirst()) {
            category = cursor.getString(1);
            date = cursor.getString(2);
            time = cursor.getString(3);
            ticketType = cursor.getString(4);
            extras = cursor.getString(5);
            mode = cursor.getString(6);

            String summary = "Category: " + category + "\n" +
                    "Date: " + date + "\n" +
                    "Time: " + time + "\n" +
                    "Ticket Type: " + ticketType + "\n" +
                    "Extras: " + extras + "\n" +
                    "Mode: " + mode;
            tvSummaryDetails.setText(summary);
            cursor.close();
        }
    }

    private void showPopupMenu() {
        PopupMenu popup = new PopupMenu(this, btnOptionsMenu);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.edit_booking) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("ID", bookingId);
                intent.putExtra("CATEGORY", category);
                intent.putExtra("DATE", date);
                intent.putExtra("TIME", time);
                intent.putExtra("TICKET_TYPE", ticketType);
                intent.putExtra("EXTRAS", extras);
                intent.putExtra("MODE", mode);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.cancel_booking) {
                dbHelper.deleteBooking(bookingId);
                Toast.makeText(this, "Booking Cancelled", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            } else if (itemId == R.id.share_booking) {
                Toast.makeText(this, "Sharing: " + tvSummaryDetails.getText().toString(), Toast.LENGTH_LONG).show();
                return true;
            }
            return false;
        });
        popup.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
