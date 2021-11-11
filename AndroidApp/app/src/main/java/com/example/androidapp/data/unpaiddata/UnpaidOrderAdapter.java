package com.example.androidapp.data.unpaiddata;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;


import java.util.ArrayList;
import java.util.List;

public class UnpaidOrderAdapter extends RecyclerView.Adapter<UnpaidOrderAdapter.UnpaidOrderViewHolder>{

    private List<UnpaidOrder> mListUnpaidOrder = new ArrayList<>();
    private OnItemClickListener listener;


    public void setUnpaidOrder(List<UnpaidOrder> mListUnpaidOrder) {
        this.mListUnpaidOrder = mListUnpaidOrder;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UnpaidOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.unpaid_order_item_recycler, parent, false);

        return new UnpaidOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnpaidOrderViewHolder holder, int position){
        UnpaidOrder unpaidOrder = mListUnpaidOrder.get(position);
        if (unpaidOrder == null) {
            return;
        }

        holder.tvOrderName.setText(unpaidOrder.getClientName());
        holder.tvOrderDate.setText(unpaidOrder.getDate());
        holder.tvOrderTime.setText(unpaidOrder.getTime());
        holder.tvOrderPrice.setText("1000");

    }

    @Override
    public int getItemCount() {
        if (mListUnpaidOrder != null) {
            return mListUnpaidOrder.size();
        }
        return 0;
    }

    public UnpaidOrder getUnpaidOrderAt(int pos){
        return mListUnpaidOrder.get(pos);
    }

    public class UnpaidOrderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvOrderName;
        private TextView tvOrderDate;
        private TextView tvOrderTime;
        private TextView tvOrderPrice;

        public UnpaidOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderName = itemView.findViewById(R.id.order_name);
            tvOrderDate = itemView.findViewById(R.id.order_day);
            tvOrderTime = itemView.findViewById(R.id.order_time);
            tvOrderPrice = itemView.findViewById(R.id.order_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION){
                        listener.onItemClick(mListUnpaidOrder.get(pos));
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(UnpaidOrder unpaidOrder);

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
