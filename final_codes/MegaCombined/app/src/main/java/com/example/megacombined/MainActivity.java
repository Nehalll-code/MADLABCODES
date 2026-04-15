package com.example.megacombined;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = findViewById(R.id.dashboardGrid);
        List<DashboardItem> items = new ArrayList<>();
        items.add(new DashboardItem("Vehicle Service", android.R.drawable.ic_menu_directions));
        items.add(new DashboardItem("Course Enroll", android.R.drawable.ic_menu_edit));
        items.add(new DashboardItem("Parking Slot", android.R.drawable.ic_menu_mylocation));
        items.add(new DashboardItem("All Bookings", android.R.drawable.ic_menu_view));
        items.add(new DashboardItem("About App", android.R.drawable.ic_menu_info_details));

        DashboardAdapter adapter = new DashboardAdapter(this, items);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: startActivity(new Intent(MainActivity.this, VehicleModuleActivity.class)); break;
                    case 1: startActivity(new Intent(MainActivity.this, CourseModuleActivity.class)); break;
                    case 2: startActivity(new Intent(MainActivity.this, ParkingModuleActivity.class)); break;
                    case 3: startActivity(new Intent(MainActivity.this, AllBookingsActivity.class)); break;
                    case 4: startActivity(new Intent(MainActivity.this, AboutActivity.class)); break;
                }
            }
        });
    }
}
