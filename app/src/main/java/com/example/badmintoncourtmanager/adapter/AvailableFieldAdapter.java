package com.example.badmintoncourtmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.model.Field;

import java.util.List;

public class AvailableFieldAdapter extends RecyclerView.Adapter<AvailableFieldAdapter.ViewHolder> {
    private Context context;
    private List<Field> fieldList;
    private OnFieldClickListener listener;

    public interface OnFieldClickListener {
        void onBookClick(Field field);
    }

    public AvailableFieldAdapter(Context context, List<Field> fieldList, OnFieldClickListener listener) {
        this.context = context;
        this.fieldList = fieldList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_available_field, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Field field = fieldList.get(position);

        holder.tvFieldName.setText(field.getName());
        holder.tvFieldType.setText("Loại: " + field.getType());
        holder.tvFieldPrice.setText(String.format("Giá: %,.0f đ/giờ", field.getPricePerHour()));
        holder.tvFieldStatus.setText(field.getStatus());

        // Đổi màu status
        if ("Đang hoạt động".equals(field.getStatus())) {
            holder.tvFieldStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        } else {
            holder.tvFieldStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        }

        holder.btnBook.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookClick(field);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fieldList.size();
    }

    public void updateList(List<Field> newList) {
        this.fieldList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFieldName, tvFieldType, tvFieldPrice, tvFieldStatus;
        Button btnBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFieldName = itemView.findViewById(R.id.tvFieldName);
            tvFieldType = itemView.findViewById(R.id.tvFieldType);
            tvFieldPrice = itemView.findViewById(R.id.tvFieldPrice);
            tvFieldStatus = itemView.findViewById(R.id.tvFieldStatus);
            btnBook = itemView.findViewById(R.id.btnBook);
        }
    }
}
