package com.example.management_with_db;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

/**
 * Activity to display resources with a Toggle for Edit/Delete actions.
 */
public class ViewListActivity extends AppCompatActivity {

    private ListView lvResources;
    private DatabaseHelper dbHelper;
    private ResourceAdapter adapter;
    private Switch swEnableEdit;
    private Button btnClearAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        lvResources = findViewById(R.id.lvResources);
        swEnableEdit = findViewById(R.id.swEnableEdit);
        btnClearAll = findViewById(R.id.btnClearAll);
        
        dbHelper = DatabaseHelper.getInstance(this);

        loadData();

        // Toggle logic for showing/hiding Edit and Delete buttons
        swEnableEdit.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (adapter != null) {
                adapter.setActionEnabled(isChecked);
            }
            // Show Clear All button only when editing is enabled
            btnClearAll.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        btnClearAll.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Warning")
                    .setMessage("This will delete ALL resources. Proceed?")
                    .setPositiveButton("Yes, Clear All", (dialog, which) -> {
                        dbHelper.clearAllResources();
                        loadData();
                        Toast.makeText(this, "Database Cleared", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void loadData() {
        List<DatabaseHelper.Resource> resourceList = dbHelper.getAllResources();
        adapter = new ResourceAdapter(this, resourceList);
        lvResources.setAdapter(adapter);
        
        // Ensure the adapter state matches the current switch state
        adapter.setActionEnabled(swEnableEdit.isChecked());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}
