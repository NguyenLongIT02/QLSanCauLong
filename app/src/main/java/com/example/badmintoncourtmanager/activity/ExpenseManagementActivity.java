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
import com.example.badmintoncourtmanager.adapter.ExpenseAdapter;
import com.example.badmintoncourtmanager.database.DatabaseHelper;
import com.example.badmintoncourtmanager.model.Expense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ExpenseManagementActivity extends AppCompatActivity implements ExpenseAdapter.OnExpenseClickListener {
    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton fabAdd;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_management);

        databaseHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recycler_view);
        fabAdd = findViewById(R.id.fab_add);
        buttonBack = findViewById(R.id.button_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadExpenses();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ExpenseManagementActivity.this, AddEditExpenseActivity.class);
            startActivity(intent);
        });

        buttonBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExpenses();
    }

    private void loadExpenses() {
        List<Expense> expenses = databaseHelper.getAllExpenses();
        adapter = new ExpenseAdapter(this, expenses, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEditClick(Expense expense) {
        Intent intent = new Intent(this, AddEditExpenseActivity.class);
        intent.putExtra("EXPENSE_ID", expense.getId());
        intent.putExtra("EXPENSE_CATEGORY", expense.getCategory());
        intent.putExtra("EXPENSE_AMOUNT", expense.getAmount());
        intent.putExtra("EXPENSE_DATE", expense.getDate());
        intent.putExtra("EXPENSE_DESCRIPTION", expense.getDescription());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Expense expense) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa khoản chi này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    databaseHelper.deleteExpense(expense.getId());
                    Toast.makeText(this, "Đã xóa khoản chi", Toast.LENGTH_SHORT).show();
                    loadExpenses();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
