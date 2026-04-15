package com.example.megacombined;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ParkingListActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private List<Booking> list = new ArrayList<>();
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        TextView title = findViewById(R.id.listTitle);
        title.setText("Parking Bookings");

        db = new DatabaseHelper(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookingAdapter(list, booking -> {
            db.deleteParkingBooking(booking.getId());
            loadData();
        });
        recyclerView.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        list.clear();
        Cursor cursor = db.getAllParkingBookings();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DATE));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_P_V_TYPE));
                String area = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_P_AREA));
                list.add(new Booking(id, name, date, type, area, "PARKING"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
