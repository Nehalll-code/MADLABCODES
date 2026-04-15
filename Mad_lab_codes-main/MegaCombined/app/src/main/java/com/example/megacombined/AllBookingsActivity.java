package com.example.megacombined;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AllBookingsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private List<Booking> list = new ArrayList<>();
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        TextView title = findViewById(R.id.listTitle);
        title.setText("All Platform Bookings");

        db = new DatabaseHelper(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookingAdapter(list, booking -> {
            if (booking.getType().equals("VEHICLE")) db.deleteVehicleBooking(booking.getId());
            else if (booking.getType().equals("COURSE")) db.deleteCourseEnrollment(booking.getId());
            else if (booking.getType().equals("PARKING")) db.deleteParkingBooking(booking.getId());
            loadAllData();
        });
        recyclerView.setAdapter(adapter);

        loadAllData();
    }

    private void loadAllData() {
        list.clear();
        
        // Load Vehicles
        Cursor c1 = db.getAllVehicleBookings();
        if (c1.moveToFirst()) {
            do {
                list.add(new Booking(c1.getInt(0), c1.getString(1), c1.getString(8), c1.getString(4), "Vehicle Service", "VEHICLE"));
            } while (c1.moveToNext());
        }
        c1.close();

        // Load Courses
        Cursor c2 = db.getAllCourseEnrollments();
        if (c2.moveToFirst()) {
            do {
                list.add(new Booking(c2.getInt(0), c2.getString(1), c2.getString(8), c2.getString(4), "Course Enroll", "COURSE"));
            } while (c2.moveToNext());
        }
        c2.close();

        // Load Parking
        Cursor c3 = db.getAllParkingBookings();
        if (c3.moveToFirst()) {
            do {
                list.add(new Booking(c3.getInt(0), c3.getString(1), c3.getString(8), c3.getString(4), "Parking Slot", "PARKING"));
            } while (c3.moveToNext());
        }
        c3.close();

        adapter.notifyDataSetChanged();
    }
}
