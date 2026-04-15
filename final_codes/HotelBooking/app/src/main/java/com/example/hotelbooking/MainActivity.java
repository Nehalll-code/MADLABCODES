package com.example.hotelbooking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GridView dashboardGridView;
    private String[] labels = {"Book Room", "View Bookings", "Hotel Info", "Contact / Help"};
    private int[] icons = {
            android.R.drawable.ic_menu_add,
            android.R.drawable.ic_menu_view,
            android.R.drawable.ic_menu_info_details,
            android.R.drawable.ic_menu_call
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dashboardGridView = findViewById(R.id.dashboardGridView);
        DashboardAdapter adapter = new DashboardAdapter(this, labels, icons);
        dashboardGridView.setAdapter(adapter);

        dashboardGridView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(MainActivity.this, GuestDetailsActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(MainActivity.this, BookingListActivity.class));
                    break;
                case 2:
                    Toast.makeText(this, "Premium Hotel v1.0", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(this, "Contact: support@hotel.com", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}
