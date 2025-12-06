package com.example.badmintoncourtmanager.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.database.DatabaseHelper;
import com.example.badmintoncourtmanager.model.Field;

public class AddEditFieldActivity extends AppCompatActivity {
    private EditText editFieldName, editFieldPrice;
    private Button buttonSave;
    private ImageButton buttonBack;
    private DatabaseHelper databaseHelper;

    private int fieldId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_field);

        databaseHelper = new DatabaseHelper(this);

        editFieldName = findViewById(R.id.edit_field_name);
        editFieldPrice = findViewById(R.id.edit_field_price);
        buttonSave = findViewById(R.id.button_save);
        buttonBack = findViewById(R.id.button_back);

        // Check if editing existing field
        if (getIntent().hasExtra("FIELD_ID")) {
            isEditMode = true;
            fieldId = getIntent().getIntExtra("FIELD_ID", -1);
            editFieldName.setText(getIntent().getStringExtra("FIELD_NAME"));
            editFieldPrice.setText(String.valueOf(getIntent().getDoubleExtra("FIELD_PRICE", 0)));
        }

        buttonBack.setOnClickListener(v -> finish());
        buttonSave.setOnClickListener(v -> saveField());
    }

    private void saveField() {
        String name = editFieldName.getText().toString().trim();
        String priceStr = editFieldPrice.getText().toString().trim();

        if (name.isEmpty()) {
            editFieldName.setError("Vui lòng nhập tên sân");
            editFieldName.requestFocus();
            return;
        }

        if (priceStr.isEmpty()) {
            editFieldPrice.setError("Vui lòng nhập giá");
            editFieldPrice.requestFocus();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                editFieldPrice.setError("Giá phải lớn hơn 0");
                editFieldPrice.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editFieldPrice.setError("Giá không hợp lệ");
            editFieldPrice.requestFocus();
            return;
        }

        if (isEditMode) {
            Field field = new Field(fieldId, name, "Đơn", price, "Hoạt động", "");
            databaseHelper.updateField(field);
            Toast.makeText(this, "Đã cập nhật sân", Toast.LENGTH_SHORT).show();
        } else {
            Field field = new Field(name, "Đơn", price, "Hoạt động", "");
            databaseHelper.addField(field);
            Toast.makeText(this, "Đã thêm sân mới", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
