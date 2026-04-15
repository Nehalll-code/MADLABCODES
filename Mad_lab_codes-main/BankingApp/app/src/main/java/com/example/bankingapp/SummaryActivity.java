package com.example.bankingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity {

    private TextView tvName, tvAge, tvEmail, tvType, tvKyc, tvBranch, tvServices, tvDateTime, tvMode;
    private Button btnModify, btnSubmit;
    private Customer customer;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        dbHelper = new DatabaseHelper(this);
        customer = (Customer) getIntent().getSerializableExtra("customer");

        tvName = findViewById(R.id.tvSummaryName);
        tvAge = findViewById(R.id.tvSummaryAge);
        tvEmail = findViewById(R.id.tvSummaryEmail);
        tvType = findViewById(R.id.tvSummaryType);
        tvKyc = findViewById(R.id.tvSummaryKyc);
        tvBranch = findViewById(R.id.tvSummaryBranch);
        tvServices = findViewById(R.id.tvSummaryServices);
        tvDateTime = findViewById(R.id.tvSummaryDateTime);
        tvMode = findViewById(R.id.tvSummaryMode);
        btnModify = findViewById(R.id.btnModify);
        btnSubmit = findViewById(R.id.btnSubmit);

        displayDetails();

        btnModify.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> {
            long id = dbHelper.addCustomer(customer);
            if (id > 0) {
                Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error saving to database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayDetails() {
        tvName.setText("Name: " + customer.getName());
        tvAge.setText("Age: " + customer.getAge());
        tvEmail.setText("Email: " + customer.getEmail());
        tvType.setText("Type: " + customer.getAccountType());
        tvKyc.setText("KYC Verified: " + (customer.isKycVerified() ? "Yes" : "No"));
        tvBranch.setText("Branch: " + customer.getBranch());
        tvServices.setText("Services: " + customer.getServices());
        tvDateTime.setText("Date/Time: " + customer.getOpeningDate() + " " + customer.getOpeningTime());
        tvMode.setText("Mode: " + customer.getAccountMode());
    }
}
