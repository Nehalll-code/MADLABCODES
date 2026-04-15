package com.example.management_with_db;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class BookingActivity extends AppCompatActivity {

    private Spinner spResourceSelect;
    private SeekBar sbQuantity;
    private TextView tvSelectedQuantity;
    private DatabaseHelper dbHelper;
    private int requiredQuantity = 1;
    private List<DatabaseHelper.Resource> resourceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        dbHelper = DatabaseHelper.getInstance(this);

        spResourceSelect = findViewById(R.id.spResourceSelect);
        sbQuantity = findViewById(R.id.sbQuantity);
        tvSelectedQuantity = findViewById(R.id.tvSelectedQuantity);
        Button btnBook = findViewById(R.id.btnBook);

        loadResources();

        sbQuantity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                requiredQuantity = progress + 1;
                tvSelectedQuantity.setText("Required: " + requiredQuantity);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnBook.setOnClickListener(v -> {
            DatabaseHelper.Resource selected = (DatabaseHelper.Resource) spResourceSelect.getSelectedItem();
            if (selected == null) {
                Toast.makeText(this, "No resource selected", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selected.availability >= requiredQuantity) {
                int newCount = selected.availability - requiredQuantity;
                if (dbHelper.updateAvailability(selected.id, newCount)) {
                    Toast.makeText(this, "Booking Successful!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "Insufficient resources available!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadResources() {
        resourceList = dbHelper.getAllResources();
        ArrayAdapter<DatabaseHelper.Resource> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, resourceList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spResourceSelect.setAdapter(adapter);
    }
}
