package com.example.bankingapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CustomerDetailActivity extends AppCompatActivity {

    private TextView tvName, tvAge, tvEmail, tvType, tvKyc, tvBranch, tvServices, tvDateTime, tvMode;
    private Button btnDelete;
    private Customer customer;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        dbHelper = new DatabaseHelper(this);
        customer = (Customer) getIntent().getSerializableExtra("customer");

        tvName = findViewById(R.id.tvDetailName);
        tvAge = findViewById(R.id.tvDetailAge);
        tvEmail = findViewById(R.id.tvDetailEmail);
        tvType = findViewById(R.id.tvDetailType);
        tvKyc = findViewById(R.id.tvDetailKyc);
        tvBranch = findViewById(R.id.tvDetailBranch);
        tvServices = findViewById(R.id.tvDetailServices);
        tvDateTime = findViewById(R.id.tvDetailDateTime);
        tvMode = findViewById(R.id.tvDetailMode);
        btnDelete = findViewById(R.id.btnDeleteCustomer);

        if (customer != null) {
            displayDetails();
        }

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Customer")
                    .setMessage("Are you sure you want to delete this customer?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.deleteCustomer(customer.getId());
                        Toast.makeText(this, "Customer Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void displayDetails() {
        tvName.setText("Name: " + customer.getName());
        tvAge.setText("Age: " + customer.getAge());
        tvEmail.setText("Email: " + customer.getEmail());
        tvType.setText("Account Type: " + customer.getAccountType());
        tvKyc.setText("KYC Verified: " + (customer.isKycVerified() ? "Yes" : "No"));
        tvBranch.setText("Branch: " + customer.getBranch());
        tvServices.setText("Services: " + customer.getServices());
        tvDateTime.setText("Date/Time: " + customer.getOpeningDate() + " " + customer.getOpeningTime());
        tvMode.setText("Account Mode: " + customer.getAccountMode());
    }
}
