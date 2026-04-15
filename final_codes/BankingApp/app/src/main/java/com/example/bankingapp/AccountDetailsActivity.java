package com.example.bankingapp;

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
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Arrays;
import java.util.List;

public class AccountDetailsActivity extends AppCompatActivity {

    private Spinner spinnerBranch;
    private ListView lvServices;
    private Button btnPickDate, btnPickTime, btnAccountMode, btnNext;
    private Customer customer;
    private String selectedDate = "", selectedTime = "", selectedMode = "Online";
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        customer = (Customer) getIntent().getSerializableExtra("customer");
        isEdit = getIntent().getBooleanExtra("isEdit", false);

        spinnerBranch = findViewById(R.id.spinnerBranch);
        lvServices = findViewById(R.id.lvServices);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnPickTime = findViewById(R.id.btnPickTime);
        btnAccountMode = findViewById(R.id.btnAccountMode);
        btnNext = findViewById(R.id.btnNextAccount);

        // Branch Spinner
        String[] branches = {"Main Branch", "Downtown", "Westside", "Corporate"};
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branches);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBranch.setAdapter(branchAdapter);

        // Services ListView
        String[] services = {"ATM", "Net Banking", "Mobile Banking", "Cheque Book"};
        ArrayAdapter<String> servicesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, services);
        lvServices.setAdapter(servicesAdapter);

        if (isEdit && customer != null) {
            // Pre-populate fields
            for (int i = 0; i < branches.length; i++) {
                if (branches[i].equals(customer.getBranch())) {
                    spinnerBranch.setSelection(i);
                    break;
                }
            }

            if (customer.getServices() != null) {
                List<String> selectedServices = Arrays.asList(customer.getServices().split(", "));
                for (int i = 0; i < services.length; i++) {
                    if (selectedServices.contains(services[i])) {
                        lvServices.setItemChecked(i, true);
                    }
                }
            }

            selectedDate = customer.getOpeningDate();
            btnPickDate.setText(selectedDate);
            selectedTime = customer.getOpeningTime();
            btnPickTime.setText(selectedTime);
            selectedMode = customer.getAccountMode();
            btnAccountMode.setText("Mode: " + selectedMode);
        }

        btnPickDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                btnPickDate.setText(selectedDate);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnPickTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                selectedTime = hourOfDay + ":" + String.format("%02d", minute);
                btnPickTime.setText(selectedTime);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });

        btnAccountMode.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenu().add("Online");
            popup.getMenu().add("Offline");
            popup.setOnMenuItemClickListener(item -> {
                selectedMode = item.getTitle().toString();
                btnAccountMode.setText("Mode: " + selectedMode);
                return true;
            });
            popup.show();
        });

        btnNext.setOnClickListener(v -> {
            if (selectedDate == null || selectedDate.isEmpty() || selectedTime == null || selectedTime.isEmpty()) {
                Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lvServices.getCount(); i++) {
                if (lvServices.isItemChecked(i)) {
                    sb.append(services[i]).append(", ");
                }
            }
            if (sb.length() > 2) sb.setLength(sb.length() - 2);

            customer.setBranch(spinnerBranch.getSelectedItem().toString());
            customer.setServices(sb.toString());
            customer.setOpeningDate(selectedDate);
            customer.setOpeningTime(selectedTime);
            customer.setAccountMode(selectedMode);

            Intent intent = new Intent(this, SummaryActivity.class);
            intent.putExtra("customer", customer);
            intent.putExtra("isEdit", isEdit);
            startActivity(intent);
        });
    }
}
