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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.androidapp.R;
import com.example.androidapp.data.menudata.Dish;

import java.util.ArrayList;
import java.util.List;


//Adapter for RecyclerView
public class OrderAdapter extends ListAdapter<Order, OrderAdapter.OrderViewHolder>{
    private List<Order> mListOrder = new ArrayList<>();
    private OnItemClickListener listener;
    private OnItemClickDelListener delListener;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public OrderAdapter(){
        super(DIFF_CALLBACK);
        //Open 1 card only when delete
        viewBinderHelper.setOpenOnlyOne(true);
    }
    //setup for animation
    private static final DiffUtil.ItemCallback<Order> DIFF_CALLBACK = new DiffUtil.ItemCallback<Order>() {
        @Override
        public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getClient().getClientName().equals(newItem.getClient().getClientName()) &&
                    oldItem.getClient().getAddress().equals(newItem.getClient().getAddress()) &&
                    oldItem.getDate().equals(newItem.getDate()) &&
                    oldItem.getTime().equals(newItem.getTime()) &&
                    oldItem.getClient().getPhoneNumber().equals(newItem.getClient().getPhoneNumber()) &&
                    oldItem.getPrice() == newItem.getPrice() &&
                    oldItem.getPaid() == newItem.getPaid() &&
                    oldItem.getShip() == newItem.getShip();
        }
    };

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
        Order order = getItem(position);
        if (order == null) {
            return;
        }

        //Provide id object
        viewBinderHelper.bind(holder.swipeRevealLayout, Integer.toString(order.getId()));

        holder.tvOrderName.setText(order.getClient().getClientName());
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


    public Order getOrderAt(int pos){
        return getItem(pos);
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
                        listener.onItemClick(getItem(pos));
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
