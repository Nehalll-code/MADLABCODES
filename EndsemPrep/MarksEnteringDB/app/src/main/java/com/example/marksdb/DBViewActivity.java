package com.example.marksdb;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DBViewActivity extends AppCompatActivity {
    private static final String TAG = "DBViewActivity";
    private DatabaseHelper dbHelper;
    private TextView tvUsers, tvMarks, tvGpa;
    private EditText etDeleteName;
    private Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_view);

        Toolbar toolbar = findViewById(R.id.toolbarDB);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);
        tvUsers = findViewById(R.id.tvUsersData);
        tvMarks = findViewById(R.id.tvMarksData);
        tvGpa = findViewById(R.id.tvGpaData);
        etDeleteName = findViewById(R.id.etDeleteStudentName);
        btnDelete = findViewById(R.id.btnDeleteRecord);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord();
            }
        });

        refreshData();
    }

    private void deleteRecord() {
        String name = etDeleteName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Enter a name to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean deleted = dbHelper.deleteStudentRecord(name);
        if (deleted) {
            Toast.makeText(this, "Records deleted for " + name, Toast.LENGTH_SHORT).show();
            etDeleteName.setText("");
            refreshData();
        } else {
            Toast.makeText(this, "No records found for " + name, Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshData() {
        displayUsers();
        displayMarks();
        displayGPA();
    }

    private void displayUsers() {
        Cursor cursor = dbHelper.getAllUsers();
        StringBuilder sb = new StringBuilder();
        if (cursor.moveToFirst()) {
            do {
                sb.append("ID: ").append(cursor.getInt(0))
                  .append(", User: ").append(cursor.getString(1))
                  .append(", Pass: ").append(cursor.getString(2))
                  .append("\n");
            } while (cursor.moveToNext());
        } else {
            sb.append("No users found");
        }
        tvUsers.setText(sb.toString());
        cursor.close();
    }

    private void displayMarks() {
        Cursor cursor = dbHelper.getAllMarks();
        StringBuilder sb = new StringBuilder();
        if (cursor.moveToFirst()) {
            do {
                sb.append("Name: ").append(cursor.getString(1))
                  .append(", M1: ").append(cursor.getInt(2))
                  .append(", M2: ").append(cursor.getInt(3))
                  .append(", M3: ").append(cursor.getInt(4))
                  .append(", M4: ").append(cursor.getInt(5))
                  .append(", M5: ").append(cursor.getInt(6))
                  .append("\n");
            } while (cursor.moveToNext());
        } else {
            sb.append("No marks found");
        }
        tvMarks.setText(sb.toString());
        cursor.close();
    }

    private void displayGPA() {
        Cursor cursor = dbHelper.getAllGPA();
        StringBuilder sb = new StringBuilder();
        if (cursor.moveToFirst()) {
            do {
                sb.append("Name: ").append(cursor.getString(1))
                  .append(", GPA: ").append(String.format("%.2f", cursor.getDouble(2)))
                  .append("\n");
            } while (cursor.moveToNext());
        } else {
            sb.append("No GPA records found");
        }
        tvGpa.setText(sb.toString());
        cursor.close();
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
