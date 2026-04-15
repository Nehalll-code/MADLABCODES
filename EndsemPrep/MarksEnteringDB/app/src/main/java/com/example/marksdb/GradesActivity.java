package com.example.marksdb;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GradesActivity extends AppCompatActivity {
    private static final String TAG = "GradesActivity";
    private String studentName;
    private DatabaseHelper dbHelper;

    private CheckBox[] checkBoxes = new CheckBox[5];
    private EditText[] editTexts = new EditText[5];
    private TextView[] tvNames = new TextView[5];
    private TextView[] tvCredits = new TextView[5];
    private int[] subjectCredits = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        Toolbar toolbar = findViewById(R.id.toolbarGrades);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        studentName = getIntent().getStringExtra("STUDENT_NAME");
        dbHelper = new DatabaseHelper(this);

        TextView tvTitle = findViewById(R.id.tvStudentNameDisplay);
        tvTitle.setText("Entering grades for: " + studentName);

        initViews();
        loadSettings();

        Button btnSubmit = findViewById(R.id.btnSubmitGrades);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAndSubmit();
            }
        });
    }

    private void initViews() {
        checkBoxes[0] = findViewById(R.id.cbSubj1);
        checkBoxes[1] = findViewById(R.id.cbSubj2);
        checkBoxes[2] = findViewById(R.id.cbSubj3);
        checkBoxes[3] = findViewById(R.id.cbSubj4);
        checkBoxes[4] = findViewById(R.id.cbSubj5);

        editTexts[0] = findViewById(R.id.etSubj1Marks);
        editTexts[1] = findViewById(R.id.etSubj2Marks);
        editTexts[2] = findViewById(R.id.etSubj3Marks);
        editTexts[3] = findViewById(R.id.etSubj4Marks);
        editTexts[4] = findViewById(R.id.etSubj5Marks);

        tvNames[0] = findViewById(R.id.tvSubj1Name);
        tvNames[1] = findViewById(R.id.tvSubj2Name);
        tvNames[2] = findViewById(R.id.tvSubj3Name);
        tvNames[3] = findViewById(R.id.tvSubj4Name);
        tvNames[4] = findViewById(R.id.tvSubj5Name);

        tvCredits[0] = findViewById(R.id.tvSubj1Credits);
        tvCredits[1] = findViewById(R.id.tvSubj2Credits);
        tvCredits[2] = findViewById(R.id.tvSubj3Credits);
        tvCredits[3] = findViewById(R.id.tvSubj4Credits);
        tvCredits[4] = findViewById(R.id.tvSubj5Credits);

        for (int i = 0; i < 5; i++) {
            final int index = i;
            checkBoxes[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    editTexts[index].setEnabled(isChecked);
                }
            });
        }
    }

    private void loadSettings() {
        Cursor cursor = dbHelper.getSettings();
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                if (i < 5) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SUBJ_NAME));
                    int credits = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SUBJ_CREDITS));
                    tvNames[i].setText(name);
                    tvCredits[i].setText(" (" + credits + ") ");
                    subjectCredits[i] = credits;
                    i++;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void confirmAndSubmit() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Submission")
                .setMessage("Do you want to calculate CGPA and save to DB?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveAndCalculate();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void saveAndCalculate() {
        double[] grades = new double[5];
        double weightedSum = 0;
        int totalCredits = 0;

        for (int i = 0; i < 5; i++) {
            // Requirement: All subjects must be checked
            if (!checkBoxes[i].isChecked()) {
                Toast.makeText(this, "All subjects must be checked to calculate CGPA", Toast.LENGTH_SHORT).show();
                return;
            }

            String gradeStr = editTexts[i].getText().toString().trim();
            if (gradeStr.isEmpty()) {
                Toast.makeText(this, "Please enter grade for " + tvNames[i].getText(), Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double val = Double.parseDouble(gradeStr);
                // Requirement: Must be between 0 and 10
                if (val < 0 || val > 10) {
                    Toast.makeText(this, "Grade for " + tvNames[i].getText() + " must be between 0 and 10", Toast.LENGTH_SHORT).show();
                    return;
                }
                grades[i] = val;
                weightedSum += val * subjectCredits[i];
                totalCredits += subjectCredits[i];
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid input for " + tvNames[i].getText(), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        double cgpa = (totalCredits > 0) ? (weightedSum / totalCredits) : 0;
        
        // Storing as int in DB for compatibility with existing schema, rounding the 0-10 grade.
        int[] intGrades = new int[5];
        for (int i = 0; i < 5; i++) {
            intGrades[i] = (int) Math.round(grades[i]);
        }

        dbHelper.insertMarks(studentName, intGrades);
        dbHelper.insertGPA(studentName, cgpa);

        Intent intent = new Intent(this, GpaResultActivity.class);
        intent.putExtra("STUDENT_NAME", studentName);
        intent.putExtra("GPA", cgpa);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
