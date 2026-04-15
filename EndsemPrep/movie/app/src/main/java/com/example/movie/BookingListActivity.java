package com.example.movie;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class BookingListActivity extends AppCompatActivity {

    ListView listViewBookings;
    Button btnBackList;
    DatabaseHelper dbHelper;
    ArrayList<String> bookingList;
    ArrayList<String> bookingIds;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        Toolbar toolbar = findViewById(R.id.toolbar_list);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);
        listViewBookings = findViewById(R.id.listViewBookings);
        btnBackList = findViewById(R.id.btnBackList);

        loadBookings();

        registerForContextMenu(listViewBookings);

        btnBackList.setOnClickListener(v -> finish());
    }

    private void loadBookings() {
        bookingList = new ArrayList<>();
        bookingIds = new ArrayList<>();
        Cursor cursor = dbHelper.getAllBookings();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String category = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);
                bookingIds.add(id);
                bookingList.add("ID: " + id + " | " + category + " | " + date + " " + time);
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookingList);
        listViewBookings.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String id = bookingIds.get(info.position);

        int itemId = item.getItemId();
        if (itemId == R.id.update_booking) {
            Cursor cursor = dbHelper.getBookingById(id);
            if (cursor != null && cursor.moveToFirst()) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("ID", cursor.getString(0));
                intent.putExtra("CATEGORY", cursor.getString(1));
                intent.putExtra("DATE", cursor.getString(2));
                intent.putExtra("TIME", cursor.getString(3));
                intent.putExtra("TICKET_TYPE", cursor.getString(4));
                intent.putExtra("EXTRAS", cursor.getString(5));
                intent.putExtra("MODE", cursor.getString(6));
                cursor.close();
                startActivity(intent);
            }
            return true;
        } else if (itemId == R.id.delete_booking) {
            dbHelper.deleteBooking(id);
            Toast.makeText(this, "Booking Deleted", Toast.LENGTH_SHORT).show();
            loadBookings();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
