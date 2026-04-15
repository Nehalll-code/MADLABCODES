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

public class ParkingDetailsActivity extends AppCompatActivity {

    private String selectedDate = "";
    private String selectedTime = "";
    private String selectedShift = "Not Selected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details); // Reusing layout

        TextView title = findViewById(R.id.serviceDetailTitle);
        Spinner spinner = findViewById(R.id.spinnerCenter);
        ListView listView = findViewById(R.id.listViewIssues);
        Button btnDate = findViewById(R.id.btnDate);
        Button btnTime = findViewById(R.id.btnTime);
        Button btnSlot = findViewById(R.id.btnSlot);
        Button btnSummary = findViewById(R.id.btnSummary);
        TextView tvDateTime = findViewById(R.id.tvDateTime);

        title.setText("Parking Booking - Step 2");
        btnSlot.setText("Select Shift (PopupMenu)");

        // Spinner (Parking Area)
        String[] areas = {"Basement A", "Ground Floor", "Roof Top", "VIP Section"};
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, areas));

        // ListView (Slot Options)
        String[] slots = {"Near Elevator", "Wide Space", "Electric Charging", "CCTV Covered", "Quick Exit"};
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, slots));

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
            popup.getMenu().add("Day Shift");
            popup.getMenu().add("Night Shift");
            popup.getMenu().add("24-Hour Stay");
            popup.setOnMenuItemClickListener(item -> {
                selectedShift = item.getTitle().toString();
                btnSlot.setText("Shift: " + selectedShift);
                return true;
            });
            popup.show();
        });

        btnSummary.setOnClickListener(v -> {
            if (selectedDate.isEmpty() || selectedTime.isEmpty() || selectedShift.equals("Not Selected")) {
                Toast.makeText(this, "Please select Date, Time and Shift", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder optionsBuilder = new StringBuilder();
            for (int i = 0; i < listView.getCount(); i++) {
                if (listView.isItemChecked(i)) {
                    optionsBuilder.append(slots[i]).append(", ");
                }
            }

            Intent intent = new Intent(this, ParkingSummaryActivity.class);
            intent.putExtras(getIntent().getExtras());
            intent.putExtra("area", spinner.getSelectedItem().toString());
            intent.putExtra("options", optionsBuilder.toString());
            intent.putExtra("date", selectedDate);
            intent.putExtra("time", selectedTime);
            intent.putExtra("shift", selectedShift);
            startActivity(intent);
        });
    }
}
