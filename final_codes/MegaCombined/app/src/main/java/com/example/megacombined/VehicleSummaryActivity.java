package com.example.megacombined;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class VehicleSummaryActivity extends AppCompatActivity {

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
        String serviceType = extras.getString("serviceType");
        boolean pickup = extras.getBoolean("pickup");
        String center = extras.getString("center");
        String issues = extras.getString("issues");
        String date = extras.getString("date");
        String time = extras.getString("time");
        String slot = extras.getString("slot");

        String summary = "Name: " + name + "\n" +
                "Email: " + email + "\n" +
                "Age: " + age + "\n" +
                "Service: " + serviceType + "\n" +
                "Pickup: " + (pickup ? "Yes" : "No") + "\n" +
                "Center: " + center + "\n" +
                "Issues: " + issues + "\n" +
                "Date: " + date + "\n" +
                "Time: " + time + "\n" +
                "Slot: " + slot;

        tvSummary.setText(summary);

        btnConfirm.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(this);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COL_NAME, name);
            cv.put(DatabaseHelper.COL_AGE, age);
            cv.put(DatabaseHelper.COL_EMAIL, email);
            cv.put(DatabaseHelper.COL_V_TYPE, serviceType);
            cv.put(DatabaseHelper.COL_V_PICKUP, pickup ? 1 : 0);
            cv.put(DatabaseHelper.COL_V_CENTER, center);
            cv.put(DatabaseHelper.COL_V_ISSUES, issues);
            cv.put(DatabaseHelper.COL_DATE, date);
            cv.put(DatabaseHelper.COL_TIME, time);
            cv.put(DatabaseHelper.COL_V_SLOT, slot);

            if (db.insertVehicleBooking(cv) != -1) {
                Toast.makeText(this, "Booking Successful!", Toast.LENGTH_LONG).show();
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
