package com.example.megacombined;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class VehicleListActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private List<Booking> bookingsList = new ArrayList<>();
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        TextView title = findViewById(R.id.listTitle);
        title.setText("Vehicle Service Bookings");

        db = new DatabaseHelper(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookingAdapter(bookingsList, booking -> {
            db.deleteVehicleBooking(booking.getId());
            loadData();
        });
        recyclerView.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        bookingsList.clear();
        Cursor cursor = db.getAllVehicleBookings();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DATE));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_V_TYPE));
                String slot = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_V_SLOT));
                bookingsList.add(new Booking(id, name, date, type, slot, "VEHICLE"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
