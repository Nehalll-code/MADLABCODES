package com.example.emmanp;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

public class TaskDetailActivity extends AppCompatActivity {

    private static final String TAG = "TaskDetailActivity";
    private DatabaseHelper dbHelper;
    private int taskId;
    private TextView tvTitle, tvStatus, tvEmployee, tvDepartment, tvPriority, tvDeadline, tvTime, tvEquipment, tvUrgent, tvNotes;
    private String taskTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbHelper = new DatabaseHelper(this);
        taskId = getIntent().getIntExtra("task_id", -1);

        tvTitle = findViewById(R.id.tv_detail_title);
        tvStatus = findViewById(R.id.tv_detail_status);
        tvEmployee = findViewById(R.id.tv_detail_employee);
        tvDepartment = findViewById(R.id.tv_detail_department);
        tvPriority = findViewById(R.id.tv_detail_priority);
        tvDeadline = findViewById(R.id.tv_detail_deadline);
        tvTime = findViewById(R.id.tv_detail_time);
        tvEquipment = findViewById(R.id.tv_detail_equipment);
        tvUrgent = findViewById(R.id.tv_detail_urgent);
        tvNotes = findViewById(R.id.tv_detail_notes);

        if (taskId != -1) {
            loadTaskDetails();
        } else {
            finish();
        }
    }

    private void loadTaskDetails() {
        Log.d(TAG, "Loading details for task ID: " + taskId);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_TASKS, null, DatabaseHelper.COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)}, null, null, null);

        if (cursor.moveToFirst()) {
            taskTitle = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TITLE));
            getSupportActionBar().setTitle(taskTitle);
            tvTitle.setText(taskTitle);
            tvStatus.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_STATUS)));
            tvEmployee.setText("Employee: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_EMPLOYEE)));
            tvDepartment.setText("Department: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DEPARTMENT)));
            tvPriority.setText("Priority: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_PRIORITY)));
            tvDeadline.setText("Deadline: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DEADLINE)));
            tvTime.setText("Time: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TIME)));
            
            boolean hasEquip = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_EQUIPMENT_REQUIRED)) == 1;
            String equipName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_EQUIPMENT_NAME));
            tvEquipment.setText("Equipment: " + (hasEquip ? equipName : "None"));
            
            boolean isUrgent = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_URGENT)) == 1;
            tvUrgent.setText("Urgent: " + (isUrgent ? "Yes" : "No"));
            tvNotes.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_NOTES)));
        }
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_task) {
            Intent intent = new Intent(this, AddTaskActivity.class);
            intent.putExtra("task_id", taskId);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_mark_complete) {
            new AlertDialog.Builder(this)
                    .setMessage("Mark this task as complete?")
                    .setPositiveButton("Confirm", (dialog, which) -> {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.COLUMN_TASK_STATUS, "Completed");
                        db.update(DatabaseHelper.TABLE_TASKS, values, DatabaseHelper.COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
                        
                        Toast.makeText(this, "Task completed", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Task ID " + taskId + " marked complete");
                        sendNotification("Task closed", taskTitle);
                        loadTaskDetails();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        } else if (id == R.id.action_delete_task) {
            new AlertDialog.Builder(this)
                    .setMessage("Permanently delete this task?")
                    .setPositiveButton("Confirm", (dialog, which) -> {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete(DatabaseHelper.TABLE_TASKS, DatabaseHelper.COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
                        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "Task ID " + taskId + " deleted");
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        } else if (id == R.id.action_duplicate_task) {
            duplicateTask();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void duplicateTask() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_TASKS, null, DatabaseHelper.COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)}, null, null, null);
        if (cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_TASK_TITLE, "Copy of - " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TITLE)));
            values.put(DatabaseHelper.COLUMN_TASK_EMPLOYEE, cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_EMPLOYEE)));
            values.put(DatabaseHelper.COLUMN_TASK_DEPARTMENT, cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DEPARTMENT)));
            values.put(DatabaseHelper.COLUMN_TASK_PRIORITY, cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_PRIORITY)));
            values.put(DatabaseHelper.COLUMN_TASK_DEADLINE, cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DEADLINE)));
            values.put(DatabaseHelper.COLUMN_TASK_TIME, cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TIME)));
            values.put(DatabaseHelper.COLUMN_TASK_EQUIPMENT_REQUIRED, cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_EQUIPMENT_REQUIRED)));
            values.put(DatabaseHelper.COLUMN_TASK_EQUIPMENT_NAME, cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_EQUIPMENT_NAME)));
            values.put(DatabaseHelper.COLUMN_TASK_URGENT, cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_URGENT)));
            values.put(DatabaseHelper.COLUMN_TASK_NOTES, cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_NOTES)));
            values.put(DatabaseHelper.COLUMN_TASK_STATUS, "Pending");

            db.insert(DatabaseHelper.TABLE_TASKS, null, values);
            Toast.makeText(this, "Task duplicated", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Task ID " + taskId + " duplicated");
        }
        cursor.close();
    }

    private void sendNotification(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "task_reminders")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTaskDetails();
    }
}
