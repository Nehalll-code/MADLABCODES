package com.example.management_with_db;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * SummaryActivity displays the data before final submission or update.
 * Acts as a confirmation step for CRUD operations.
 */
public class SummaryActivity extends AppCompatActivity {

    private TextView tvSummaryName, tvSummaryAvailability, tvSummaryDate;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // FIX: Use the Singleton instance to prevent "database closed" errors
        dbHelper = DatabaseHelper.getInstance(this);

        tvSummaryName = findViewById(R.id.tvSummaryName);
        tvSummaryAvailability = findViewById(R.id.tvSummaryAvailability);
        tvSummaryDate = findViewById(R.id.tvSummaryDate);
        Button btnModify = findViewById(R.id.btnModify);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        // Retrieve data from Intent
        final int id = getIntent().getIntExtra("id", -1);
        final String name = getIntent().getStringExtra("name");
        final int availability = getIntent().getIntExtra("availability", 1);
        final String date = getIntent().getStringExtra("date");
        final boolean isEditMode = getIntent().getBooleanExtra("isEditMode", false);

        // Display data
        tvSummaryName.setText("Name: " + (name != null ? name : "N/A"));
        tvSummaryAvailability.setText("Availability: " + availability);
        tvSummaryDate.setText("Date: " + (date != null ? date : "N/A"));

        if (isEditMode) {
            btnSubmit.setText("Confirm Update");
            setTitle("Update Summary");
        } else {
            btnSubmit.setText("Confirm Registration");
            setTitle("New Resource Summary");
        }

        btnModify.setOnClickListener(v -> {
            // Go back to RegistrationActivity with current data
            Intent intent = new Intent(SummaryActivity.this, RegistrationActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("name", name);
            intent.putExtra("availability", availability);
            intent.putExtra("date", date);
            intent.putExtra("isEditMode", isEditMode);
            intent.putExtra("from_summary", true);
            startActivity(intent);
            finish();
        });

        btnSubmit.setOnClickListener(v -> {
            if (isEditMode) {
                // Perform Update using the singleton dbHelper
                if (dbHelper.updateResource(id, name, availability, date)) {
                    Toast.makeText(this, "Resource Updated Successfully", Toast.LENGTH_SHORT).show();
                    returnToMain();
                } else {
                    Toast.makeText(this, "Failed to update resource", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Perform Create using the singleton dbHelper
                long newId = dbHelper.addResource(name, availability, date);
                if (newId != -1) {
                    Toast.makeText(this, "Resource Registered Successfully", Toast.LENGTH_SHORT).show();
                    returnToMain();
                } else {
                    Toast.makeText(this, "Error: Could not save resource", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void returnToMain() {
        Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
