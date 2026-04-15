package com.example.marksdb;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";
    private TextView tvWelcome;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Toolbar toolbar = findViewById(R.id.toolbarWelcome);
        setSupportActionBar(toolbar);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogout = findViewById(R.id.btnLogout);

        String username = getIntent().getStringExtra("USERNAME");
        tvWelcome.setText("Welcome, " + username + "!");

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Logging out");
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_enter_grades) {
            showStudentNameDialog();
            return true;
        } else if (id == R.id.menu_admin_view) {
            startActivity(new Intent(this, AdminActivity.class));
            return true;
        } else if (id == R.id.menu_db_view) {
            startActivity(new Intent(this, DBViewActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showStudentNameDialog() {
        final EditText etStudentName = new EditText(this);
        etStudentName.setHint("Enter Student Name");
        new AlertDialog.Builder(this)
                .setTitle("Student Input")
                .setView(etStudentName)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = etStudentName.getText().toString().trim();
                        if (!name.isEmpty()) {
                            Intent intent = new Intent(WelcomeActivity.this, GradesActivity.class);
                            intent.putExtra("STUDENT_NAME", name);
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
