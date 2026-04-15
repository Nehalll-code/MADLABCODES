package com.example.studentregistration;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    private RecyclerView rvStudents;
    private StudentAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        rvStudents = findViewById(R.id.rvStudents);
        dbHelper = new DatabaseHelper(this);

        List<Student> students = dbHelper.getAllStudents();
        
        adapter = new StudentAdapter(students);
        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        rvStudents.setAdapter(adapter);
    }
}
