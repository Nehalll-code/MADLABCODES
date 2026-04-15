package com.example.bankingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalDetailsActivity extends AppCompatActivity {

    private EditText etName, etAge, etEmail;
    private RadioGroup rgAccountType;
    private ToggleButton toggleKyc;
    private Button btnNext;
    private Customer customer;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etEmail = findViewById(R.id.etEmail);
        rgAccountType = findViewById(R.id.rgAccountType);
        toggleKyc = findViewById(R.id.toggleKyc);
        btnNext = findViewById(R.id.btnNextPersonal);

        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            customer = (Customer) getIntent().getSerializableExtra("customer");
            if (customer != null) {
                etName.setText(customer.getName());
                etAge.setText(String.valueOf(customer.getAge()));
                etEmail.setText(customer.getEmail());
                toggleKyc.setChecked(customer.isKycVerified());
                
                if ("Savings Account".equals(customer.getAccountType())) {
                    rgAccountType.check(R.id.rbSavings);
                } else if ("Current Account".equals(customer.getAccountType())) {
                    rgAccountType.check(R.id.rbCurrent);
                } else if ("Fixed Deposit".equals(customer.getAccountType())) {
                    rgAccountType.check(R.id.rbFixed);
                }
            }
        }

        btnNext.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (name.isEmpty() || ageStr.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int age = Integer.parseInt(ageStr);
            if (age < 18) {
                Toast.makeText(this, "Age must be at least 18", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedId = rgAccountType.getCheckedRadioButtonId();
            RadioButton rb = findViewById(selectedId);
            String accountType = rb.getText().toString();

            if (customer == null) {
                customer = new Customer();
            }
            customer.setName(name);
            customer.setAge(age);
            customer.setEmail(email);
            customer.setAccountType(accountType);
            customer.setKycVerified(toggleKyc.isChecked());

            Intent intent = new Intent(this, AccountDetailsActivity.class);
            intent.putExtra("customer", customer);
            intent.putExtra("isEdit", isEdit);
            startActivity(intent);
        });
    }
}
