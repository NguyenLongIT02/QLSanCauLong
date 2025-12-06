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
import com.example.badmintoncourtmanager.adapter.ServiceAdapter;
import com.example.badmintoncourtmanager.database.DatabaseHelper;
import com.example.badmintoncourtmanager.model.Service;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ServiceManagementActivity extends AppCompatActivity implements ServiceAdapter.OnServiceClickListener {
    private RecyclerView recyclerView;
    private ServiceAdapter adapter;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton fabAdd;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_management);

        databaseHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recycler_view);
        fabAdd = findViewById(R.id.fab_add);
        buttonBack = findViewById(R.id.button_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadServices();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ServiceManagementActivity.this, AddEditServiceActivity.class);
            startActivity(intent);
        });

        buttonBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadServices();
    }

    private void loadServices() {
        List<Service> services = databaseHelper.getAllServices();
        adapter = new ServiceAdapter(this, services, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEditClick(Service service) {
        Intent intent = new Intent(this, AddEditServiceActivity.class);
        intent.putExtra("SERVICE_ID", service.getId());
        intent.putExtra("SERVICE_NAME", service.getName());
        intent.putExtra("SERVICE_PRICE", service.getPrice());
        intent.putExtra("SERVICE_UNIT", service.getUnit());
        intent.putExtra("SERVICE_QUANTITY", service.getQuantity());
        intent.putExtra("SERVICE_DESCRIPTION", service.getDescription());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Service service) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa dịch vụ \"" + service.getName() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    databaseHelper.deleteService(service.getId());
                    Toast.makeText(this, "Đã xóa dịch vụ", Toast.LENGTH_SHORT).show();
                    loadServices();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
