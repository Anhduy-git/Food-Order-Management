package com.example.androidapp.data.orderdata;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.androidapp.R;

import java.util.ArrayList;
import java.util.List;


//Adapter for RecyclerView
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    private List<Order> mListOrder = new ArrayList<>();
    private OnItemClickListener listener;
    private OnItemClickDelListener delListener;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    //Open 1 card only when delete
    public OrderAdapter(){
        viewBinderHelper.setOpenOnlyOne(true);
    }

    public void setOrder(List<Order> mListOrder) {
        this.mListOrder = mListOrder;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_recycler, parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = mListOrder.get(position);
        if (order == null) {
            return;
        }

        //Provide id object
        viewBinderHelper.bind(holder.swipeRevealLayout, Integer.toString(order.getId()));

        holder.tvOrderName.setText(order.getClientName());
        holder.tvOrderDate.setText(order.getDate());
        holder.tvOrderTime.setText(order.getTime());
        holder.tvOrderPrice.setText("1000");


        if (order.getPaid() == true){
            holder.flagPaid.setVisibility(View.VISIBLE);
        } else {
            holder.flagPaid.setVisibility(View.INVISIBLE);
        }

        if (order.getShip() == true){
            holder.flagShip.setVisibility(View.VISIBLE);
        } else {
            holder.flagShip.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    public Order getOrderAt(int pos){
        return mListOrder.get(pos);
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvOrderName;
        private TextView tvOrderDate;
        private TextView tvOrderTime;
        private TextView tvOrderPrice;
        private View flagPaid;
        private View flagShip;
        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout layoutDel;
        private RelativeLayout item;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            swipeRevealLayout = itemView.findViewById(R.id.swipe_reveal_layout);
            layoutDel = itemView.findViewById(R.id.order_item_del);
            tvOrderName = itemView.findViewById(R.id.order_name);
            tvOrderDate = itemView.findViewById(R.id.order_day);
            tvOrderTime = itemView.findViewById(R.id.order_time);
            tvOrderPrice = itemView.findViewById(R.id.order_price);
            flagPaid = itemView.findViewById(R.id.flag_paid);
            flagShip = itemView.findViewById(R.id.flag_ship);
            //This is the main layout in order_item_recycler
            item = itemView.findViewById(R.id.order_item);
            //Set onClick method for each item
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION){
                        listener.onItemClick(mListOrder.get(pos));
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
                    Order order = getOrderAt(pos);
                    if (delListener != null && pos != RecyclerView.NO_POSITION){
                        delListener.onItemClickDel(order);
                    }

                }
            });

        }
    }
    public interface OnItemClickListener{
        void onItemClick(Order order);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickDelListener{
        void onItemClickDel(Order order);
    }
    public void setOnItemClickDelListener(OnItemClickDelListener delListener){
        this.delListener = delListener;
    }
}
