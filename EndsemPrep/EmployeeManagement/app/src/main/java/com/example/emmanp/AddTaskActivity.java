package com.example.emmanp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = "AddTaskActivity";
    private DatabaseHelper dbHelper;
    private EditText etTitle, etEquipmentName, etNotes, etDeadline, etScheduledTime;
    private Spinner spEmployee;
    private RadioGroup rgDepartment, rgPriority;
    private android.widget.CheckBox cbEquipment;
    private androidx.appcompat.widget.SwitchCompat swUrgent;
    private ToggleButton tbUrgent;
    private TextView tvCharCount;
    private LinearLayout rootLayout;
    private Calendar selectedCalendar = Calendar.getInstance();
    private int editTaskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbHelper = new DatabaseHelper(this);
        rootLayout = findViewById(R.id.ll_add_task_root);
        etTitle = findViewById(R.id.et_task_title);
        spEmployee = findViewById(R.id.sp_employee);
        rgDepartment = findViewById(R.id.rg_department);
        rgPriority = findViewById(R.id.rg_priority);
        etDeadline = findViewById(R.id.et_deadline);
        etScheduledTime = findViewById(R.id.et_scheduled_time);
        cbEquipment = findViewById(R.id.cb_requires_equipment);
        etEquipmentName = findViewById(R.id.et_equipment_name);
        swUrgent = findViewById(R.id.sw_urgent);
        tbUrgent = findViewById(R.id.tb_urgent);
        etNotes = findViewById(R.id.et_notes);
        tvCharCount = findViewById(R.id.tv_char_count);

        setupEmployeeSpinner();
        setupDatePicker();
        setupTimePicker();
        setupListeners();

        editTaskId = getIntent().getIntExtra("task_id", -1);
        if (editTaskId != -1) {
            getSupportActionBar().setTitle("Edit Task");
            loadTaskData(editTaskId);
        }
    }

    private void loadTaskData(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_TASKS, null, DatabaseHelper.COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            etTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TITLE)));
            
            String employee = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_EMPLOYEE));
            for (int i = 0; i < spEmployee.getCount(); i++) {
                if (spEmployee.getItemAtPosition(i).toString().equals(employee)) {
                    spEmployee.setSelection(i);
                    break;
                }
            }

            String dept = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DEPARTMENT));
            if (dept.equals("Operations")) rgDepartment.check(R.id.rb_operations);
            else if (dept.equals("Logistics")) rgDepartment.check(R.id.rb_logistics);
            else if (dept.equals("Administration")) rgDepartment.check(R.id.rb_administration);

            String priority = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_PRIORITY));
            if (priority.equals("Low")) rgPriority.check(R.id.rb_low);
            else if (priority.equals("Medium")) rgPriority.check(R.id.rb_medium);
            else if (priority.equals("High")) rgPriority.check(R.id.rb_high);

            etDeadline.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DEADLINE)));
            etScheduledTime.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TIME)));
            
            boolean reqEquip = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_EQUIPMENT_REQUIRED)) == 1;
            cbEquipment.setChecked(reqEquip);
            if (reqEquip) {
                etEquipmentName.setVisibility(View.VISIBLE);
                etEquipmentName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_EQUIPMENT_NAME)));
            }

            boolean isUrgent = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_URGENT)) == 1;
            swUrgent.setChecked(isUrgent);
            tbUrgent.setChecked(isUrgent);
            etNotes.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_NOTES)));
        }
        cursor.close();
    }

    private void setupEmployeeSpinner() {
        Cursor cursor = dbHelper.getAllEmployees();
        ArrayList<String> employees = new ArrayList<>();
        while (cursor.moveToNext()) {
            employees.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMPLOYEE_NAME)));
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, employees);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEmployee.setAdapter(adapter);

        spEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Selected Employee: " + employees.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupDatePicker() {
        etDeadline.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                
                Calendar maxDate = Calendar.getInstance();
                maxDate.add(Calendar.DAY_OF_YEAR, 20);
                
                Calendar minDate = Calendar.getInstance();
                minDate.add(Calendar.DAY_OF_YEAR, -20);
                minDate.set(Calendar.HOUR_OF_DAY, 0);
                minDate.set(Calendar.MINUTE, 0);
                minDate.set(Calendar.SECOND, 0);
                minDate.set(Calendar.MILLISECOND, 0);

                if (selectedDate.before(minDate) || selectedDate.after(maxDate)) {
                    new AlertDialog.Builder(this)
                            .setMessage("Date must be within 20 days before or after today")
                            .setPositiveButton("OK", null)
                            .show();
                    etDeadline.setText("");
                } else {
                    selectedCalendar.set(Calendar.YEAR, year);
                    selectedCalendar.set(Calendar.MONTH, month);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.getTime());
                    etDeadline.setText(dateStr);
                }
            }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    private void setupTimePicker() {
        etScheduledTime.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                if (hourOfDay < 9 || (hourOfDay >= 17 && minute > 0) || hourOfDay > 17) {
                    new AlertDialog.Builder(this)
                            .setMessage("Time must be between 9:00 AM and 5:00 PM")
                            .setPositiveButton("OK", null)
                            .show();
                    etScheduledTime.setText("");
                } else {
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedCalendar.set(Calendar.MINUTE, minute);
                    String timeStr = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    etScheduledTime.setText(timeStr);
                }
            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        });
    }

    private void setupListeners() {
        cbEquipment.setOnCheckedChangeListener((buttonView, isChecked) -> {
            etEquipmentName.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        swUrgent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tbUrgent.setChecked(isChecked);
            if (isChecked) {
                rootLayout.setBackgroundColor(Color.parseColor("#FFEBEE"));
                Toast.makeText(this, "Marked as urgent", Toast.LENGTH_SHORT).show();
            } else {
                rootLayout.setBackgroundColor(Color.WHITE);
                Toast.makeText(this, "Urgent flag removed", Toast.LENGTH_SHORT).show();
            }
        });

        tbUrgent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            swUrgent.setChecked(isChecked);
        });

        etNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCharCount.setText(s.length() + "/200");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        findViewById(R.id.btn_submit).setOnClickListener(v -> validateAndSubmit());
    }

    private void validateAndSubmit() {
        String title = etTitle.getText().toString().trim();
        String deadline = etDeadline.getText().toString().trim();
        String time = etScheduledTime.getText().toString().trim();

        if (title.isEmpty() || rgDepartment.getCheckedRadioButtonId() == -1 || rgPriority.getCheckedRadioButtonId() == -1 || deadline.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TASK_TITLE, title);
        values.put(DatabaseHelper.COLUMN_TASK_EMPLOYEE, spEmployee.getSelectedItem().toString());
        values.put(DatabaseHelper.COLUMN_TASK_DEPARTMENT, ((RadioButton) findViewById(rgDepartment.getCheckedRadioButtonId())).getText().toString());
        values.put(DatabaseHelper.COLUMN_TASK_PRIORITY, ((RadioButton) findViewById(rgPriority.getCheckedRadioButtonId())).getText().toString());
        values.put(DatabaseHelper.COLUMN_TASK_DEADLINE, deadline);
        values.put(DatabaseHelper.COLUMN_TASK_TIME, time);
        values.put(DatabaseHelper.COLUMN_TASK_EQUIPMENT_REQUIRED, cbEquipment.isChecked() ? 1 : 0);
        values.put(DatabaseHelper.COLUMN_TASK_EQUIPMENT_NAME, cbEquipment.isChecked() ? etEquipmentName.getText().toString() : "");
        values.put(DatabaseHelper.COLUMN_TASK_URGENT, swUrgent.isChecked() ? 1 : 0);
        values.put(DatabaseHelper.COLUMN_TASK_NOTES, etNotes.getText().toString());

        if (editTaskId == -1) {
            values.put(DatabaseHelper.COLUMN_TASK_STATUS, "Pending");
            db.insert(DatabaseHelper.TABLE_TASKS, null, values);
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
            sendNotification("New task assigned", title);
        } else {
            db.update(DatabaseHelper.TABLE_TASKS, values, DatabaseHelper.COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(editTaskId)});
            Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void sendNotification(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "task_reminders")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify((int) System.currentTimeMillis(), builder.build());
    }
}
