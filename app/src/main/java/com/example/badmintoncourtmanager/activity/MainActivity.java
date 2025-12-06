package com.example.badmintoncourtmanager.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.badmintoncourtmanager.R;

public class MainActivity extends AppCompatActivity {
        private LinearLayout cardFieldManagement, cardServiceManagement, cardBookingManagement;
        private LinearLayout cardAvailableFields, cardExpenseManagement, cardStatistics, cardHelp;
        private ImageButton buttonLogout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                // Initialize views
                cardFieldManagement = findViewById(R.id.card_field_management);
                cardServiceManagement = findViewById(R.id.card_service_management);
                cardBookingManagement = findViewById(R.id.card_booking_management);
                cardAvailableFields = findViewById(R.id.card_available_fields);
                cardExpenseManagement = findViewById(R.id.card_expense_management);
                cardStatistics = findViewById(R.id.card_statistics);
                cardHelp = findViewById(R.id.card_help);
                buttonLogout = findViewById(R.id.button_logout);

                // Set click listeners
                cardFieldManagement
                                .setOnClickListener(v -> startActivity(
                                                new Intent(MainActivity.this, FieldManagementActivity.class)));

                cardServiceManagement
                                .setOnClickListener(v -> startActivity(
                                                new Intent(MainActivity.this, ServiceManagementActivity.class)));

                cardBookingManagement
                                .setOnClickListener(v -> startActivity(
                                                new Intent(MainActivity.this, BookingManagementActivity.class)));

                cardAvailableFields
                                .setOnClickListener(v -> startActivity(
                                                new Intent(MainActivity.this, AvailableFieldsActivity.class)));

                cardExpenseManagement
                                .setOnClickListener(v -> startActivity(
                                                new Intent(MainActivity.this, ExpenseManagementActivity.class)));

                cardStatistics.setOnClickListener(
                                v -> startActivity(new Intent(MainActivity.this, StatisticsActivity.class)));

                cardHelp.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HelpActivity.class)));

                buttonLogout.setOnClickListener(v -> showLogoutDialog());
        }

        private void showLogoutDialog() {
                new AlertDialog.Builder(this)
                                .setTitle("Đăng xuất")
                                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                })
                                .setNegativeButton("Hủy", null)
                                .show();
        }
}
