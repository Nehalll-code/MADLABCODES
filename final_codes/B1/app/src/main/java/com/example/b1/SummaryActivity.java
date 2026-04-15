package com.example.b1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    TextView tvSummary;
    Button btnEdit, btnDelete, btnBack;
    ToggleButton tbDeleteEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        myDb = new DatabaseHelper(this);
        tvSummary = findViewById(R.id.tvSummary);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);
        tbDeleteEnable = findViewById(R.id.tbDeleteEnable);

        displayAllData();

        btnEdit.setOnClickListener(v -> editLatestRecord());

        btnDelete.setOnClickListener(v -> {
            // Check if ToggleButton is enabled
            if (tbDeleteEnable.isChecked()) {
                deleteLatestRecord();
            } else {
                Toast.makeText(SummaryActivity.this, "Please enable deletion toggle first", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void displayAllData() {
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            tvSummary.setText("No Admission Records Found");
            return;
        }

        StringBuilder buffer = new StringBuilder();
        while (res.moveToNext()) {
            buffer.append("ID: ").append(res.getString(0)).append("\n");
            buffer.append("Student Name: ").append(res.getString(1)).append("\n");
            buffer.append("Marks: ").append(res.getString(2)).append("\n");
            buffer.append("Gender: ").append(res.getString(3)).append("\n");
            buffer.append("Course: ").append(res.getString(4)).append("\n");
            buffer.append("Admission Date: ").append(res.getString(5)).append("\n");
            buffer.append("----------------------------\n\n");
        }
        tvSummary.setText(buffer.toString());
    }

    private void editLatestRecord() {
        Cursor res = myDb.getLatestData();
        if (res.getCount() == 0) {
            Toast.makeText(this, "No records to edit", Toast.LENGTH_SHORT).show();
            return;
        }

        if (res.moveToFirst()) {
            Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
            intent.putExtra("ID", res.getString(0));
            intent.putExtra("NAME", res.getString(1));
            intent.putExtra("MARKS", res.getString(2));
            intent.putExtra("GENDER", res.getString(3));
            intent.putExtra("COURSE", res.getString(4));
            intent.putExtra("DATE", res.getString(5));
            startActivity(intent);
            finish();
        }
    }

    private void deleteLatestRecord() {
        Cursor res = myDb.getLatestData();
        if (res.getCount() == 0) {
            Toast.makeText(this, "No records to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        if (res.moveToFirst()) {
            String id = res.getString(0);
            Integer deletedRows = myDb.deleteData(id);
            if (deletedRows > 0) {
                Toast.makeText(this, "Admission Record Deleted", Toast.LENGTH_SHORT).show();
                displayAllData(); 
            } else {
                Toast.makeText(this, "Deletion Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
