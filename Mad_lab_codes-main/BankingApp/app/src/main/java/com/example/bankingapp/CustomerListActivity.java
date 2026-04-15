package com.example.bankingapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CustomerListActivity extends AppCompatActivity {

    private RecyclerView rvCustomers;
    private CustomerAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        dbHelper = new DatabaseHelper(this);
        rvCustomers = findViewById(R.id.rvCustomers);
        rvCustomers.setLayoutManager(new LinearLayoutManager(this));

        loadCustomers();
    }

    private void loadCustomers() {
        List<Customer> customers = dbHelper.getAllCustomers();
        adapter = new CustomerAdapter(this, customers, customer -> {
            Intent intent = new Intent(CustomerListActivity.this, CustomerDetailActivity.class);
            intent.putExtra("customer", customer);
            startActivity(intent);
        });
        rvCustomers.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCustomers();
    }
}
