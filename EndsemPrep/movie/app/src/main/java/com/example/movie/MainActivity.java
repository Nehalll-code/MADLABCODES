package com.example.movie;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerCategory;
    Button btnPickDate, btnPickTime, btnSubmit, btnViewBookings;
    TextView tvSelectedDate, tvSelectedTime;
    RadioGroup radioGroupTicket;
    CheckBox cbSnacks, cbParking, cb3DGlasses;
    ToggleButton toggleBookingMode;
    DatabaseHelper dbHelper;

    String selectedDate = "", selectedTime = "";
    String bookingId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnPickTime = findViewById(R.id.btnPickTime);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnViewBookings = findViewById(R.id.btnViewBookings);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        radioGroupTicket = findViewById(R.id.radioGroupTicket);
        cbSnacks = findViewById(R.id.cbSnacks);
        cbParking = findViewById(R.id.cbParking);
        cb3DGlasses = findViewById(R.id.cb3DGlasses);
        toggleBookingMode = findViewById(R.id.toggleBookingMode);

        btnPickDate.setOnClickListener(v -> showDatePicker());
        btnPickTime.setOnClickListener(v -> showTimePicker());

        btnSubmit.setOnClickListener(v -> saveBooking());
        btnViewBookings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BookingListActivity.class);
            startActivity(intent);
        });

        checkIntentExtras();
    }

    private void checkIntentExtras() {
        Intent intent = getIntent();
        if (intent.hasExtra("ID")) {
            bookingId = intent.getStringExtra("ID");
            String category = intent.getStringExtra("CATEGORY");
            selectedDate = intent.getStringExtra("DATE");
            selectedTime = intent.getStringExtra("TIME");
            String ticketType = intent.getStringExtra("TICKET_TYPE");
            String extras = intent.getStringExtra("EXTRAS");
            String mode = intent.getStringExtra("MODE");

            // Pre-fill fields
            ArrayAdapter adapter = (ArrayAdapter) spinnerCategory.getAdapter();
            if (adapter != null) {
                int spinnerPosition = adapter.getPosition(category);
                spinnerCategory.setSelection(spinnerPosition);
            }

            tvSelectedDate.setText(selectedDate);
            tvSelectedTime.setText(selectedTime);

            if ("Regular".equals(ticketType)) {
                radioGroupTicket.check(R.id.rbRegular);
            } else if ("VIP".equals(ticketType)) {
                radioGroupTicket.check(R.id.rbVIP);
            }

            if (extras != null) {
                cbSnacks.setChecked(extras.contains("Snacks"));
                cbParking.setChecked(extras.contains("Parking"));
                cb3DGlasses.setChecked(extras.contains("3D Glasses"));
            }

            toggleBookingMode.setChecked("Online".equals(mode));
            btnSubmit.setText("Update Booking");
        }
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
            tvSelectedDate.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        c.add(Calendar.DAY_OF_MONTH, 7);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showTimePicker() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            if (hourOfDay >= 9 && hourOfDay <= 23) {
                if (hourOfDay == 23 && minute1 > 0) {
                    Toast.makeText(MainActivity.this, "Select time between 9 AM and 11 PM", Toast.LENGTH_SHORT).show();
                } else {
                    selectedTime = hourOfDay + ":" + String.format("%02d", minute1);
                    tvSelectedTime.setText(selectedTime);
                }
            } else {
                Toast.makeText(MainActivity.this, "Select time between 9 AM and 11 PM", Toast.LENGTH_SHORT).show();
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void saveBooking() {
        String category = spinnerCategory.getSelectedItem().toString();
        if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = radioGroupTicket.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select ticket type", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton radioButton = findViewById(selectedId);
        String ticketType = radioButton.getText().toString();

        StringBuilder extras = new StringBuilder();
        if (cbSnacks.isChecked()) extras.append("Snacks ");
        if (cbParking.isChecked()) extras.append("Parking ");
        if (cb3DGlasses.isChecked()) extras.append("3D Glasses ");

        String mode = toggleBookingMode.isChecked() ? "Online" : "Offline";

        if (bookingId == null) {
            long result = dbHelper.insertBooking(category, selectedDate, selectedTime, ticketType, extras.toString().trim(), mode);
            if (result != -1) {
                Toast.makeText(this, "Booking Successful", Toast.LENGTH_SHORT).show();
                navigateToSummary(String.valueOf(result));
            }
        } else {
            boolean result = dbHelper.updateBooking(bookingId, category, selectedDate, selectedTime, ticketType, extras.toString().trim(), mode);
            if (result) {
                Toast.makeText(this, "Booking Updated", Toast.LENGTH_SHORT).show();
                navigateToSummary(bookingId);
            }
        }
    }

    private void navigateToSummary(String id) {
        Intent intent = new Intent(MainActivity.this, BookingSummaryActivity.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_view_bookings) {
            startActivity(new Intent(this, BookingListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
