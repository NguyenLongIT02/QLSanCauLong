package com.example.badmintoncourtmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.adapter.AvailableFieldAdapter;
import com.example.badmintoncourtmanager.database.DatabaseHelper;
import com.example.badmintoncourtmanager.model.Field;

import java.util.List;

public class AvailableFieldsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private ImageButton buttonBack;
    private AvailableFieldAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_fields);

        databaseHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recycler_view);
        buttonBack = findViewById(R.id.button_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadAvailableFields();

        buttonBack.setOnClickListener(v -> finish());
    }

    private void loadAvailableFields() {
        List<Field> fields = databaseHelper.getAvailableFields();
        adapter = new AvailableFieldAdapter(this, fields, field -> {
            // Khi click vào sân, chuyển đến màn hình đặt sân
            Intent intent = new Intent(AvailableFieldsActivity.this, AddEditBookingActivity.class);
            intent.putExtra("field_id", field.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAvailableFields();
    }
}
