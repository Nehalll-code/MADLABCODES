package com.example.q1;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private LinearLayout registrationForm, confirmationView;
    private Spinner vehicleTypeSpinner;
    private TextInputEditText vehicleNumberET, rcNumberET;
    private TextView displayDetailsTV;
    private MaterialButton submitBtn, confirmBtn, editBtn;

    private String[] vehicleTypes = {"Car", "Bike", "Bus", "Truck", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        registrationForm = findViewById(R.id.registrationForm);
        confirmationView = findViewById(R.id.confirmationView);
        vehicleTypeSpinner = findViewById(R.id.vehicleTypeSpinner);
        vehicleNumberET = findViewById(R.id.vehicleNumberET);
        rcNumberET = findViewById(R.id.rcNumberET);
        displayDetailsTV = findViewById(R.id.displayDetailsTV);
        submitBtn = findViewById(R.id.submitBtn);
        confirmBtn = findViewById(R.id.confirmBtn);
        editBtn = findViewById(R.id.editBtn);

        // Setup Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicleTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleTypeSpinner.setAdapter(adapter);

        // Submit Button Logic
        submitBtn.setOnClickListener(v -> {
            String vehicleType = vehicleTypeSpinner.getSelectedItem().toString();
            String vehicleNumber = vehicleNumberET.getText().toString().trim();
            String rcNumber = rcNumberET.getText().toString().trim();

            if (vehicleNumber.isEmpty() || rcNumber.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String details = "Vehicle Type: " + vehicleType + "\n" +
                             "Vehicle Number: " + vehicleNumber + "\n" +
                             "RC Number: " + rcNumber;

            displayDetailsTV.setText(details);
            registrationForm.setVisibility(View.GONE);
            confirmationView.setVisibility(View.VISIBLE);
        });

        // Edit Button Logic
        editBtn.setOnClickListener(v -> {
            confirmationView.setVisibility(View.GONE);
            registrationForm.setVisibility(View.VISIBLE);
        });

        // Confirm Button Logic
        confirmBtn.setOnClickListener(v -> {
            String serialNumber = "SN-" + (1000 + new Random().nextInt(9000));
            Toast.makeText(MainActivity.this, "Parking Allotted! Serial Number: " + serialNumber, Toast.LENGTH_LONG).show();
            
            // Reset for next registration
            resetForm();
        });
    }

    private void resetForm() {
        vehicleNumberET.setText("");
        rcNumberET.setText("");
        vehicleTypeSpinner.setSelection(0);
        confirmationView.setVisibility(View.GONE);
        registrationForm.setVisibility(View.VISIBLE);
    }
}
