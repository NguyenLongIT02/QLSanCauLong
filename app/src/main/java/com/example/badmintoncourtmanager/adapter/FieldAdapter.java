package com.example.badmintoncourtmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.model.Field;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.FieldViewHolder> {
    private Context context;
    private List<Field> fieldList;
    private OnFieldClickListener listener;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public interface OnFieldClickListener {
        void onEditClick(Field field);

        void onDeleteClick(Field field);

        void onStatusChange(Field field, boolean isActive);
    }

    public FieldAdapter(Context context, List<Field> fieldList, OnFieldClickListener listener) {
        this.context = context;
        this.fieldList = fieldList;
        this.listener = listener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_field, parent, false);
        return new FieldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        Field field = fieldList.get(position);

        holder.textName.setText(field.getName());
        holder.textType.setText("Loại: " + field.getType());
        holder.textPrice.setText("Giá: " + currencyFormat.format(field.getPricePerHour()) + "/giờ");

        holder.switchStatus.setOnCheckedChangeListener(null);
        boolean isActive = "Hoạt động".equals(field.getStatus());
        holder.switchStatus.setChecked(isActive);
        holder.switchStatus.setText(isActive ? "Hoạt động" : "Bảo trì");

        holder.switchStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String newStatus = isChecked ? "Hoạt động" : "Bảo trì";
            holder.switchStatus.setText(newStatus);
            field.setStatus(newStatus);
            listener.onStatusChange(field, isChecked);
        });

        holder.buttonEdit.setOnClickListener(v -> listener.onEditClick(field));
        holder.buttonDelete.setOnClickListener(v -> listener.onDeleteClick(field));
    }

    @Override
    public int getItemCount() {
        return fieldList.size();
    }

    @Override
    public long getItemId(int position) {
        return fieldList.get(position).getId();
    }

    static class FieldViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textType, textPrice;
        SwitchCompat switchStatus;
        ImageButton buttonEdit, buttonDelete;

        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_field_name);
            textType = itemView.findViewById(R.id.text_field_type);
            textPrice = itemView.findViewById(R.id.text_field_price);
            switchStatus = itemView.findViewById(R.id.switch_field_status);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }
}
