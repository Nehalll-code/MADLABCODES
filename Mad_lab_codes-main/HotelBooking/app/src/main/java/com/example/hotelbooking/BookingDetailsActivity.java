package com.example.hotelbooking;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;

public class BookingDetailsActivity extends AppCompatActivity {

    private Spinner spinnerBranch;
    private ListView lvAmenities;
    private TextView tvSelectedDate, tvSelectedTime, tvSelectedSlot;
    private Button btnPickDate, btnPickTime, btnShowSlot, btnNext;
    private Booking booking;

    private String[] branches = {"Bangalore", "Mumbai", "Delhi", "Chennai", "Kolkata"};
    private String[] amenities = {"WiFi", "Swimming Pool", "Breakfast", "Parking", "Gym"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        booking = (Booking) getIntent().getSerializableExtra("booking");

        spinnerBranch = findViewById(R.id.spinnerBranch);
        lvAmenities = findViewById(R.id.lvAmenities);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        tvSelectedSlot = findViewById(R.id.tvSelectedSlot);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnPickTime = findViewById(R.id.btnPickTime);
        btnShowSlot = findViewById(R.id.btnShowSlot);
        btnNext = findViewById(R.id.btnNextBooking);

        ArrayAdapter<String> branchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branches);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBranch.setAdapter(branchAdapter);

        ArrayAdapter<String> amenitiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, amenities);
        lvAmenities.setAdapter(amenitiesAdapter);

        btnPickDate.setOnClickListener(v -> showDatePicker());
        btnPickTime.setOnClickListener(v -> showTimePicker());
        btnShowSlot.setOnClickListener(v -> showPopupMenu());

        btnNext.setOnClickListener(v -> {
            if (validateDetails()) {
                booking.setBranch(spinnerBranch.getSelectedItem().toString());
                
                StringBuilder selectedAmenities = new StringBuilder();
                SparseBooleanArray checked = lvAmenities.getCheckedItemPositions();
                for (int i = 0; i < checked.size(); i++) {
                    int key = checked.keyAt(i);
                    if (checked.get(key)) {
                        selectedAmenities.append(amenities[key]).append(", ");
                    }
                }
                if (selectedAmenities.length() > 0) {
                    selectedAmenities.setLength(selectedAmenities.length() - 2);
                }
                booking.setAmenities(selectedAmenities.toString());
                booking.setCheckInDate(tvSelectedDate.getText().toString());
                booking.setCheckInTime(tvSelectedTime.getText().toString());
                booking.setSlot(tvSelectedSlot.getText().toString().replace("Slot: ", ""));

                Intent intent = new Intent(BookingDetailsActivity.this, SummaryActivity.class);
                intent.putExtra("booking", booking);
                startActivity(intent);
            }
        });
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            tvSelectedDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            tvSelectedTime.setText(String.format("%02d:%02d", hourOfDay, minute));
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void showPopupMenu() {
        PopupMenu popup = new PopupMenu(this, btnShowSlot);
        popup.getMenu().add("Morning");
        popup.getMenu().add("Afternoon");
        popup.getMenu().add("Evening");
        popup.setOnMenuItemClickListener(item -> {
            tvSelectedSlot.setText("Slot: " + item.getTitle());
            return true;
        });
        popup.show();
    }

    private boolean validateDetails() {
        if (tvSelectedDate.getText().toString().equals("Not selected")) {
            Toast.makeText(this, "Please pick a date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tvSelectedTime.getText().toString().equals("Not selected")) {
            Toast.makeText(this, "Please pick a time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tvSelectedSlot.getText().toString().equals("Slot: Not selected")) {
            Toast.makeText(this, "Please select a slot", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
