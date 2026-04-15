package com.example.management_with_db;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

/**
 * RegistrationActivity handles input for creating or editing a resource.
 */
public class RegistrationActivity extends AppCompatActivity {

    private EditText etResourceName;
    private Spinner spAvailability;
    private TextView tvSelectedDate;
    private String selectedDate = "";
    
    private int resourceId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etResourceName = findViewById(R.id.etResourceName);
        spAvailability = findViewById(R.id.spAvailability);
        Button btnPickDate = findViewById(R.id.btnPickDate);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        Button btnNext = findViewById(R.id.btnNext);

        // Populate Spinner (1 to 10)
        Integer[] items = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAvailability.setAdapter(adapter);

        // Check if we are in Edit Mode
        Intent intent = getIntent();
        if (intent.hasExtra("isEditMode") && intent.getBooleanExtra("isEditMode", false)) {
            isEditMode = true;
            resourceId = intent.getIntExtra("id", -1);
            etResourceName.setText(intent.getStringExtra("name"));
            selectedDate = intent.getStringExtra("date");
            tvSelectedDate.setText(selectedDate);
            
            int availability = intent.getIntExtra("availability", 1);
            spAvailability.setSelection(availability - 1);
            
            setTitle("Edit Resource");
            btnNext.setText("Go to Summary");
        } else if (intent.hasExtra("from_summary")) {
            // Coming back from SummaryActivity to modify
            resourceId = intent.getIntExtra("id", -1);
            isEditMode = intent.getBooleanExtra("isEditMode", false);
            etResourceName.setText(intent.getStringExtra("name"));
            selectedDate = intent.getStringExtra("date");
            tvSelectedDate.setText(selectedDate);
            int availability = intent.getIntExtra("availability", 1);
            spAvailability.setSelection(availability - 1);
        }

        btnPickDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrationActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                        tvSelectedDate.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        btnNext.setOnClickListener(v -> {
            String name = etResourceName.getText().toString().trim();
            if (name.isEmpty() || selectedDate.isEmpty()) {
                Toast.makeText(RegistrationActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                return;
            }

            int availability = (int) spAvailability.getSelectedItem();

            Intent summaryIntent = new Intent(RegistrationActivity.this, SummaryActivity.class);
            summaryIntent.putExtra("id", resourceId);
            summaryIntent.putExtra("name", name);
            summaryIntent.putExtra("availability", availability);
            summaryIntent.putExtra("date", selectedDate);
            summaryIntent.putExtra("isEditMode", isEditMode);
            startActivity(summaryIntent);
        });
    }
}
