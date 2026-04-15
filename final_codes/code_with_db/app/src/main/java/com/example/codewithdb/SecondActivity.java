package com.example.codewithdb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private EditText etEdit1, etEdit2, etEdit3;
    private Button btnUpdate, btnDelete, btnBack;
    private DBHelper db;
    private int recordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        db = new DBHelper(this);

        etEdit1 = findViewById(R.id.etEdit1);
        etEdit2 = findViewById(R.id.etEdit2);
        etEdit3 = findViewById(R.id.etEdit3);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);

        // Get data from Intent
        recordId = getIntent().getIntExtra("ID", -1);
        etEdit1.setText(getIntent().getStringExtra("COL1"));
        etEdit2.setText(getIntent().getStringExtra("COL2"));
        etEdit3.setText(getIntent().getStringExtra("COL3"));

        // UPDATE
        btnUpdate.setOnClickListener(v -> {
            String val1 = etEdit1.getText().toString().trim();
            String val2 = etEdit2.getText().toString().trim();
            String val3 = etEdit3.getText().toString().trim();

            if (val1.isEmpty() || val2.isEmpty() || val3.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = db.updateData(recordId, val1, val2, val3);
            if (updated) {
                Toast.makeText(this, "Record Updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        });

        // DELETE
        btnDelete.setOnClickListener(v -> {
            int deleted = db.deleteData(recordId);
            if (deleted > 0) {
                Toast.makeText(this, "Record Deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Delete Failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}
