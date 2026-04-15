package com.example.emmanp;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String CHANNEL_ID = "task_reminders";
    private DatabaseHelper dbHelper;
    private TextView tvTotalTasks, tvDueToday, tvOverdue, tvPriorityLevel;
    private LinearLayout llRecentTasks;
    private int priorityLevel = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);

        tvTotalTasks = findViewById(R.id.tv_total_tasks_count);
        tvDueToday = findViewById(R.id.tv_due_today_count);
        tvOverdue = findViewById(R.id.tv_overdue_count);
        tvPriorityLevel = findViewById(R.id.tv_priority_level);
        llRecentTasks = findViewById(R.id.ll_recent_tasks);

        findViewById(R.id.card_total_tasks).setOnClickListener(v -> navigateToTaskList("all"));
        findViewById(R.id.card_due_today).setOnClickListener(v -> navigateToTaskList("due_today"));
        findViewById(R.id.card_overdue).setOnClickListener(v -> navigateToTaskList("overdue"));

        ZoomButton zbPlus = findViewById(R.id.zb_plus);
        ZoomButton zbMinus = findViewById(R.id.zb_minus);

        zbPlus.setOnClickListener(v -> {
            if (priorityLevel < 5) {
                priorityLevel++;
                tvPriorityLevel.setText(String.valueOf(priorityLevel));
                Log.d(TAG, "Priority Level increased to: " + priorityLevel);
            }
        });

        zbMinus.setOnClickListener(v -> {
            if (priorityLevel > 1) {
                priorityLevel--;
                tvPriorityLevel.setText(String.valueOf(priorityLevel));
                Log.d(TAG, "Priority Level decreased to: " + priorityLevel);
            }
        });

        findViewById(R.id.btn_add_task).setOnClickListener(v -> {
            Log.d(TAG, "Navigating to Add Task Screen");
            startActivity(new Intent(this, AddTaskActivity.class));
        });

        createNotificationChannel();
        refreshDashboard();
    }

    private void refreshDashboard() {
        Log.d(TAG, "Refreshing dashboard data");
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Total Tasks
        Cursor cursorAll = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_TASKS, null);
        if (cursorAll.moveToFirst()) tvTotalTasks.setText(String.valueOf(cursorAll.getInt(0)));
        cursorAll.close();

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Due Today
        Cursor cursorToday = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_TASKS + " WHERE " + DatabaseHelper.COLUMN_TASK_DEADLINE + " = ?", new String[]{today});
        if (cursorToday.moveToFirst()) tvDueToday.setText(String.valueOf(cursorToday.getInt(0)));
        cursorToday.close();

        // Overdue (Pending and date < today)
        Cursor cursorOverdue = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_TASKS + " WHERE " + DatabaseHelper.COLUMN_TASK_DEADLINE + " < ? AND " + DatabaseHelper.COLUMN_TASK_STATUS + " = 'Pending'", new String[]{today});
        if (cursorOverdue.moveToFirst()) tvOverdue.setText(String.valueOf(cursorOverdue.getInt(0)));
        cursorOverdue.close();

        // Recent Tasks (Last 3)
        llRecentTasks.removeAllViews();
        Cursor cursorRecent = db.query(DatabaseHelper.TABLE_TASKS, null, null, null, null, null, DatabaseHelper.COLUMN_TASK_ID + " DESC", "3");
        while (cursorRecent.moveToNext()) {
            TextView tv = new TextView(this);
            String title = cursorRecent.getString(cursorRecent.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TITLE));
            String status = cursorRecent.getString(cursorRecent.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_STATUS));
            tv.setText(title + " (" + status + ")");
            tv.setPadding(0, 8, 0, 8);
            llRecentTasks.addView(tv);
        }
        cursorRecent.close();
    }

    private void navigateToTaskList(String filter) {
        Log.d(TAG, "Navigating to Task List with filter: " + filter);
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter", filter);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            refreshDashboard();
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            Toast.makeText(this, "Tasks refreshed at " + currentTime, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Refresh triggered at " + currentTime);
            return true;
        } else if (id == R.id.action_view_all_tasks) {
            navigateToTaskList("all");
            return true;
        } else if (id == R.id.action_mark_all_complete) {
            showConfirmationDialog("Are you sure you want to mark all tasks as complete?", () -> {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("UPDATE " + DatabaseHelper.TABLE_TASKS + " SET " + DatabaseHelper.COLUMN_TASK_STATUS + " = 'Completed'");
                Toast.makeText(this, "All tasks marked complete", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "All tasks marked complete in database");
                refreshDashboard();
            });
            return true;
        } else if (id == R.id.action_reset_all_pending) {
            showConfirmationDialog("Are you sure you want to reset all tasks to pending?", () -> {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("UPDATE " + DatabaseHelper.TABLE_TASKS + " SET " + DatabaseHelper.COLUMN_TASK_STATUS + " = 'Pending'");
                Toast.makeText(this, "All tasks reset to pending", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "All tasks reset to pending in database");
                refreshDashboard();
            });
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_about) {
            showAboutPopup();
            return true;
        } else if (id == R.id.action_send_reminder) {
            sendReminderNotification();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showConfirmationDialog(String message, Runnable onConfirm) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Confirm", (dialog, which) -> onConfirm.run())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showAboutPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_about, null);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setElevation(10);
        popupWindow.showAtLocation(findViewById(R.id.toolbar), Gravity.CENTER, 0, 0);
    }

    private void sendReminderNotification() {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter", "overdue");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Task Reminder")
                .setContentText("You have overdue tasks today")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
        Log.d(TAG, "Reminder notification sent");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Task Reminders";
            String description = "Notifications for task reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDashboard();
    }
}
