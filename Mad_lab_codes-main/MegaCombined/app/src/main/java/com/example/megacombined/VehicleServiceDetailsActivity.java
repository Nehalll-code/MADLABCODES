package com.example.megacombined;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class VehicleServiceDetailsActivity extends AppCompatActivity {

    private String selectedDate = "";
    private String selectedTime = "";
    private String selectedSlot = "Not Selected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        Spinner spinner = findViewById(R.id.spinnerCenter);
        ListView listView = findViewById(R.id.listViewIssues);
        Button btnDate = findViewById(R.id.btnDate);
        Button btnTime = findViewById(R.id.btnTime);
        Button btnSlot = findViewById(R.id.btnSlot);
        Button btnSummary = findViewById(R.id.btnSummary);
        TextView tvDateTime = findViewById(R.id.tvDateTime);

        // Spinner Setup
        String[] centers = {"City Center Service", "North Wing Garage", "Express Service Hub"};
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, centers));

        // ListView Setup
        String[] issues = {"Engine Noise", "Brake Squeal", "Tire Pressure", "Battery Check", "AC Cooling", "Suspension"};
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, issues));

        btnDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                tvDateTime.setText("Date: " + selectedDate + " | Time: " + selectedTime);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                selectedTime = hourOfDay + ":" + String.format("%02d", minute);
                tvDateTime.setText("Date: " + selectedDate + " | Time: " + selectedTime);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });

        btnSlot.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, btnSlot);
            popup.getMenu().add("Morning (9AM - 12PM)");
            popup.getMenu().add("Afternoon (1PM - 4PM)");
            popup.getMenu().add("Evening (5PM - 8PM)");
            popup.setOnMenuItemClickListener(item -> {
                selectedSlot = item.getTitle().toString();
                btnSlot.setText("Slot: " + selectedSlot);
                return true;
            });
            popup.show();
        });

        btnSummary.setOnClickListener(v -> {
            if (selectedDate.isEmpty() || selectedTime.isEmpty() || selectedSlot.equals("Not Selected")) {
                Toast.makeText(this, "Please select Date, Time and Slot", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder issuesBuilder = new StringBuilder();
            for (int i = 0; i < listView.getCount(); i++) {
                if (listView.isItemChecked(i)) {
                    issuesBuilder.append(issues[i]).append(", ");
                }
            }

            Intent intent = new Intent(this, VehicleSummaryActivity.class);
            intent.putExtras(getIntent().getExtras()); // Pass previous data
            intent.putExtra("center", spinner.getSelectedItem().toString());
            intent.putExtra("issues", issuesBuilder.toString());
            intent.putExtra("date", selectedDate);
            intent.putExtra("time", selectedTime);
            intent.putExtra("slot", selectedSlot);
            startActivity(intent);
        });
    }
}
