package com.example.badmintoncourtmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.model.ServiceUsage;

import java.util.List;

public class ServiceUsageAdapter extends RecyclerView.Adapter<ServiceUsageAdapter.ViewHolder> {
    private Context context;
    private List<ServiceUsage> serviceUsageList;
    private OnServiceUsageListener listener;
    private java.util.Map<Integer, Integer> inventoryMap = new java.util.HashMap<>();

    public void setInventoryMap(java.util.Map<Integer, Integer> map) {
        this.inventoryMap = map;
    }

    public interface OnServiceUsageListener {
        void onDelete(int position);

        void onQuantityChanged(int position, int newQuantity);
    }

    public ServiceUsageAdapter(Context context, List<ServiceUsage> serviceUsageList, OnServiceUsageListener listener) {
        this.context = context;
        this.serviceUsageList = serviceUsageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_usage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceUsage serviceUsage = serviceUsageList.get(position);

        holder.tvServiceName.setText(serviceUsage.getServiceName());
        holder.tvServicePrice.setText(String.format("%,.0f đ", serviceUsage.getPrice()));
        holder.tvQuantity.setText(String.valueOf(serviceUsage.getQuantity()));

        double totalPrice = serviceUsage.getPrice() * serviceUsage.getQuantity();
        holder.tvTotalPrice.setText(String.format("Tổng: %,.0f đ", totalPrice));

        // Nếu listener null (chế độ xem), ẩn nút xóa và disable buttons
        if (listener == null) {
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnDecrease.setEnabled(false);
            holder.btnIncrease.setEnabled(false);
        } else {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDecrease.setEnabled(true);
            holder.btnIncrease.setEnabled(true);

            holder.btnDelete.setOnClickListener(v -> {
                listener.onDelete(holder.getAdapterPosition());
            });

            holder.btnDecrease.setOnClickListener(v -> {
                int currentQty = serviceUsage.getQuantity();
                if (currentQty > 1) {
                    int newQty = currentQty - 1;
                    serviceUsage.setQuantity(newQty);
                    holder.tvQuantity.setText(String.valueOf(newQty));
                    double total = serviceUsage.getPrice() * newQty;
                    holder.tvTotalPrice.setText(String.format("Tổng: %,.0f đ", total));
                    listener.onQuantityChanged(holder.getAdapterPosition(), newQty);
                }
            });

            holder.btnIncrease.setOnClickListener(v -> {
                int currentQty = serviceUsage.getQuantity();
                int maxQty = inventoryMap.getOrDefault(serviceUsage.getServiceId(), Integer.MAX_VALUE);

                if (currentQty < maxQty) {
                    int newQty = currentQty + 1;
                    serviceUsage.setQuantity(newQty);
                    holder.tvQuantity.setText(String.valueOf(newQty));
                    double total = serviceUsage.getPrice() * newQty;
                    holder.tvTotalPrice.setText(String.format("Tổng: %,.0f đ", total));
                    listener.onQuantityChanged(holder.getAdapterPosition(), newQty);
                } else {
                    android.widget.Toast
                            .makeText(context, "Đã đạt giới hạn tồn kho: " + maxQty, android.widget.Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return serviceUsageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceName, tvServicePrice, tvTotalPrice, tvQuantity;
        Button btnDecrease, btnIncrease;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvServicePrice = itemView.findViewById(R.id.tvServicePrice);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
