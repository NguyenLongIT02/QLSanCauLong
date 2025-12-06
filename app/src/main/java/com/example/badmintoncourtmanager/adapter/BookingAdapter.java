package com.example.badmintoncourtmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        void onPayClick(Booking booking);
    }

    public BookingAdapter(Context context, List<Booking> bookingList, OnBookingClickListener listener) {
        this.context = context;
        this.bookingList = bookingList;
        this.listener = listener;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
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

        // Safety checks for null values
        String fieldName = booking.getFieldName() != null ? booking.getFieldName() : "Sân không xác định";
        String status = booking.getStatus() != null ? booking.getStatus() : "Chưa xác định";

        holder.textFieldName.setText(fieldName);
        holder.textCustomerName.setText("Khách: " + booking.getCustomerName());
        holder.textDate.setText("Ngày: " + booking.getDate());
        holder.textTime.setText("Giờ: " + booking.getStartTime() + " - " + booking.getEndTime());
        holder.textPrice.setText(currencyFormat.format(booking.getTotalPrice()));
        holder.textStatus.setText(status);

        // Set status color
        if ("Đã thanh toán".equals(status)) {
            holder.textStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
            holder.btnPay.setVisibility(View.GONE);
        } else if ("Đã hủy".equals(status)) {
            holder.textStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
            holder.btnPay.setVisibility(View.GONE);
        } else if ("Đã đặt".equals(status)) {
            holder.textStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark));
            holder.btnPay.setVisibility(View.VISIBLE);
        } else {
            holder.textStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark));
            holder.btnPay.setVisibility(View.VISIBLE);
        }

        holder.cardView.setOnClickListener(v -> listener.onBookingClick(booking));
        holder.btnPay.setOnClickListener(v -> listener.onPayClick(booking));
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
        CardView cardView;
        TextView textFieldName, textCustomerName, textDate, textTime, textPrice, textStatus;
        Button btnPay;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            textFieldName = itemView.findViewById(R.id.text_field_name);
            textCustomerName = itemView.findViewById(R.id.text_customer_name);
            textDate = itemView.findViewById(R.id.text_date);
            textTime = itemView.findViewById(R.id.text_time);
            textPrice = itemView.findViewById(R.id.text_price);
            textStatus = itemView.findViewById(R.id.text_status);
            btnPay = itemView.findViewById(R.id.btn_pay);
        }
    }
}
