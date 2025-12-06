package com.example.badmintoncourtmanager.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.database.DatabaseHelper;

import java.text.NumberFormat;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {
    private TextView textTotalRevenue, textPendingRevenue, textTotalExpenses, textProfit, textTotalBookings;
    private ImageButton buttonBack;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        databaseHelper = new DatabaseHelper(this);

        textTotalRevenue = findViewById(R.id.text_total_revenue);
        textPendingRevenue = findViewById(R.id.text_pending_revenue);
        textTotalExpenses = findViewById(R.id.text_total_expenses);
        textProfit = findViewById(R.id.text_profit);
        textTotalBookings = findViewById(R.id.text_total_bookings);
        buttonBack = findViewById(R.id.button_back);

        buttonBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStatistics();
    }

    private void loadStatistics() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        double totalRevenue = databaseHelper.getTotalRevenue();
        double pendingRevenue = databaseHelper.getPendingRevenue();
        double totalExpenses = databaseHelper.getTotalExpenses();
        double profit = totalRevenue - totalExpenses;
        int totalBookings = databaseHelper.getTotalBookings();

        textTotalRevenue.setText(currencyFormat.format(totalRevenue));
        textPendingRevenue.setText(currencyFormat.format(pendingRevenue));
        textTotalExpenses.setText(currencyFormat.format(totalExpenses));
        textProfit.setText(currencyFormat.format(profit));
        textTotalBookings.setText(String.valueOf(totalBookings));
    }
}
