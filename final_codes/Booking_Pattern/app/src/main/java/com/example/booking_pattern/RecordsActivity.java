package com.example.booking_pattern;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecordsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listView;
    private List<BookingRecord> records;
    private ArrayAdapter<BookingRecord> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        dbHelper = DatabaseHelper.getInstance(this);
        listView = findViewById(R.id.listViewRecords);

        loadRecords();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            showOptionsDialog(records.get(position));
        });
    }

    private void loadRecords() {
        records = dbHelper.getAllBookings();
        if (records.isEmpty()) {
            Toast.makeText(this, "No records found!", Toast.LENGTH_SHORT).show();
        }

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                records
        );
        listView.setAdapter(adapter);
    }

    private void showOptionsDialog(BookingRecord record) {
        String[] options = {"View Details", "Update Name", "Delete Record"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Booking Options");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                showDetailsDialog(record);
            } else if (which == 1) {
                showUpdateDialog(record);
            } else if (which == 2) {
                confirmDelete(record);
            }
        });
        builder.show();
    }

    private void showDetailsDialog(BookingRecord record) {
        String details = "Name: " + record.getName() + "\n" +
                "Age: " + record.getAge() + "\n" +
                "Email: " + record.getEmail() + "\n" +
                "Category: " + record.getCategory() + "\n" +
                "Date: " + record.getDate() + "\n" +
                "Time: " + record.getTime() + "\n" +
                "Type: " + record.getType() + "\n" +
                "Notification: " + record.getNotification();

        new AlertDialog.Builder(this)
                .setTitle("Booking Details")
                .setMessage(details)
                .setPositiveButton("Close", null)
                .show();
    }

    private void showUpdateDialog(BookingRecord record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Name");

        final EditText input = new EditText(this);
        input.setText(record.getName());
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newName = input.getText().toString();
            if (newName.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            BookingRecord updatedRecord = new BookingRecord(
                    record.getId(),
                    newName,
                    record.getAge(),
                    record.getEmail(),
                    record.getCategory(),
                    record.getDate(),
                    record.getTime(),
                    record.getType(),
                    record.getNotification()
            );

            dbHelper.updateBooking(updatedRecord);
            loadRecords();
            Toast.makeText(this, "Name updated", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void confirmDelete(BookingRecord record) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Record")
                .setMessage("Are you sure you want to delete booking for " + record.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteBooking(record.getId());
                    loadRecords();
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
