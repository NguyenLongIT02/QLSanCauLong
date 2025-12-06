package com.example.badmintoncourtmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

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
            Toast.makeText(this, "Lỗi tải lịch đặt: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBookingClick(Booking booking) {
        Intent intent = new Intent(this, BookingDetailActivity.class);
        intent.putExtra("booking_id", booking.getId());
        startActivity(intent);
    }
}
