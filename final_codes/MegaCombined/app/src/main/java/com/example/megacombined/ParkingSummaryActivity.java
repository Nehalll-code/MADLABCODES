package com.example.megacombined;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ParkingSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        TextView tvSummary = findViewById(R.id.tvSummaryText);
        Button btnConfirm = findViewById(R.id.btnConfirm);
        Button btnEdit = findViewById(R.id.btnEdit);

        Bundle extras = getIntent().getExtras();
        if (extras == null) return;

        String name = extras.getString("name");
        String email = extras.getString("email");
        int age = extras.getInt("age");
        String vehicleType = extras.getString("vehicleType");
        boolean active = extras.getBoolean("active");
        String area = extras.getString("area");
        String options = extras.getString("options");
        String date = extras.getString("date");
        String time = extras.getString("time");
        String shift = extras.getString("shift");

        String summary = "Name: " + name + "\n" +
                "Email: " + email + "\n" +
                "Age: " + age + "\n" +
                "Vehicle: " + vehicleType + "\n" +
                "Active: " + (active ? "Yes" : "No") + "\n" +
                "Area: " + area + "\n" +
                "Options: " + options + "\n" +
                "Date: " + date + "\n" +
                "Time: " + time + "\n" +
                "Shift: " + shift;

        tvSummary.setText(summary);

        btnConfirm.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(this);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COL_NAME, name);
            cv.put(DatabaseHelper.COL_AGE, age);
            cv.put(DatabaseHelper.COL_EMAIL, email);
            cv.put(DatabaseHelper.COL_P_V_TYPE, vehicleType);
            cv.put(DatabaseHelper.COL_P_ACTIVE, active ? 1 : 0);
            cv.put(DatabaseHelper.COL_P_AREA, area);
            cv.put(DatabaseHelper.COL_P_OPTIONS, options);
            cv.put(DatabaseHelper.COL_DATE, date);
            cv.put(DatabaseHelper.COL_TIME, time);
            cv.put(DatabaseHelper.COL_P_SHIFT, shift);

            if (db.insertParkingBooking(cv) != -1) {
                Toast.makeText(this, "Parking Slot Booked!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error in Booking", Toast.LENGTH_SHORT).show();
            }
        });

        btnEdit.setOnClickListener(v -> finish());
    }
}
