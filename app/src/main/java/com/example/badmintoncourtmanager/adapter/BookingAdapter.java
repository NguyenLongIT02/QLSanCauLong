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

        void onEditClick(Booking booking);

        void onDeleteClick(Booking booking);

        void onPayClick(Booking booking);
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

        // Safety checks for null values
        String fieldName = booking.getFieldName() != null ? booking.getFieldName() : "Sân không xác định";
        String status = booking.getStatus() != null ? booking.getStatus() : "Chưa xác định";

        holder.textFieldName.setText(fieldName);
        holder.textCustomerName.setText("Khách: " + booking.getCustomerName());
        holder.textDate.setText("Ngày: " + booking.getDate());
        holder.textTime.setText("Giờ: " + booking.getStartTime() + " - " + booking.getEndTime());
        holder.textPrice.setText(currencyFormat.format(booking.getTotalPrice()));
        holder.textStatus.setText(status);

        // Set status color and button visibility based on payment status
        if ("Đã thanh toán".equals(status)) {
            holder.textStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
            // Đã thanh toán: chỉ hiện nút Xóa
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnPay.setVisibility(View.GONE);
        } else if ("Đã hủy".equals(status)) {
            holder.textStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnPay.setVisibility(View.GONE);
        } else if ("Đã đặt".equals(status)) {
            holder.textStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark));
            // Chưa thanh toán: hiện đủ 3 nút
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnPay.setVisibility(View.VISIBLE);
        } else {
            holder.textStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark));
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnPay.setVisibility(View.VISIBLE);
        }

        // Set CardView clickable
        holder.cardView.setClickable(true);
        holder.cardView.setFocusable(true);

        holder.cardView.setOnClickListener(v -> {
            try {
                if (listener != null) {
                    listener.onBookingClick(booking);
                }
            } catch (Exception e) {
                e.printStackTrace();
                android.widget.Toast.makeText(context, "Lỗi: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT)
                        .show();
            }
        });

        // Edit button click handler
        holder.btnEdit.setOnClickListener(v -> {
            try {
                if (listener != null) {
                    listener.onEditClick(booking);
                }
            } catch (Exception e) {
                e.printStackTrace();
                android.widget.Toast.makeText(context, "Lỗi sửa: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT)
                        .show();
            }
        });

        // Delete button click handler
        holder.btnDelete.setOnClickListener(v -> {
            try {
                if (listener != null) {
                    listener.onDeleteClick(booking);
                }
            } catch (Exception e) {
                e.printStackTrace();
                android.widget.Toast.makeText(context, "Lỗi xóa: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT)
                        .show();
            }
        });

        // Pay button click handler
        holder.btnPay.setClickable(true);
        holder.btnPay.setFocusable(true);

        holder.btnPay.setOnClickListener(v -> {
            try {
                // Prevent parent from receiving click event
                v.setPressed(false);
                if (listener != null) {
                    listener.onPayClick(booking);
                }
            } catch (Exception e) {
                e.printStackTrace();
                android.widget.Toast
                        .makeText(context, "Lỗi thanh toán: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT)
                        .show();
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
        CardView cardView;
        TextView textFieldName, textCustomerName, textDate, textTime, textPrice, textStatus;
        Button btnEdit, btnDelete, btnPay;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            textFieldName = itemView.findViewById(R.id.text_field_name);
            textCustomerName = itemView.findViewById(R.id.text_customer_name);
            textDate = itemView.findViewById(R.id.text_date);
            textTime = itemView.findViewById(R.id.text_time);
            textPrice = itemView.findViewById(R.id.text_price);
            textStatus = itemView.findViewById(R.id.text_status);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnPay = itemView.findViewById(R.id.btn_pay);
        }
    }
}
