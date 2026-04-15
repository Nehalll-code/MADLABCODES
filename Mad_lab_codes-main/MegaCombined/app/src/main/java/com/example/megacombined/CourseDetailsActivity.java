package com.example.megacombined;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class CourseDetailsActivity extends AppCompatActivity {

    private String selectedDate = "";
    private String selectedTime = "";
    private String selectedBatch = "Not Selected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details); // Reusing the layout

        TextView title = findViewById(R.id.serviceDetailTitle);
        Spinner spinner = findViewById(R.id.spinnerCenter);
        ListView listView = findViewById(R.id.listViewIssues);
        Button btnDate = findViewById(R.id.btnDate);
        Button btnTime = findViewById(R.id.btnTime);
        Button btnSlot = findViewById(R.id.btnSlot);
        Button btnSummary = findViewById(R.id.btnSummary);
        TextView tvDateTime = findViewById(R.id.tvDateTime);

        title.setText("Course Enrollment - Step 2");
        btnSlot.setText("Select Batch (PopupMenu)");

        // Spinner Setup (Instructors)
        String[] instructors = {"Dr. Smith", "Prof. Johnson", "Ms. Davis"};
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, instructors));

        // ListView Setup (Modules)
        String[] modules = {"Java Fundamentals", "Android UI", "Database Basics", "Networking", "Security", "Advanced UI"};
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, modules));

        btnDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                tvDateTime.setText("Start Date: " + selectedDate + " | Time: " + selectedTime);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                selectedTime = hourOfDay + ":" + String.format("%02d", minute);
                tvDateTime.setText("Start Date: " + selectedDate + " | Time: " + selectedTime);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });

        btnSlot.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, btnSlot);
            popup.getMenu().add("Morning Batch (8AM)");
            popup.getMenu().add("Evening Batch (6PM)");
            popup.getMenu().add("Weekend Batch");
            popup.setOnMenuItemClickListener(item -> {
                selectedBatch = item.getTitle().toString();
                btnSlot.setText("Batch: " + selectedBatch);
                return true;
            });
            popup.show();
        });

        btnSummary.setOnClickListener(v -> {
            if (selectedDate.isEmpty() || selectedTime.isEmpty() || selectedBatch.equals("Not Selected")) {
                Toast.makeText(this, "Please select Date, Time and Batch", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder modulesBuilder = new StringBuilder();
            for (int i = 0; i < listView.getCount(); i++) {
                if (listView.isItemChecked(i)) {
                    modulesBuilder.append(modules[i]).append(", ");
                }
            }

            Intent intent = new Intent(this, CourseSummaryActivity.class);
            intent.putExtras(getIntent().getExtras()); // Pass previous data
            intent.putExtra("instructor", spinner.getSelectedItem().toString());
            intent.putExtra("modules", modulesBuilder.toString());
            intent.putExtra("date", selectedDate);
            intent.putExtra("time", selectedTime);
            intent.putExtra("batch", selectedBatch);
            startActivity(intent);
        });
    }
}
