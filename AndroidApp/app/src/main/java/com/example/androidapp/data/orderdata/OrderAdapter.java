package com.example.androidapp.data.orderdata;



import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.androidapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
                    oldItem.getShip() == newItem.getShip() &&
                    oldItem.getOrderListDish().equals(newItem.getOrderListDish());
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
        holder.tvOrderPrice.setText(String.format("%,d", order.getPrice()) + " VND");
        //Read image from file
        try {
            File f=new File(order.getClient().getImageDir());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            holder.imageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e) {
            Resources res = holder.imageView.getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.ava_client_default);
            holder.imageView.setImageBitmap(bitmap);
        }
        //Handle flag
        if (order.getPaid()){
            holder.flagPaid.setVisibility(View.VISIBLE);
        } else {
            holder.flagPaid.setVisibility(View.INVISIBLE);
        }

        if (order.getShip()){
            holder.flagShip.setVisibility(View.VISIBLE);
        } else {
            holder.flagShip.setVisibility(View.INVISIBLE);
        }
    }

    public Order getOrderAt(int pos){
        return getItem(pos);
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvOrderName;
        private final TextView tvOrderDate;
        private final TextView tvOrderTime;
        private final TextView tvOrderPrice;
        private final ImageView imageView;
        private final View flagPaid;
        private final View flagShip;
        private final SwipeRevealLayout swipeRevealLayout;
        private final LinearLayout layoutDel;
        private final RelativeLayout item;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            swipeRevealLayout = itemView.findViewById(R.id.swipe_reveal_layout);
            layoutDel = itemView.findViewById(R.id.order_item_del);
            tvOrderName = itemView.findViewById(R.id.order_name);
            tvOrderDate = itemView.findViewById(R.id.order_day);
            tvOrderTime = itemView.findViewById(R.id.order_time);
            tvOrderPrice = itemView.findViewById(R.id.order_price);
            imageView = itemView.findViewById(R.id.order_avatar);
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