package com.example.b1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etStudentName, etMarks;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    Spinner spCourse;
    DatePicker datePicker;
    Button btnSubmit, btnViewAll;
    DatabaseHelper myDb;
    String editId = null; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        etStudentName = findViewById(R.id.etStudentName);
        etMarks = findViewById(R.id.etMarks);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        spCourse = findViewById(R.id.spCourse);
        datePicker = findViewById(R.id.datePicker);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnViewAll = findViewById(R.id.btnViewAll);

        // Check if we are in Edit Mode
        Intent intent = getIntent();
        if (intent.hasExtra("ID")) {
            editId = intent.getStringExtra("ID");
            etStudentName.setText(intent.getStringExtra("NAME"));
            etMarks.setText(intent.getStringExtra("MARKS"));
            
            String gender = intent.getStringExtra("GENDER");
            if ("Male".equals(gender)) rbMale.setChecked(true);
            else if ("Female".equals(gender)) rbFemale.setChecked(true);

            String course = intent.getStringExtra("COURSE");
            @SuppressWarnings("unchecked")
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spCourse.getAdapter();
            int spinnerPosition = adapter.getPosition(course);
            spCourse.setSelection(spinnerPosition);

            String date = intent.getStringExtra("DATE");
            if (date != null) {
                String[] dateParts = date.split("-");
                if (dateParts.length == 3) {
                    int day = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]) - 1;
                    int year = Integer.parseInt(dateParts[2]);
                    datePicker.updateDate(year, month, day);
                }
            }

            btnSubmit.setText("Update Admission");
        }

        btnSubmit.setOnClickListener(v -> submitData());
        
        btnViewAll.setOnClickListener(v -> {
            Intent intentView = new Intent(MainActivity.this, SummaryActivity.class);
            startActivity(intentView);
        });
    }

    private void submitData() {
        String name = etStudentName.getText().toString().trim();
        String marks = etMarks.getText().toString().trim();
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        
        if (name.isEmpty() || marks.isEmpty() || selectedGenderId == -1) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton rbSelected = findViewById(selectedGenderId);
        String gender = rbSelected.getText().toString();
        String course = spCourse.getSelectedItem().toString();
        
        String date = datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();

        boolean isSuccess;
        if (editId == null) {
            // INSERT
            isSuccess = myDb.insertData(name, marks, gender, course, date);
            if (isSuccess) Toast.makeText(this, "Admission Data Inserted", Toast.LENGTH_SHORT).show();
        } else {
            // UPDATE
            isSuccess = myDb.updateData(editId, name, marks, gender, course, date);
            if (isSuccess) Toast.makeText(this, "Admission Data Updated", Toast.LENGTH_SHORT).show();
        }

        if (isSuccess) {
            Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Operation Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
