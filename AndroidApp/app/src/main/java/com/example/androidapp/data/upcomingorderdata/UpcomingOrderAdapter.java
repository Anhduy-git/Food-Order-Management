package com.example.androidapp.data.upcomingorderdata;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;


import java.util.ArrayList;
import java.util.List;

public class UpcomingOrderAdapter extends RecyclerView.Adapter<UpcomingOrderAdapter.UpcommingOrderViewHolder>{

    private List<UpcomingOrder> mListUpcomingOrder = new ArrayList<>();
    private OnItemClickListener listener;


    public void setUpcommingOrder(List<UpcomingOrder> mListUpcomingOrder) {
        this.mListUpcomingOrder = mListUpcomingOrder;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UpcommingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_recycler, parent, false);

        return new UpcommingOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcommingOrderViewHolder holder, int position){
        UpcomingOrder upcomingOrder = mListUpcomingOrder.get(position);
        if (upcomingOrder == null) {
            return;
        }

        holder.tvOrderName.setText(upcomingOrder.getClientName());
        holder.tvOrderDate.setText(upcomingOrder.getDate());
        holder.tvOrderTime.setText(upcomingOrder.getTime());
        holder.tvOrderPrice.setText("1000");

        if (upcomingOrder.getPaid() == true){
            holder.flagPaid.setVisibility(View.VISIBLE);
        } else {
            holder.flagPaid.setVisibility(View.INVISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        if (mListUpcomingOrder != null) {
            return mListUpcomingOrder.size();
        }
        return 0;
    }

    public UpcomingOrder getUpcommingOrderAt(int pos){
        return mListUpcomingOrder.get(pos);
    }

    public class UpcommingOrderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvOrderName;
        private TextView tvOrderDate;
        private TextView tvOrderTime;
        private TextView tvOrderPrice;
        private View flagPaid;

        public UpcommingOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderName = itemView.findViewById(R.id.order_name);
            tvOrderDate = itemView.findViewById(R.id.order_day);
            tvOrderTime = itemView.findViewById(R.id.order_time);
            tvOrderPrice = itemView.findViewById(R.id.order_price);
            flagPaid = itemView.findViewById(R.id.flag_paid);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION){
                        listener.onItemClick(mListUpcomingOrder.get(pos));
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(UpcomingOrder upcomingOrder);

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
