package com.example.studentregistration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class BasicDetailsActivity extends AppCompatActivity {

    private TextInputEditText etName, etAge, etEmail;
    private RadioGroup rgCourse;
    private ToggleButton tbStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_details);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etEmail = findViewById(R.id.etEmail);
        rgCourse = findViewById(R.id.rgCourse);
        tbStatus = findViewById(R.id.tbStatus);
        MaterialButton btnNext = findViewById(R.id.btnNext1);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    Student student = new Student();
                    student.setName(etName.getText().toString().trim());
                    student.setAge(Integer.parseInt(etAge.getText().toString().trim()));
                    student.setEmail(etEmail.getText().toString().trim());

                    int selectedId = rgCourse.getCheckedRadioButtonId();
                    RadioButton rbSelected = findViewById(selectedId);
                    student.setCourse(rbSelected.getText().toString());

                    student.setStatus(tbStatus.isChecked() ? "Active" : "Inactive");

                    Intent intent = new Intent(BasicDetailsActivity.this, AcademicDetailsActivity.class);
                    intent.putExtra("student_data", student);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(etName.getText())) {
            etName.setError("Name is required");
            return false;
        }
        if (TextUtils.isEmpty(etAge.getText())) {
            etAge.setError("Age is required");
            return false;
        }
        String ageStr = etAge.getText().toString();
        int age = Integer.parseInt(ageStr);
        if (age < 15 || age > 60) {
            etAge.setError("Age must be between 15 and 60");
            return false;
        }
        if (TextUtils.isEmpty(etEmail.getText()) || !Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches()) {
            etEmail.setError("Valid email is required");
            return false;
        }
        if (rgCourse.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a course", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
