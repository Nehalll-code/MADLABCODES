package com.example.management_with_db;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

/**
 * Enhanced Adapter that supports a "Master Toggle" for Edit/Delete visibility.
 */
public class ResourceAdapter extends ArrayAdapter<DatabaseHelper.Resource> {

    private final Context context;
    private final List<DatabaseHelper.Resource> resources;
    private final DatabaseHelper dbHelper;
    private boolean isActionEnabled = false;

    public ResourceAdapter(@NonNull Context context, List<DatabaseHelper.Resource> resources) {
        super(context, R.layout.list_item_resource, resources);
        this.context = context;
        this.resources = resources;
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Updates the visibility state of Edit/Delete buttons.
     */
    public void setActionEnabled(boolean enabled) {
        this.isActionEnabled = enabled;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_resource, parent, false);
        }

        DatabaseHelper.Resource resource = resources.get(position);

        TextView tvItemName = convertView.findViewById(R.id.tvItemName);
        TextView tvItemDetails = convertView.findViewById(R.id.tvItemDetails);
        Button btnItemEdit = convertView.findViewById(R.id.btnItemEdit);
        Button btnItemDelete = convertView.findViewById(R.id.btnItemDelete);

        tvItemName.setText(resource.name);
        tvItemDetails.setText("Available: " + resource.availability + " | Date: " + resource.date);

        // Toggle visibility based on the Switch state
        if (isActionEnabled) {
            btnItemEdit.setVisibility(View.VISIBLE);
            btnItemDelete.setVisibility(View.VISIBLE);
        } else {
            btnItemEdit.setVisibility(View.GONE);
            btnItemDelete.setVisibility(View.GONE);
        }

        // Edit logic
        btnItemEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, RegistrationActivity.class);
            intent.putExtra("id", resource.id);
            intent.putExtra("name", resource.name);
            intent.putExtra("availability", resource.availability);
            intent.putExtra("date", resource.date);
            intent.putExtra("isEditMode", true);
            context.startActivity(intent);
        });

        // Proper Deletion logic
        btnItemDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Confirmation")
                    .setMessage("Are you sure you want to delete '" + resource.name + "'?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        if (dbHelper.deleteResource(resource.id)) {
                            resources.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Deletion Failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return convertView;
    }
}
