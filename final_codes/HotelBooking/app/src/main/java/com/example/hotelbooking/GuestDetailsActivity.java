package com.example.hotelbooking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;

public class GuestDetailsActivity extends AppCompatActivity {

    private EditText etName, etAge, etEmail;
    private RadioGroup rgRoomType;
    private ToggleButton toggleConfirm;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_details);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etEmail = findViewById(R.id.etEmail);
        rgRoomType = findViewById(R.id.rgRoomType);
        toggleConfirm = findViewById(R.id.toggleConfirm);
        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(v -> {
            if (validateInput()) {
                Booking booking = new Booking();
                booking.setName(etName.getText().toString().trim());
                booking.setAge(Integer.parseInt(etAge.getText().toString().trim()));
                booking.setEmail(etEmail.getText().toString().trim());

                int selectedId = rgRoomType.getCheckedRadioButtonId();
                RadioButton rb = findViewById(selectedId);
                booking.setRoomType(rb.getText().toString());

                Intent intent = new Intent(GuestDetailsActivity.this, BookingDetailsActivity.class);
                intent.putExtra("booking", booking);
                startActivity(intent);
            }
        });
    }

    private boolean validateInput() {
        String name = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name is required");
            return false;
        }
        if (ageStr.isEmpty()) {
            etAge.setError("Age is required");
            return false;
        }
        int age = Integer.parseInt(ageStr);
        if (age < 18) {
            etAge.setError("Age must be at least 18");
            return false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            return false;
        }
        if (rgRoomType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a room type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!toggleConfirm.isChecked()) {
            Toast.makeText(this, "Please confirm the booking details", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
