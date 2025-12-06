package com.example.badmintoncourtmanager.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.database.DatabaseHelper;
import com.example.badmintoncourtmanager.model.Service;

public class AddEditServiceActivity extends AppCompatActivity {
    private EditText editServiceName, editServicePrice, editServiceUnit, editServiceQuantity, editServiceDescription;
    private Button buttonSave;
    private ImageButton buttonBack;
    private DatabaseHelper databaseHelper;

    private int serviceId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_service);

        databaseHelper = new DatabaseHelper(this);

        editServiceName = findViewById(R.id.edit_service_name);
        editServicePrice = findViewById(R.id.edit_service_price);
        editServiceUnit = findViewById(R.id.edit_service_unit);
        editServiceQuantity = findViewById(R.id.edit_service_quantity);
        editServiceDescription = findViewById(R.id.edit_service_description);
        buttonSave = findViewById(R.id.button_save);
        buttonBack = findViewById(R.id.button_back);

        // Check if editing existing service
        if (getIntent().hasExtra("SERVICE_ID")) {
            isEditMode = true;
            serviceId = getIntent().getIntExtra("SERVICE_ID", -1);
            editServiceName.setText(getIntent().getStringExtra("SERVICE_NAME"));
            editServicePrice.setText(String.valueOf(getIntent().getDoubleExtra("SERVICE_PRICE", 0)));
            editServiceUnit.setText(getIntent().getStringExtra("SERVICE_UNIT"));
            editServiceQuantity.setText(String.valueOf(getIntent().getIntExtra("SERVICE_QUANTITY", 0)));
            editServiceDescription.setText(getIntent().getStringExtra("SERVICE_DESCRIPTION"));
        }

        buttonBack.setOnClickListener(v -> finish());
        buttonSave.setOnClickListener(v -> saveService());
    }

    private void saveService() {
        String name = editServiceName.getText().toString().trim();
        String priceStr = editServicePrice.getText().toString().trim();
        String unit = editServiceUnit.getText().toString().trim();
        String quantityStr = editServiceQuantity.getText().toString().trim();
        String description = editServiceDescription.getText().toString().trim();

        if (name.isEmpty()) {
            editServiceName.setError("Vui lòng nhập tên dịch vụ");
            editServiceName.requestFocus();
            return;
        }

        if (priceStr.isEmpty()) {
            editServicePrice.setError("Vui lòng nhập giá");
            editServicePrice.requestFocus();
            return;
        }

        if (unit.isEmpty()) {
            editServiceUnit.setError("Vui lòng nhập đơn vị tính");
            editServiceUnit.requestFocus();
            return;
        }

        if (quantityStr.isEmpty()) {
            editServiceQuantity.setError("Vui lòng nhập số lượng");
            editServiceQuantity.requestFocus();
            return;
        }

        double price;
        int quantity;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                editServicePrice.setError("Giá phải lớn hơn 0");
                editServicePrice.requestFocus();
                return;
            }
            quantity = Integer.parseInt(quantityStr);
            if (quantity < 0) {
                editServiceQuantity.setError("Số lượng không thể âm");
                editServiceQuantity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editServicePrice.setError("Định dạng số không hợp lệ");
            return;
        }

        if (isEditMode) {
            Service service = new Service(serviceId, name, price, unit, quantity, description);
            databaseHelper.updateService(service);
            Toast.makeText(this, "Đã cập nhật dịch vụ", Toast.LENGTH_SHORT).show();
        } else {
            Service service = new Service(name, price, unit, quantity, description);
            databaseHelper.addService(service);
            Toast.makeText(this, "Đã thêm dịch vụ mới", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
