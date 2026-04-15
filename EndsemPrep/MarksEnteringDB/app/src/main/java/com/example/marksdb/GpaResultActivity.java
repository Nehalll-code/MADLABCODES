package com.example.marksdb;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GpaResultActivity extends AppCompatActivity {
    private static final String TAG = "GpaResultActivity";
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa_result);

        Toolbar toolbar = findViewById(R.id.toolbarGpa);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);

        String name = getIntent().getStringExtra("STUDENT_NAME");
        double gpa = getIntent().getDoubleExtra("GPA", 0.0);

        TextView tvName = findViewById(R.id.tvResultName);
        TextView tvGpa = findViewById(R.id.tvResultGpa);
        TextView tvTopperName = findViewById(R.id.tvTopperName);
        TextView tvTopperGpa = findViewById(R.id.tvTopperGpa);
        Button btnHome = findViewById(R.id.btnBackToHome);

        tvName.setText("Student Name: " + name);
        tvGpa.setText(String.format("Calculated GPA: %.2f", gpa));

        Log.d(TAG, "Fetching topper details");
        Cursor cursor = dbHelper.getTopper();
        if (cursor != null && cursor.moveToFirst()) {
            String tName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GPA_STUDENT_NAME));
            double tGpa = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GPA_VALUE));
            tvTopperName.setText("Topper Name: " + tName);
            tvTopperGpa.setText(String.format("Topper GPA: %.2f", tGpa));
        }
        if (cursor != null) cursor.close();

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to WelcomeActivity and clear the activity stack
                Intent intent = new Intent(GpaResultActivity.this, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
