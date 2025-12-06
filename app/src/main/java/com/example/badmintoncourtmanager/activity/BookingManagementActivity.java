package com.example.badmintoncourtmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.adapter.BookingAdapter;
import com.example.badmintoncourtmanager.database.DatabaseHelper;
import com.example.badmintoncourtmanager.model.Booking;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class BookingManagementActivity extends AppCompatActivity implements BookingAdapter.OnBookingClickListener {
    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton fabAdd;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_management);

        databaseHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recycler_view);
        fabAdd = findViewById(R.id.fab_add);
        buttonBack = findViewById(R.id.button_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);

        loadBookings();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(BookingManagementActivity.this, AddEditBookingActivity.class);
            startActivity(intent);
        });

        buttonBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookings();
    }

    private void loadBookings() {
        try {
            List<Booking> bookings = databaseHelper.getAllBookings();
            adapter = new BookingAdapter(this, bookings, this);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            android.widget.Toast.makeText(this, "Lỗi tải lịch đặt: " + e.getMessage(), android.widget.Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onBookingClick(Booking booking) {
        Intent intent = new Intent(this, BookingDetailActivity.class);
        intent.putExtra("booking_id", booking.getId());
        startActivity(intent);
    }

    @Override
    public void onPayClick(Booking booking) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Xác nhận thanh toán")
                .setMessage("Bạn chắc chắn muốn thanh toán đơn đặt sân này?")
                .setPositiveButton("Thanh toán", (dialog, which) -> {
                    boolean success = databaseHelper.updateBookingStatus(booking.getId(), "Đã thanh toán");
                    if (success) {
                        android.widget.Toast
                                .makeText(this, "Đã thanh toán thành công!", android.widget.Toast.LENGTH_SHORT).show();
                        loadBookings(); // Refresh list to update UI and hide pay button
                    } else {
                        android.widget.Toast
                                .makeText(this, "Cập nhật trạng thái thất bại!", android.widget.Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onEditClick(Booking booking) {
        Intent intent = new Intent(this, AddEditBookingActivity.class);
        intent.putExtra("booking_id", booking.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Booking booking) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa lịch đặt sân này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean success = databaseHelper.deleteBooking(booking.getId());
                    if (success) {
                        android.widget.Toast.makeText(this, "Đã xóa lịch đặt!", android.widget.Toast.LENGTH_SHORT)
                                .show();
                        loadBookings(); // Refresh list
                    } else {
                        android.widget.Toast.makeText(this, "Xóa thất bại!", android.widget.Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
