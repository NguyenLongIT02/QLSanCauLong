package com.example.badmintoncourtmanager.activity;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.adapter.TransactionAdapter;
import com.example.badmintoncourtmanager.database.DatabaseHelper;
import com.example.badmintoncourtmanager.model.Transaction;

import java.util.List;

public class TransactionHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private DatabaseHelper databaseHelper;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        databaseHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recycler_view);
        buttonBack = findViewById(R.id.button_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadTransactions();

        buttonBack.setOnClickListener(v -> finish());
    }

    private void loadTransactions() {
        List<Transaction> transactions = databaseHelper.getAllTransactions();
        adapter = new TransactionAdapter(this, transactions);
        recyclerView.setAdapter(adapter);
    }
}
