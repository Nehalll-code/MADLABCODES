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
            if ("VEHICLE".equals(booking.getType())) db.deleteVehicleBooking(booking.getId());
            else if ("COURSE".equals(booking.getType())) db.deleteCourseEnrollment(booking.getId());
            else if ("PARKING".equals(booking.getType())) db.deleteParkingBooking(booking.getId());
            loadAllData();
        });
        recyclerView.setAdapter(adapter);

        loadAllData();
    }

    private void loadAllData() {
        list.clear();
        
        // Load Vehicles
        Cursor c1 = db.getAllVehicleBookings();
        if (c1 != null) {
            if (c1.moveToFirst()) {
                int idIdx = c1.getColumnIndexOrThrow(DatabaseHelper.COL_ID);
                int nameIdx = c1.getColumnIndexOrThrow(DatabaseHelper.COL_NAME);
                int dateIdx = c1.getColumnIndexOrThrow(DatabaseHelper.COL_DATE);
                int typeIdx = c1.getColumnIndexOrThrow(DatabaseHelper.COL_V_TYPE);
                do {
                    list.add(new Booking(c1.getInt(idIdx), c1.getString(nameIdx), c1.getString(dateIdx), c1.getString(typeIdx), "Vehicle Service", "VEHICLE"));
                } while (c1.moveToNext());
            }
            c1.close();
        }

        // Load Courses
        Cursor c2 = db.getAllCourseEnrollments();
        if (c2 != null) {
            if (c2.moveToFirst()) {
                int idIdx = c2.getColumnIndexOrThrow(DatabaseHelper.COL_ID);
                int nameIdx = c2.getColumnIndexOrThrow(DatabaseHelper.COL_NAME);
                int dateIdx = c2.getColumnIndexOrThrow(DatabaseHelper.COL_DATE);
                int typeIdx = c2.getColumnIndexOrThrow(DatabaseHelper.COL_C_TYPE);
                do {
                    list.add(new Booking(c2.getInt(idIdx), c2.getString(nameIdx), c2.getString(dateIdx), c2.getString(typeIdx), "Course Enroll", "COURSE"));
                } while (c2.moveToNext());
            }
            c2.close();
        }

        // Load Parking
        Cursor c3 = db.getAllParkingBookings();
        if (c3 != null) {
            if (c3.moveToFirst()) {
                int idIdx = c3.getColumnIndexOrThrow(DatabaseHelper.COL_ID);
                int nameIdx = c3.getColumnIndexOrThrow(DatabaseHelper.COL_NAME);
                int dateIdx = c3.getColumnIndexOrThrow(DatabaseHelper.COL_DATE);
                int typeIdx = c3.getColumnIndexOrThrow(DatabaseHelper.COL_P_V_TYPE);
                do {
                    list.add(new Booking(c3.getInt(idIdx), c3.getString(nameIdx), c3.getString(dateIdx), c3.getString(typeIdx), "Parking Slot", "PARKING"));
                } while (c3.moveToNext());
            }
            c3.close();
        }

        adapter.notifyDataSetChanged();
    }
}
