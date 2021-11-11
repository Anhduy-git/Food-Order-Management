package com.example.androidapp.data.upcomingorderdata;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.androidapp.R;
import com.example.androidapp.data.orderdata.Order;
import com.example.androidapp.data.orderdata.OrderAdapter;


import java.util.ArrayList;
import java.util.List;

public class UpcomingOrderAdapter extends RecyclerView.Adapter<UpcomingOrderAdapter.UpcommingOrderViewHolder>{

    private List<UpcomingOrder> mListUpcomingOrder = new ArrayList<>();
    private OnItemClickListener listener;
    private OnItemClickDelListener delListener;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    //Open 1 card only when delete
    public UpcomingOrderAdapter(){
        viewBinderHelper.setOpenOnlyOne(true);
    }

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

        //Provide id object
        viewBinderHelper.bind(holder.swipeRevealLayout, Integer.toString(upcomingOrder.getId()));

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
        RelativeLayout item;
        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout layoutDel;

        public UpcommingOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            swipeRevealLayout = itemView.findViewById(R.id.swipe_reveal_layout);
            layoutDel = itemView.findViewById(R.id.order_item_del);
            tvOrderName = itemView.findViewById(R.id.order_name);
            tvOrderDate = itemView.findViewById(R.id.order_day);
            tvOrderTime = itemView.findViewById(R.id.order_time);
            tvOrderPrice = itemView.findViewById(R.id.order_price);
            flagPaid = itemView.findViewById(R.id.flag_paid);

            //This is the main layout in order_item_recycler
            item = itemView.findViewById(R.id.order_item);
            //Set onClick method for each item
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION){
                        listener.onItemClick(mListUpcomingOrder.get(pos));
                    }
                }
            });
            //Set delete when click layout del
            layoutDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Get pos
                    int pos = getAdapterPosition();
                    //Get del order
                    UpcomingOrder upcomingOrder = getUpcommingOrderAt(pos);
                    if (delListener != null && pos != RecyclerView.NO_POSITION){
                        delListener.onItemClickDel(upcomingOrder);
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

    public interface OnItemClickDelListener{
        void onItemClickDel(UpcomingOrder upcomingOrder);
    }
    public void setOnItemClickDelListener(UpcomingOrderAdapter.OnItemClickDelListener delListener){
        this.delListener = delListener;
    }

}
