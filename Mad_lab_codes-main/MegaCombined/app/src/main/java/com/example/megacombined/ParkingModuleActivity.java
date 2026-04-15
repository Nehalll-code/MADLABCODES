package com.example.megacombined;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ParkingModuleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_main);

        ImageView icon = findViewById(R.id.moduleIcon);
        TextView title = findViewById(R.id.moduleTitle);
        Button btnBook = findViewById(R.id.btnBook);
        Button btnView = findViewById(R.id.btnViewBookings);

        icon.setImageResource(android.R.drawable.ic_menu_mylocation);
        title.setText("Parking Slot Booking");

        btnBook.setOnClickListener(v -> startActivity(new Intent(this, ParkingUserDetailsActivity.class)));
        btnView.setOnClickListener(v -> startActivity(new Intent(this, ParkingListActivity.class)));
    }
}
