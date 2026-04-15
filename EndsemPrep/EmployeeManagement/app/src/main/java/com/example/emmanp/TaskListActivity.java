package com.example.emmanp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TaskListActivity extends AppCompatActivity {

    private static final String TAG = "TaskListActivity";
    private DatabaseHelper dbHelper;
    private ListView lvTasks;
    private TableLayout tlTasks;
    private GridView gvTasks;
    private View svTableView;
    private String currentView = "List"; // List, Table, Grid
    private String currentSort = DatabaseHelper.COLUMN_TASK_ID + " DESC";
    private String currentFilter = null;

    private ArrayList<Task> taskList = new ArrayList<>();
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbHelper = new DatabaseHelper(this);
        lvTasks = findViewById(R.id.lv_tasks);
        tlTasks = findViewById(R.id.tl_tasks);
        gvTasks = findViewById(R.id.gv_tasks);
        svTableView = findViewById(R.id.sv_table_view);

        String filterType = getIntent().getStringExtra("filter");
        if (filterType != null) {
            if (filterType.equals("due_today")) {
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                currentFilter = DatabaseHelper.COLUMN_TASK_DEADLINE + " = '" + today + "'";
            } else if (filterType.equals("overdue")) {
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                currentFilter = DatabaseHelper.COLUMN_TASK_DEADLINE + " < '" + today + "' AND " + DatabaseHelper.COLUMN_TASK_STATUS + " = 'Pending'";
            }
        }

        adapter = new TaskAdapter();
        lvTasks.setAdapter(adapter);
        gvTasks.setAdapter(adapter);

        lvTasks.setOnItemClickListener((parent, view, position, id) -> openTaskDetail(taskList.get(position).id));
        gvTasks.setOnItemClickListener((parent, view, position, id) -> openTaskDetail(taskList.get(position).id));

        loadTasks();
    }

    private void loadTasks() {
        Log.d(TAG, "Loading tasks with sort: " + currentSort + " and filter: " + currentFilter);
        taskList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_TASKS, null, currentFilter, null, null, null, currentSort);

        while (cursor.moveToNext()) {
            Task task = new Task();
            task.id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_ID));
            task.title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TITLE));
            task.employee = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_EMPLOYEE));
            task.priority = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_PRIORITY));
            task.deadline = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DEADLINE));
            task.status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_STATUS));
            taskList.add(task);
        }
        cursor.close();

        updateUI();
    }

    private void updateUI() {
        lvTasks.setVisibility(currentView.equals("List") ? View.VISIBLE : View.GONE);
        svTableView.setVisibility(currentView.equals("Table") ? View.VISIBLE : View.GONE);
        gvTasks.setVisibility(currentView.equals("Grid") ? View.VISIBLE : View.GONE);

        adapter.notifyDataSetChanged();

        if (currentView.equals("Table")) {
            populateTable();
        }
    }

    private void populateTable() {
        // Clear all but header
        int childCount = tlTasks.getChildCount();
        if (childCount > 1) {
            tlTasks.removeViews(1, childCount - 1);
        }

        for (Task task : taskList) {
            TableRow row = new TableRow(this);
            row.setOnClickListener(v -> openTaskDetail(task.id));
            
            row.addView(createTableCell(task.title));
            row.addView(createTableCell(task.employee));
            row.addView(createTableCell(task.priority));
            row.addView(createTableCell(task.deadline));
            row.addView(createTableCell(task.status));
            
            tlTasks.addView(row);
        }
    }

    private TextView createTableCell(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(8, 8, 8, 8);
        return tv;
    }

    private void openTaskDetail(int taskId) {
        Log.d(TAG, "Opening task detail for ID: " + taskId);
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra("task_id", taskId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_date) {
            currentSort = DatabaseHelper.COLUMN_TASK_DEADLINE + " ASC";
            loadTasks();
            Toast.makeText(this, "Sorted by date", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Sort by date triggered");
            return true;
        } else if (id == R.id.action_sort_priority) {
            currentSort = "CASE " + DatabaseHelper.COLUMN_TASK_PRIORITY + " WHEN 'High' THEN 1 WHEN 'Medium' THEN 2 WHEN 'Low' THEN 3 END";
            loadTasks();
            Toast.makeText(this, "Sorted by priority", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Sort by priority triggered");
            return true;
        } else if (id == R.id.action_delete_completed) {
            new AlertDialog.Builder(this)
                    .setMessage("Delete all completed tasks?")
                    .setPositiveButton("Confirm", (dialog, which) -> {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete(DatabaseHelper.TABLE_TASKS, DatabaseHelper.COLUMN_TASK_STATUS + " = 'Completed'", null);
                        Toast.makeText(this, "Completed tasks deleted", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Completed tasks deleted from database");
                        loadTasks();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        } else if (id == R.id.action_filter_overdue) {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            currentFilter = DatabaseHelper.COLUMN_TASK_DEADLINE + " < '" + today + "' AND " + DatabaseHelper.COLUMN_TASK_STATUS + " = 'Pending'";
            loadTasks();
            Toast.makeText(this, "Showing overdue tasks", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Filter overdue triggered");
            return true;
        } else if (id == R.id.action_switch_view) {
            if (currentView.equals("List")) currentView = "Table";
            else if (currentView.equals("Table")) currentView = "Grid";
            else currentView = "List";
            
            updateUI();
            Toast.makeText(this, "Switched to " + currentView + " View", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Switched view to: " + currentView);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private class Task {
        int id;
        String title, employee, priority, deadline, status;
    }

    private class TaskAdapter extends BaseAdapter {
        @Override
        public int getCount() { return taskList.size(); }
        @Override
        public Object getItem(int position) { return taskList.get(position); }
        @Override
        public long getItemId(int position) { return taskList.get(position).id; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Task task = taskList.get(position);
            if (currentView.equals("Grid")) {
                if (convertView == null || !(convertView instanceof CardView)) {
                    convertView = LayoutInflater.from(TaskListActivity.this).inflate(R.layout.item_task_grid, parent, false);
                }
                TextView tvTitle = convertView.findViewById(R.id.tv_grid_title);
                CardView card = (CardView) convertView;
                tvTitle.setText(task.title);
                
                int color = Color.GREEN;
                if (task.priority.equals("High")) color = Color.RED;
                else if (task.priority.equals("Medium")) color = Color.YELLOW;
                card.setCardBackgroundColor(color);
                
                return convertView;
            } else {
                if (convertView == null || convertView instanceof CardView) {
                    convertView = LayoutInflater.from(TaskListActivity.this).inflate(android.R.layout.simple_list_item_2, parent, false);
                }
                TextView tv1 = convertView.findViewById(android.R.id.text1);
                TextView tv2 = convertView.findViewById(android.R.id.text2);
                tv1.setText(task.title);
                tv2.setText(task.employee + " - " + task.status);
                return convertView;
            }
        }
    }
}
