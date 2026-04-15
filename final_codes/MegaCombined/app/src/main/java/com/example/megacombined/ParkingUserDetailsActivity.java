package com.example.megacombined;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;

public class ParkingUserDetailsActivity extends AppCompatActivity {

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

        title.setText("Parking Booking - Step 1");
        rb1.setText("2-Wheeler");
        rb2.setText("4-Wheeler");
        rb3.setText("Heavy Vehicle");
        labelToggle.setText("Mark Slot Active?");

        btnNext.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            int selectedId = rg.getCheckedRadioButtonId();

            if (name.isEmpty()) {
                etName.setError("Name is required");
                return;
            }
            if (ageStr.isEmpty()) {
                etAge.setError("Age is required");
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                etAge.setError("Invalid age");
                return;
            }

            if (age < 18 || age > 100) {
                etAge.setError("Age must be between 18 and 100");
                return;
            }

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Valid email is required");
                return;
            }

            if (selectedId == -1) {
                Toast.makeText(this, "Please select vehicle type", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRb = findViewById(selectedId);
            String vehicleType = selectedRb.getText().toString();
            boolean active = toggle.isChecked();

            Intent intent = new Intent(this, ParkingDetailsActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("age", age);
            intent.putExtra("email", email);
            intent.putExtra("vehicleType", vehicleType);
            intent.putExtra("active", active);
            startActivity(intent);
        });
    }
}
