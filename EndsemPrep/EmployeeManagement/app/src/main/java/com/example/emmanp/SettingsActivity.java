package com.example.emmanp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private DatabaseHelper dbHelper;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbHelper = new DatabaseHelper(this);
        prefs = getSharedPreferences("WorkForcePrefs", MODE_PRIVATE);

        setupNotificationSettings();
        setupDisplaySettings();
        setupDatabaseSettings();
    }

    private void setupNotificationSettings() {
        SwitchCompat swReminders = findViewById(R.id.sw_reminders);
        SwitchCompat swUrgent = findViewById(R.id.sw_urgent_alerts);
        CheckBox cbSound = findViewById(R.id.cb_sound);
        CheckBox cbVibrate = findViewById(R.id.cb_vibrate);

        swReminders.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, isChecked ? "Reminders enabled" : "Reminders disabled", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Reminders toggled: " + isChecked);
        });

        // Other listeners as needed...
    }

    private void setupDisplaySettings() {
        RadioGroup rgView = findViewById(R.id.rg_default_view);
        SwitchCompat swDark = findViewById(R.id.sw_dark_mode);

        rgView.setOnCheckedChangeListener((group, checkedId) -> {
            String view = "List";
            if (checkedId == R.id.rb_table) view = "Table";
            else if (checkedId == R.id.rb_grid) view = "Grid";
            
            prefs.edit().putString("default_view", view).apply();
            Log.d(TAG, "Default view saved: " + view);
        });

        swDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, "Restart app to apply", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Dark mode hint toggled");
        });
    }

    private void setupDatabaseSettings() {
        findViewById(R.id.btn_export_db).setOnClickListener(v -> showExportPopup());

        findViewById(R.id.btn_clear_tasks).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure?")
                    .setPositiveButton("Confirm", (dialog, which) -> {
                        new AlertDialog.Builder(this)
                                .setMessage("This cannot be undone. Proceed?")
                                .setPositiveButton("Proceed", (dialog2, which2) -> {
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    db.delete(DatabaseHelper.TABLE_TASKS, null, null);
                                    Toast.makeText(this, "All tasks cleared", Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "Database tasks table cleared");
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        findViewById(R.id.btn_sample_data).setOnClickListener(v -> {
            insertSampleData();
            Toast.makeText(this, "Sample data loaded", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Sample data inserted");
        });
    }

    private void showExportPopup() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cTasks = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_TASKS, null);
        int totalTasks = 0;
        if (cTasks.moveToFirst()) totalTasks = cTasks.getInt(0);
        cTasks.close();

        Cursor cEmp = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_EMPLOYEES, null);
        int totalEmployees = 0;
        if (cEmp.moveToFirst()) totalEmployees = cEmp.getInt(0);
        cEmp.close();

        Cursor cOldest = db.query(DatabaseHelper.TABLE_TASKS, new String[]{DatabaseHelper.COLUMN_TASK_DEADLINE}, null, null, null, null, DatabaseHelper.COLUMN_TASK_DEADLINE + " ASC", "1");
        String oldestDate = "N/A";
        if (cOldest.moveToFirst()) oldestDate = cOldest.getString(0);
        cOldest.close();

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_export, null);
        ((TextView) popupView.findViewById(R.id.tv_export_summary)).setText(
                "Total Tasks: " + totalTasks + "\nTotal Employees: " + totalEmployees + "\nOldest Task: " + oldestDate
        );

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(10);
        popupWindow.showAtLocation(findViewById(R.id.toolbar), Gravity.CENTER, 0, 0);
    }

    private void insertSampleData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] titles = {"Review Logistics", "Staff Meeting", "Equipment Check", "Inventory Audit", "Client Follow-up"};
        String[] depts = {"Logistics", "Administration", "Operations", "Logistics", "Operations"};
        
        for (int i = 0; i < 5; i++) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_TASK_TITLE, titles[i]);
            values.put(DatabaseHelper.COLUMN_TASK_EMPLOYEE, "John Doe");
            values.put(DatabaseHelper.COLUMN_TASK_DEPARTMENT, depts[i]);
            values.put(DatabaseHelper.COLUMN_TASK_PRIORITY, "Medium");
            values.put(DatabaseHelper.COLUMN_TASK_DEADLINE, "2025-05-20");
            values.put(DatabaseHelper.COLUMN_TASK_TIME, "10:00");
            values.put(DatabaseHelper.COLUMN_TASK_STATUS, "Pending");
            db.insert(DatabaseHelper.TABLE_TASKS, null, values);
        }
    }
}
