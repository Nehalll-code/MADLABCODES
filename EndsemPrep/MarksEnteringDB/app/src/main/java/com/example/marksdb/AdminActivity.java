package com.example.marksdb;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AdminActivity extends AppCompatActivity {
    private static final String TAG = "AdminActivity";
    private DatabaseHelper dbHelper;
    private EditText[] etNames = new EditText[5];
    private EditText[] etCredits = new EditText[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.toolbarAdmin);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadCurrentSettings();

        Button btnSave = findViewById(R.id.btnSaveAdminChanges);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    private void initViews() {
        etNames[0] = findViewById(R.id.etAdminSubj1Name);
        etNames[1] = findViewById(R.id.etAdminSubj2Name);
        etNames[2] = findViewById(R.id.etAdminSubj3Name);
        etNames[3] = findViewById(R.id.etAdminSubj4Name);
        etNames[4] = findViewById(R.id.etAdminSubj5Name);

        etCredits[0] = findViewById(R.id.etAdminSubj1Credits);
        etCredits[1] = findViewById(R.id.etAdminSubj2Credits);
        etCredits[2] = findViewById(R.id.etAdminSubj3Credits);
        etCredits[3] = findViewById(R.id.etAdminSubj4Credits);
        etCredits[4] = findViewById(R.id.etAdminSubj5Credits);
    }

    private void loadCurrentSettings() {
        Cursor cursor = dbHelper.getSettings();
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                if (i < 5) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SUBJ_NAME));
                    int credits = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SUBJ_CREDITS));
                    etNames[i].setText(name);
                    etCredits[i].setText(String.valueOf(credits));
                    i++;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void saveChanges() {
        Log.d(TAG, "Saving admin changes");
        for (int i = 0; i < 5; i++) {
            String name = etNames[i].getText().toString().trim();
            String creditStr = etCredits[i].getText().toString().trim();
            if (name.isEmpty() || creditStr.isEmpty()) {
                Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
                return;
            }
            int credits = Integer.parseInt(creditStr);
            dbHelper.updateSetting(i + 1, name, credits);
        }
        Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
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
