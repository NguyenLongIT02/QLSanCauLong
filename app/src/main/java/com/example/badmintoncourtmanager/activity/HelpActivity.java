package com.example.badmintoncourtmanager.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.badmintoncourtmanager.R;

public class HelpActivity extends AppCompatActivity {
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(v -> finish());
    }
}
