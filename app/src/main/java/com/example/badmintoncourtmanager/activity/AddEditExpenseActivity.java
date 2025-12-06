package com.example.badmintoncourtmanager.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.database.DatabaseHelper;
import com.example.badmintoncourtmanager.model.Expense;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditExpenseActivity extends AppCompatActivity {
    private EditText editCategory, editAmount, editDescription;
    private Button buttonSave;
    private ImageButton buttonBack;
    private DatabaseHelper databaseHelper;

    private int expenseId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_expense);

        databaseHelper = new DatabaseHelper(this);

        editCategory = findViewById(R.id.edit_category);
        editAmount = findViewById(R.id.edit_amount);
        editDescription = findViewById(R.id.edit_description);
        buttonSave = findViewById(R.id.button_save);
        buttonBack = findViewById(R.id.button_back);

        // Check if editing existing expense
        if (getIntent().hasExtra("EXPENSE_ID")) {
            isEditMode = true;
            expenseId = getIntent().getIntExtra("EXPENSE_ID", -1);
            editCategory.setText(getIntent().getStringExtra("EXPENSE_CATEGORY"));
            editAmount.setText(String.valueOf(getIntent().getDoubleExtra("EXPENSE_AMOUNT", 0)));
            editDescription.setText(getIntent().getStringExtra("EXPENSE_DESCRIPTION"));
        }

        buttonBack.setOnClickListener(v -> finish());
        buttonSave.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        String category = editCategory.getText().toString().trim();
        String amountStr = editAmount.getText().toString().trim();
        String description = editDescription.getText().toString().trim();

        if (category.isEmpty()) {
            editCategory.setError("Vui lòng nhập danh mục");
            editCategory.requestFocus();
            return;
        }

        if (amountStr.isEmpty()) {
            editAmount.setError("Vui lòng nhập số tiền");
            editAmount.requestFocus();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                editAmount.setError("Số tiền phải lớn hơn 0");
                editAmount.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editAmount.setError("Số tiền không hợp lệ");
            editAmount.requestFocus();
            return;
        }

        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        if (isEditMode) {
            String date = getIntent().getStringExtra("EXPENSE_DATE");
            Expense expense = new Expense(expenseId, category, amount, date, description);
            databaseHelper.updateExpense(expense);
            Toast.makeText(this, "Đã cập nhật khoản chi", Toast.LENGTH_SHORT).show();
        } else {
            Expense expense = new Expense(category, amount, currentDate, description);
            databaseHelper.addExpense(expense);
            Toast.makeText(this, "Đã thêm khoản chi mới", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
