package com.example.management_with_db;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRegistration = findViewById(R.id.btnRegistration);
        Button btnViewList = findViewById(R.id.btnViewList);
        Button btnBookResource = findViewById(R.id.btnBookResource);

        btnRegistration.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        btnViewList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewListActivity.class);
            startActivity(intent);
        });

        btnBookResource.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BookingActivity.class);
            startActivity(intent);
        });
    }
}
