package com.example.badmintoncourtmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.List;

public class BookingDetailActivity extends AppCompatActivity {
    private TextView tvBookingId, tvFieldName, tvCustomerName, tvCustomerPhone;
    private TextView tvDate, tvStartTime, tvEndTime, tvHours, tvStatus, tvTotalPrice;
    private RecyclerView rvServices;
    private LinearLayout layoutServices;
    private Button btnEdit, btnDelete, btnChangeStatus, btnBack;

    private DatabaseHelper dbHelper;
    private int bookingId;
    private Booking booking;
    private List<ServiceUsage> serviceUsages;
    private ServiceUsageAdapter serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private void initViews() {
        tvBookingId = findViewById(R.id.tvBookingId);
        tvFieldName = findViewById(R.id.tvFieldName);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        tvDate = findViewById(R.id.tvDate);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        tvHours = findViewById(R.id.tvHours);
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

        // Hiển thị thông tin booking
        tvBookingId.setText("Mã đặt sân: #" + booking.getId());
        tvFieldName.setText(booking.getFieldName());
        tvCustomerName.setText(booking.getCustomerName());
        tvCustomerPhone.setText(booking.getCustomerPhone());
        tvDate.setText(booking.getDate());
        tvStartTime.setText(booking.getStartTime());
        tvEndTime.setText(booking.getEndTime());
        tvHours.setText(String.format("%.1f giờ", booking.getHours()));
        tvStatus.setText(booking.getStatus());
        tvTotalPrice.setText(String.format("%,.0f đ", booking.getTotalPrice()));

        // Đổi màu status
        updateStatusColor();

        // Load services
        serviceUsages = dbHelper.getServiceUsagesByBookingId(bookingId);
        if (serviceUsages.isEmpty()) {
            layoutServices.setVisibility(View.GONE);
        } else {
            layoutServices.setVisibility(View.VISIBLE);
            setupRecyclerView();
        }
    }

    private void setupRecyclerView() {
        serviceAdapter = new ServiceUsageAdapter(this, serviceUsages, null);
        rvServices.setLayoutManager(new LinearLayoutManager(this));
        rvServices.setAdapter(serviceAdapter);
    }

    private void updateStatusColor() {
        int color;
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
            Toast.makeText(this, "Đã cập nhật trạng thái", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data khi quay lại từ màn hình edit
        loadBookingData();
    }
}
