package com.example.bankingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private Context context;
    private List<Customer> customerList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Customer customer);
    }

    public CustomerAdapter(Context context, List<Customer> customerList, OnItemClickListener listener) {
        this.context = context;
        this.customerList = customerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_item, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.tvName.setText(customer.getName());
        holder.tvType.setText(customer.getAccountType());
        holder.tvBranch.setText(customer.getBranch());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(customer));
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvBranch;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCustomerName);
            tvType = itemView.findViewById(R.id.tvCustomerType);
            tvBranch = itemView.findViewById(R.id.tvCustomerBranch);
        }
    }
}
