package com.example.q3;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView contentTextView;
    private String originalContent;
    private String currentSearchKeyword = "";

    private static final String DIGITAL_TRANSFORMATION_TEXT = 
            "Digital transformation is the integration of digital technology into all areas of a business, fundamentally changing how you operate and deliver value to customers. " +
            "It is also a cultural change that requires organizations to continually challenge the status quo, experiment, and get comfortable with failure. " +
            "Digital transformation can involve many different technologies, but the most common ones are cloud computing, big data, artificial intelligence, and the internet of things. " +
            "A business may undertake digital transformation for several reasons. But by far, the most likely reason is that they have to: it is a survival issue. " +
            "In the wake of the pandemic, an organization's ability to adapt quickly to supply chain disruptions, time to market pressures, and rapidly changing customer expectations has become critical. " +
            "Customer experience is at the heart of digital transformation. " +
            "The goal is to use technology to create a better experience for customers and employees alike.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contentTextView = findViewById(R.id.contentTextView);
        originalContent = DIGITAL_TRANSFORMATION_TEXT;
        contentTextView.setText(originalContent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            showInputDialog("Search Keywords", keyword -> {
                currentSearchKeyword = keyword;
                searchContent(keyword);
            });
            return true;
        } else if (id == R.id.action_highlight) {
            showInputDialog("Highlight Phrase", this::highlightContent);
            return true;
        } else if (id == R.id.sort_alphabetical) {
            sortContent(true);
            return true;
        } else if (id == R.id.sort_relevance) {
            sortContent(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showInputDialog(String title, InputCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> callback.onInput(input.getText().toString()));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void searchContent(String keyword) {
        if (keyword.isEmpty()) {
            contentTextView.setText(originalContent);
            return;
        }
        if (originalContent.toLowerCase().contains(keyword.toLowerCase())) {
            Toast.makeText(this, "Found: " + keyword, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void highlightContent(String phrase) {
        if (phrase.isEmpty()) {
            contentTextView.setText(originalContent);
            return;
        }
        SpannableString spannableString = new SpannableString(contentTextView.getText().toString());
        String text = contentTextView.getText().toString().toLowerCase();
        String lowerPhrase = phrase.toLowerCase();
        int index = text.indexOf(lowerPhrase);
        while (index >= 0) {
            spannableString.setSpan(new BackgroundColorSpan(Color.YELLOW), index, index + phrase.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            index = text.indexOf(lowerPhrase, index + phrase.length());
        }
        contentTextView.setText(spannableString);
    }

    private void sortContent(boolean alphabetical) {
        String[] sentences = originalContent.split("\\. ");
        List<String> sentenceList = new ArrayList<>(Arrays.asList(sentences));

        if (alphabetical) {
            Collections.sort(sentenceList);
        } else {
            // Sort by relevance to currentSearchKeyword
            if (currentSearchKeyword.isEmpty()) {
                Toast.makeText(this, "Please search for a keyword first to sort by relevance", Toast.LENGTH_SHORT).show();
                return;
            }
            Collections.sort(sentenceList, (s1, s2) -> {
                int count1 = countOccurrences(s1, currentSearchKeyword);
                int count2 = countOccurrences(s2, currentSearchKeyword);
                return Integer.compare(count2, count1); // Descending order
            });
        }

        StringBuilder sortedText = new StringBuilder();
        for (String sentence : sentenceList) {
            sortedText.append(sentence).append(". ");
        }
        contentTextView.setText(sortedText.toString().trim());
    }

    private int countOccurrences(String text, String keyword) {
        int count = 0;
        int index = text.toLowerCase().indexOf(keyword.toLowerCase());
        while (index != -1) {
            count++;
            index = text.toLowerCase().indexOf(keyword.toLowerCase(), index + 1);
        }
        return count;
    }

    interface InputCallback {
        void onInput(String input);
    }
}