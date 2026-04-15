package com.example.megacombined;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CourseSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        TextView tvSummary = findViewById(R.id.tvSummaryText);
        Button btnConfirm = findViewById(R.id.btnConfirm);
        Button btnEdit = findViewById(R.id.btnEdit);

        Bundle extras = getIntent().getExtras();
        if (extras == null) return;

        String name = extras.getString("name");
        String email = extras.getString("email");
        int age = extras.getInt("age");
        String courseType = extras.getString("courseType");
        boolean access = extras.getBoolean("access");
        String instructor = extras.getString("instructor");
        String modules = extras.getString("modules");
        String date = extras.getString("date");
        String time = extras.getString("time");
        String batch = extras.getString("batch");

        String summary = "Name: " + name + "\n" +
                "Email: " + email + "\n" +
                "Age: " + age + "\n" +
                "Course Type: " + courseType + "\n" +
                "Access Enabled: " + (access ? "Yes" : "No") + "\n" +
                "Instructor: " + instructor + "\n" +
                "Modules: " + modules + "\n" +
                "Start Date: " + date + "\n" +
                "Time: " + time + "\n" +
                "Batch: " + batch;

        tvSummary.setText(summary);

        btnConfirm.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(this);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COL_NAME, name);
            cv.put(DatabaseHelper.COL_AGE, age);
            cv.put(DatabaseHelper.COL_EMAIL, email);
            cv.put(DatabaseHelper.COL_C_TYPE, courseType);
            cv.put(DatabaseHelper.COL_C_ACCESS, access ? 1 : 0);
            cv.put(DatabaseHelper.COL_C_INSTRUCTOR, instructor);
            cv.put(DatabaseHelper.COL_C_MODULES, modules);
            cv.put(DatabaseHelper.COL_DATE, date);
            cv.put(DatabaseHelper.COL_TIME, time);
            cv.put(DatabaseHelper.COL_C_BATCH, batch);

            if (db.insertCourseEnrollment(cv) != -1) {
                Toast.makeText(this, "Enrollment Successful!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error in Enrollment", Toast.LENGTH_SHORT).show();
            }
        });

        btnEdit.setOnClickListener(v -> finish());
    }
}
