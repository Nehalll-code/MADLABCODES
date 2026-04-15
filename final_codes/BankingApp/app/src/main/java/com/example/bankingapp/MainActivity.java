package com.example.bankingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final String[] labels = {"Open Account", "View Customers", "Services Info", "About Bank"};
    private final int[] icons = {
            android.R.drawable.ic_menu_edit,
            android.R.drawable.ic_menu_view,
            android.R.drawable.ic_menu_info_details,
            android.R.drawable.ic_menu_help
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = findViewById(R.id.dashboardGridView);
        DashboardAdapter adapter = new DashboardAdapter(this, labels, icons);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(MainActivity.this, PersonalDetailsActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(MainActivity.this, CustomerListActivity.class));
                    break;
                case 2:
                case 3:
                    Toast.makeText(MainActivity.this, "Feature coming soon!", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}
