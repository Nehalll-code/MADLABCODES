package com.example.q1;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView contentTextView;
    private LinearLayout trainersLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentTextView = findViewById(R.id.contentTextView);
        trainersLayout = findViewById(R.id.trainersLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Hide trainers layout by default, show it only for trainers menu
        trainersLayout.setVisibility(View.GONE);
        contentTextView.setVisibility(View.VISIBLE);

        if (id == R.id.action_home) {
            contentTextView.setText(getString(R.string.welcome_msg));
            return true;
        } else if (id == R.id.action_about) {
            contentTextView.setText(getString(R.string.about_content));
            return true;
        } else if (id == R.id.action_contact) {
            contentTextView.setText(getString(R.string.contact_content));
            return true;
        } else if (id == R.id.menu_workout) {
            contentTextView.setText(getString(R.string.workout_plans_content));
            return true;
        } else if (id == R.id.menu_trainers) {
            contentTextView.setText("Meet Our Expert Team:");
            trainersLayout.setVisibility(View.VISIBLE);
            return true;
        } else if (id == R.id.menu_membership) {
            contentTextView.setText(getString(R.string.membership_content));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}