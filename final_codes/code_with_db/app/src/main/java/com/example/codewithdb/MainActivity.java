package com.example.codewithdb;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DBHelper db;
    private String selectedDate = "Not Selected";
    private String selectedTime = "Not Selected";
    private ArrayList<String> dataList;
    private ArrayList<Integer> idList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);

        // ===================== UI =====================
        EditText et1 = findViewById(R.id.et1);
        EditText et2 = findViewById(R.id.et2);
        Button btnInsert = findViewById(R.id.btnInsert);
        Button btnNext = findViewById(R.id.btnNext);
        Button btnDate = findViewById(R.id.btnDate);
        Button btnTime = findViewById(R.id.btnTime);
        Spinner spinner = findViewById(R.id.spinner);
        listView = findViewById(R.id.listView);
        ToggleButton toggle = findViewById(R.id.toggle);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        CheckBox cb1 = findViewById(R.id.cb1);
        CheckBox cb2 = findViewById(R.id.cb2);
        CheckBox cb3 = findViewById(R.id.cb3);

        // ===================== SPINNER =====================
        String[] options = {"Option 1", "Option 2", "Option 3"};
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options));

        // ===================== DATE =====================
        btnDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) -> {
                selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y);
                Toast.makeText(this, "Date: " + selectedDate, Toast.LENGTH_SHORT).show();
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        // ===================== TIME =====================
        btnTime.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new TimePickerDialog(this, (view, h, m) -> {
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", h, m);
                Toast.makeText(this, "Time: " + selectedTime, Toast.LENGTH_SHORT).show();
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        });

        // ===================== INSERT + DB =====================
        btnInsert.setOnClickListener(v -> {
            String text1 = et1.getText().toString().trim();
            String text2 = et2.getText().toString().trim();

            if (text1.isEmpty() || text2.isEmpty()) {
                Toast.makeText(this, "Please fill value 1 and value 2", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedRadioId = radioGroup.getCheckedRadioButtonId();
            String radioText = "None";
            if (selectedRadioId != -1) {
                RadioButton rb = findViewById(selectedRadioId);
                radioText = rb.getText().toString();
            }

            String mode = toggle.isChecked() ? "ON" : "OFF";

            StringBuilder services = new StringBuilder();
            if (cb1.isChecked()) services.append("C1 ");
            if (cb2.isChecked()) services.append("C2 ");
            if (cb3.isChecked()) services.append("C3 ");

            String combinedData = spinner.getSelectedItem().toString() + " | " +
                    selectedDate + " | " +
                    selectedTime + " | " +
                    radioText + " | " +
                    services.toString().trim() + " | " +
                    mode;

            boolean result = db.insertData(text1, text2, combinedData);

            if (result) {
                Toast.makeText(this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                et1.setText("");
                et2.setText("");
                loadData();
            } else {
                Toast.makeText(this, "Error Inserting Data", Toast.LENGTH_SHORT).show();
            }
        });

        // ===================== NEXT BUTTON =====================
        btnNext.setOnClickListener(v -> {
            if (idList.isEmpty()) {
                Toast.makeText(this, "No data to edit", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Click a list item to edit/delete", Toast.LENGTH_SHORT).show();
        });

        // ===================== LIST CLICK (EDIT) =====================
        listView.setOnItemClickListener((parent, view, position, id) -> {
            int recordId = idList.get(position);
            
            // Get full details from Cursor to pass to next activity
            Cursor cursor = db.getAllData();
            cursor.moveToPosition(position);
            String c1 = cursor.getString(1);
            String c2 = cursor.getString(2);
            String c3 = cursor.getString(3);
            cursor.close();

            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra("ID", recordId);
            intent.putExtra("COL1", c1);
            intent.putExtra("COL2", c2);
            intent.putExtra("COL3", c3);
            startActivity(intent);
        });

        // ===================== LIST LONG CLICK (DELETE) =====================
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            int recordId = idList.get(position);
            db.deleteData(recordId);
            Toast.makeText(this, "Record Deleted", Toast.LENGTH_SHORT).show();
            loadData();
            return true;
        });

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // Refresh data when returning from SecondActivity
    }

    // ===================== LOAD DATA =====================
    private void loadData() {
        Cursor cursor = db.getAllData();
        dataList = new ArrayList<>();
        idList = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                idList.add(cursor.getInt(0));
                dataList.add(cursor.getString(1) + " | " +
                             cursor.getString(2) + " | " +
                             cursor.getString(3));
            }
            cursor.close();
        }

        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList));
    }

    // ===================== TOP RIGHT MENU =====================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, "Reset");
        menu.add(Menu.NONE, 2, Menu.NONE, "Exit");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 1) { // Reset
            db.deleteAllData();
            loadData();
            Toast.makeText(this, "Database Cleared", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == 2) { // Exit
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
