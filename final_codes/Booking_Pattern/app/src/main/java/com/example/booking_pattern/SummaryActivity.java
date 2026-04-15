package com.example.booking_pattern;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity {

    private TextView tvSummary;
    private BookingRecord currentRecord;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        dbHelper = DatabaseHelper.getInstance(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvSummary = findViewById(R.id.tvSummary);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnConfirm = findViewById(R.id.btnConfirm);

        registerForContextMenu(tvSummary);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        int age = intent.getIntExtra("age", 0);
        String email = intent.getStringExtra("email");
        String category = intent.getStringExtra("category");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String type = intent.getStringExtra("type");
        String additionalOption = intent.getStringExtra("additionalOption");

        currentRecord = new BookingRecord(name, age, email, category, date, time, type, additionalOption);

        String summary = "Name: " + name + "\n" +
                "Age: " + age + "\n" +
                "Email: " + email + "\n" +
                "Category: " + category + "\n" +
                "Date: " + date + "\n" +
                "Time: " + time + "\n" +
                "Type: " + type + "\n" +
                "Notification: " + additionalOption;

        tvSummary.setText(summary);

        btnBack.setOnClickListener(v -> finish());

        btnConfirm.setOnClickListener(v -> {
            long result = dbHelper.addBooking(currentRecord);
            if (result != -1) {
                Toast.makeText(SummaryActivity.this, "Booking Saved!", Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(SummaryActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
            } else {
                Toast.makeText(SummaryActivity.this, "Error saving booking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Text Options");
        menu.add(0, 1, 0, "Copy Summary");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) {
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
