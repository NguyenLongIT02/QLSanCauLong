package com.example.badmintoncourtmanager.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.adapter.FieldAdapter;
import com.example.badmintoncourtmanager.database.DatabaseHelper;
import com.example.badmintoncourtmanager.model.Field;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FieldManagementActivity extends AppCompatActivity implements FieldAdapter.OnFieldClickListener {
    private RecyclerView recyclerView;
    private FieldAdapter adapter;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton fabAdd;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_management);

        databaseHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recycler_view);
        fabAdd = findViewById(R.id.fab_add);
        buttonBack = findViewById(R.id.button_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadFields();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(FieldManagementActivity.this, AddEditFieldActivity.class);
            startActivity(intent);
        });

        buttonBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFields();
    }

    private void loadFields() {
        List<Field> fields = databaseHelper.getAllFields();
        adapter = new FieldAdapter(this, fields, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEditClick(Field field) {
        Intent intent = new Intent(this, AddEditFieldActivity.class);
        intent.putExtra("FIELD_ID", field.getId());
        intent.putExtra("FIELD_NAME", field.getName());
        intent.putExtra("FIELD_TYPE", field.getType());
        intent.putExtra("FIELD_PRICE", field.getPricePerHour());
        intent.putExtra("FIELD_STATUS", field.getStatus());
        intent.putExtra("FIELD_DESCRIPTION", field.getDescription());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Field field) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sân \"" + field.getName() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    databaseHelper.deleteField(field.getId());
                    Toast.makeText(this, "Đã xóa sân", Toast.LENGTH_SHORT).show();
                    loadFields();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onStatusChange(Field field, boolean isActive) {
        String newStatus = isActive ? "Hoạt động" : "Bảo trì";
        boolean success = databaseHelper.updateFieldStatus(field.getId(), newStatus);
        if (success) {
            Toast.makeText(this, "Đã cập nhật: " + newStatus, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
            loadFields(); // Revert
        }
    }
}
