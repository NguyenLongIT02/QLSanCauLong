package com.example.badmintoncourtmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.model.Booking;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private Context context;
    private List<Booking> bookingList;
    private OnBookingClickListener listener;
    private final NumberFormat currencyFormat;

    public interface OnBookingClickListener {
        void onBookingClick(Booking booking);
    }

    public BookingAdapter(Context context, List<Booking> bookingList, OnBookingClickListener listener) {
        this.context = context;
        this.bookingList = bookingList;
        this.listener = listener;
        this.currencyFormat = NumberFormat
                .getCurrencyInstance(new Locale.Builder().setLanguage("vi").setRegion("VN").build());
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return bookingList.get(position).getId();
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        String fieldName = booking.getFieldName() != null ? booking.getFieldName() : "Sân không xác định";
        String status = booking.getStatus() != null ? booking.getStatus() : "Chưa xác định";

        holder.textFieldName.setText(fieldName);
        holder.textCustomerName.setText("Khách: " + booking.getCustomerName());
        holder.textDate.setText("Ngày: " + booking.getDate());
        holder.textTime.setText("Giờ: " + booking.getStartTime() + " - " + booking.getEndTime());
        holder.textPrice.setText(currencyFormat.format(booking.getTotalPrice()));
        holder.textStatus.setText(status);

        // Set status color
        int colorResId;
        if ("Đã thanh toán".equals(status)) {
            colorResId = android.R.color.holo_green_dark;
        } else if ("Đã hủy".equals(status)) {
            colorResId = android.R.color.holo_red_dark;
        } else if ("Đã đặt".equals(status)) {
            colorResId = android.R.color.holo_blue_dark;
        } else {
            colorResId = android.R.color.holo_orange_dark;
        }
        holder.textStatus.setTextColor(ContextCompat.getColor(context, colorResId));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookingClick(booking);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void updateList(List<Booking> newList) {
        this.bookingList = newList;
        notifyDataSetChanged();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView textFieldName, textCustomerName, textDate, textTime, textPrice, textStatus;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            textFieldName = itemView.findViewById(R.id.text_field_name);
            textCustomerName = itemView.findViewById(R.id.text_customer_name);
            textDate = itemView.findViewById(R.id.text_date);
            textTime = itemView.findViewById(R.id.text_time);
            textPrice = itemView.findViewById(R.id.text_price);
            textStatus = itemView.findViewById(R.id.text_status);
        }
    }
}
