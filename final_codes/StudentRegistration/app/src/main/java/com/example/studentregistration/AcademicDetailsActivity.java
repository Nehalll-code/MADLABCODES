package com.example.studentregistration;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AcademicDetailsActivity extends AppCompatActivity {

    private Spinner spinnerDept;
    private ListView lvSubjects;
    private Button btnPickDate;
    private MaterialButton btnSection, btnNext;
    private Student student;
    private String selectedDate = "";
    private String selectedSection = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_details);

        student = (Student) getIntent().getSerializableExtra("student_data");

        spinnerDept = findViewById(R.id.spinnerDept);
        lvSubjects = findViewById(R.id.lvSubjects);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnSection = findViewById(R.id.btnSection);
        btnNext = findViewById(R.id.btnNext2);

        // Spinner Setup
        final String[] departments = {"School of Engineering", "School of Management", "School of Design"};
        ArrayAdapter<String> deptAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, departments);
        spinnerDept.setAdapter(deptAdapter);

        // ListView Multi-select Setup
        final String[] subjects = {"Data Structures", "Algorithms", "Operating Systems", "Database Management", "Computer Networks", "Software Engineering"};
        ArrayAdapter<String> subAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, subjects);
        lvSubjects.setAdapter(subAdapter);

        // Date Picker
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(AcademicDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        btnPickDate.setText(selectedDate);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Popup Menu for Section
        btnSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(AcademicDetailsActivity.this, btnSection);
                popup.getMenu().add("Section A");
                popup.getMenu().add("Section B");
                popup.getMenu().add("Section C");
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        selectedSection = item.getTitle().toString();
                        btnSection.setText(selectedSection);
                        return true;
                    }
                });
                popup.show();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    student.setDepartment(spinnerDept.getSelectedItem().toString());
                    
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < lvSubjects.getCount(); i++) {
                        if (lvSubjects.isItemChecked(i)) {
                            sb.append(subjects[i]).append(", ");
                        }
                    }
                    if (sb.length() > 2) sb.setLength(sb.length() - 2);
                    student.setSubjects(sb.toString());
                    
                    student.setAdmissionDate(selectedDate);
                    student.setSection(selectedSection);

                    Intent intent = new Intent(AcademicDetailsActivity.this, SummaryActivity.class);
                    intent.putExtra("student_data", student);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validateInputs() {
        if (lvSubjects.getCheckedItemCount() == 0) {
            Toast.makeText(this, "Select at least one subject", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Select admission date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedSection.isEmpty()) {
            Toast.makeText(this, "Select a section", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
