package com.example.megacombined;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;

public class CourseUserDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        TextView title = findViewById(R.id.formTitle);
        EditText etName = findViewById(R.id.etName);
        EditText etAge = findViewById(R.id.etAge);
        EditText etEmail = findViewById(R.id.etEmail);
        RadioGroup rg = findViewById(R.id.rgType);
        RadioButton rb1 = findViewById(R.id.rb1);
        RadioButton rb2 = findViewById(R.id.rb2);
        RadioButton rb3 = findViewById(R.id.rb3);
        TextView labelToggle = findViewById(R.id.labelToggle);
        ToggleButton toggle = findViewById(R.id.toggleFeature);
        Button btnNext = findViewById(R.id.btnNext);

        title.setText("Course Enrollment - Step 1");
        rb1.setText("Free");
        rb2.setText("Paid");
        rb3.setText("Certification");
        labelToggle.setText("Enable Course Access?");

        btnNext.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String ageStr = etAge.getText().toString();
            String email = etEmail.getText().toString();
            int selectedId = rg.getCheckedRadioButtonId();

            if (name.isEmpty() || ageStr.isEmpty() || email.isEmpty() || selectedId == -1) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int age = Integer.parseInt(ageStr);
            if (age < 18) {
                Toast.makeText(this, "Age must be 18 or above", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRb = findViewById(selectedId);
            String courseType = selectedRb.getText().toString();
            boolean access = toggle.isChecked();

            Intent intent = new Intent(this, CourseDetailsActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("age", age);
            intent.putExtra("email", email);
            intent.putExtra("courseType", courseType);
            intent.putExtra("access", access);
            startActivity(intent);
        });
    }
}
