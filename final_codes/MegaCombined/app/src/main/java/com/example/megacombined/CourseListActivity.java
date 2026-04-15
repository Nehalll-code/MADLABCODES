package com.example.megacombined;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CourseListActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private List<Booking> list = new ArrayList<>();
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        TextView title = findViewById(R.id.listTitle);
        title.setText("Course Enrollments");

        db = new DatabaseHelper(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookingAdapter(list, booking -> {
            db.deleteCourseEnrollment(booking.getId());
            loadData();
        });
        recyclerView.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        list.clear();
        Cursor cursor = db.getAllCourseEnrollments();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DATE));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_C_TYPE));
                String instructor = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_C_INSTRUCTOR));
                list.add(new Booking(id, name, date, type, instructor, "COURSE"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
