package com.example.badmintoncourtmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.adapter.ServiceUsageAdapter;
import com.example.badmintoncourtmanager.database.DatabaseHelper;
import com.example.badmintoncourtmanager.model.Booking;
import com.example.badmintoncourtmanager.model.ServiceUsage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingDetailActivity extends AppCompatActivity {
    private TextView tvBookingId, tvFieldName, tvCustomerName, tvCustomerPhone;
    private TextView tvDate, tvTimeRange, tvStatus, tvTotalPrice;
    private RecyclerView rvServices;
    private LinearLayout layoutServices;
    private Button btnEdit, btnDelete, btnChangeStatus;
    private ImageButton btnBack;

    private DatabaseHelper dbHelper;
    private int bookingId;
    private Booking booking;
    private List<ServiceUsage> serviceUsages;
    private ServiceUsageAdapter serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_booking_detail);

            initViews();
            dbHelper = new DatabaseHelper(this);

            bookingId = getIntent().getIntExtra("booking_id", -1);
            if (bookingId == -1) {
                Toast.makeText(this, "Không tìm thấy thông tin đặt sân", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            loadBookingData();
            setupListeners();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khởi tạo: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews() {
        tvBookingId = findViewById(R.id.tvBookingId);
        tvFieldName = findViewById(R.id.tvFieldName);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        tvDate = findViewById(R.id.tvDate);
        tvTimeRange = findViewById(R.id.tvTimeRange);
        tvStatus = findViewById(R.id.tvStatus);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        rvServices = findViewById(R.id.rvServices);
        layoutServices = findViewById(R.id.layoutServices);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnChangeStatus = findViewById(R.id.btnChangeStatus);
        btnBack = findViewById(R.id.btnBack);
    }

    private void loadBookingData() {
        booking = dbHelper.getBookingById(bookingId);
        if (booking == null) {
            Toast.makeText(this, "Không tìm thấy thông tin đặt sân", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvBookingId.setText("Mã đặt sân: #" + booking.getId());
        tvFieldName.setText(booking.getFieldName());
        tvCustomerName.setText("Khách hàng: " + booking.getCustomerName());
        tvCustomerPhone.setText(booking.getCustomerPhone());

        // Format Date nicely if possible, assume it's stored as dd/MM/yyyy
        tvDate.setText("Ngày: " + booking.getDate());

        // Calculate hours properly
        double hours = booking.getHours();
        if (hours == 0) {
            hours = calculateHours(booking.getStartTime(), booking.getEndTime());
        }

        String timeDisplay = String.format("%s - %s (%.1fh)", booking.getStartTime(), booking.getEndTime(), hours);
        tvTimeRange.setText(timeDisplay);

        tvStatus.setText(booking.getStatus());
        tvTotalPrice.setText(String.format("%,.0f đ", booking.getTotalPrice()));

        updateStatusColor();

        serviceUsages = dbHelper.getServiceUsagesByBookingId(bookingId);
        if (serviceUsages.isEmpty()) {
            layoutServices.setVisibility(View.GONE);
        } else {
            layoutServices.setVisibility(View.VISIBLE);
            setupRecyclerView();
        }

        updateButtonVisibility();
    }

    // Helper to calculate hours from HH:mm strings
    private double calculateHours(String start, String end) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date dateStart = sdf.parse(start);
            Date dateEnd = sdf.parse(end);
            long diff = dateEnd.getTime() - dateStart.getTime();
            return (double) diff / (1000 * 60 * 60);
        } catch (Exception e) {
            return 0;
        }
    }

    private void setupRecyclerView() {
        serviceAdapter = new ServiceUsageAdapter(this, serviceUsages, null);
        rvServices.setLayoutManager(new LinearLayoutManager(this));
        rvServices.setAdapter(serviceAdapter);
    }

    private void updateStatusColor() {
        int color;
        int bgResCb = R.drawable.bg_status_rounded; // Default rounded bg

        // We will tint the background or change text color
        // Let's change text color and distinct background tint if needed,
        // but simple text color on light bg is cleaner.

        switch (booking.getStatus()) {
            case "Đã đặt":
                color = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
                break;
            case "Đã thanh toán":
                color = ContextCompat.getColor(this, android.R.color.holo_green_dark);
                break;
            case "Đã hủy":
                color = ContextCompat.getColor(this, android.R.color.holo_red_dark);
                break;
            default:
                color = ContextCompat.getColor(this, android.R.color.black);
        }
        tvStatus.setTextColor(color);
        // Note: background drawable is set in XML layout
    }

    private void updateButtonVisibility() {
        if (booking == null)
            return;

        String status = booking.getStatus();

        if ("Đã thanh toán".equals(status)) {
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.VISIBLE);
            btnChangeStatus.setVisibility(View.GONE);
        } else {
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            btnChangeStatus.setVisibility(View.VISIBLE);
        }
    }

    private void setupListeners() {
        btnEdit.setOnClickListener(v -> editBooking());
        btnDelete.setOnClickListener(v -> confirmDelete());
        btnChangeStatus.setOnClickListener(v -> showStatusDialog());
        btnBack.setOnClickListener(v -> finish());
    }

    private void editBooking() {
        Intent intent = new Intent(this, AddEditBookingActivity.class);
        intent.putExtra("booking_id", bookingId);
        startActivity(intent);
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa đặt sân này?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteBooking())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteBooking() {
        boolean success = dbHelper.deleteBooking(bookingId);
        if (success) {
            Toast.makeText(this, "Đã xóa đặt sân", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
        }
    }

    private void showStatusDialog() {
        String[] statuses = { "Đã đặt", "Đã thanh toán", "Đã hủy" };
        int currentIndex = 0;
        for (int i = 0; i < statuses.length; i++) {
            if (statuses[i].equals(booking.getStatus())) {
                currentIndex = i;
                break;
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("Chọn trạng thái")
                .setSingleChoiceItems(statuses, currentIndex, (dialog, which) -> {
                    String newStatus = statuses[which];
                    updateStatus(newStatus);
                    dialog.dismiss();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void updateStatus(String newStatus) {
        booking.setStatus(newStatus);
        boolean success = dbHelper.updateBookingStatus(bookingId, newStatus);
        if (success) {
            tvStatus.setText(newStatus);
            updateStatusColor();
            updateButtonVisibility();
            Toast.makeText(this, "Đã cập nhật trạng thái", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookingData();
    }
}
