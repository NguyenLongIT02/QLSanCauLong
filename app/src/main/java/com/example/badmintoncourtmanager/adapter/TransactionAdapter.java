package com.example.badmintoncourtmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.model.Transaction;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private Context context;
    private List<Transaction> transactionList;

    public TransactionAdapter(Context context, List<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        holder.textType.setText(transaction.getType());
        holder.textCategory.setText(transaction.getCategory());
        holder.textAmount.setText(currencyFormat.format(transaction.getAmount()));
        holder.textDate.setText(transaction.getDate());
        holder.textDescription.setText(transaction.getDescription());

        // Set type color
        if ("Thu".equals(transaction.getType())) {
            holder.textType.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
            holder.textAmount.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        } else {
            holder.textType.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
            holder.textAmount.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public void updateList(List<Transaction> newList) {
        this.transactionList = newList;
        notifyDataSetChanged();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView textType, textCategory, textAmount, textDate, textDescription;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            textType = itemView.findViewById(R.id.text_type);
            textCategory = itemView.findViewById(R.id.text_category);
            textAmount = itemView.findViewById(R.id.text_amount);
            textDate = itemView.findViewById(R.id.text_date);
            textDescription = itemView.findViewById(R.id.text_description);
        }
    }
}
