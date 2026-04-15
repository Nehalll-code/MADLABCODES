package com.example.studentregistration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class SummaryActivity extends AppCompatActivity {

    private Student student;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        student = (Student) getIntent().getSerializableExtra("student_data");
        dbHelper = new DatabaseHelper(this);

        TextView tvName = findViewById(R.id.tvSummaryName);
        TextView tvAge = findViewById(R.id.tvSummaryAge);
        TextView tvEmail = findViewById(R.id.tvSummaryEmail);
        TextView tvCourse = findViewById(R.id.tvSummaryCourse);
        TextView tvStatus = findViewById(R.id.tvSummaryStatus);
        TextView tvDept = findViewById(R.id.tvSummaryDept);
        TextView tvSubjects = findViewById(R.id.tvSummarySubjects);
        TextView tvDate = findViewById(R.id.tvSummaryDate);
        TextView tvSection = findViewById(R.id.tvSummarySection);

        tvName.setText("Name: " + student.getName());
        tvAge.setText("Age: " + student.getAge());
        tvEmail.setText("Email: " + student.getEmail());
        tvCourse.setText("Course: " + student.getCourse());
        tvStatus.setText("Status: " + student.getStatus());
        tvDept.setText("Department: " + student.getDepartment());
        tvSubjects.setText("Subjects: " + student.getSubjects());
        tvDate.setText("Admission Date: " + student.getAdmissionDate());
        tvSection.setText("Section: " + student.getSection());

        MaterialButton btnSubmit = findViewById(R.id.btnSubmit);
        MaterialButton btnModify = findViewById(R.id.btnModify);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = dbHelper.addStudent(student);
                if (id != -1) {
                    Toast.makeText(SummaryActivity.this, "Student Registered Successfully!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(SummaryActivity.this, "Error in Registration", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
