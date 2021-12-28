package com.example.androidapp.data.unpaiddata;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class UnpaidOrderAdapter extends ListAdapter<UnpaidOrder, UnpaidOrderAdapter.UnpaidOrderViewHolder>{

    private List<UnpaidOrder> mListUnpaidOrder = new ArrayList<>();
    private OnItemClickListener listener;

    public UnpaidOrderAdapter(){
        super(DIFF_CALLBACK);
    }
    //setup for animation
    private static final DiffUtil.ItemCallback<UnpaidOrder> DIFF_CALLBACK = new DiffUtil.ItemCallback<UnpaidOrder>() {
        @Override
        public boolean areItemsTheSame(@NonNull UnpaidOrder oldItem, @NonNull UnpaidOrder newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull UnpaidOrder oldItem, @NonNull UnpaidOrder newItem) {
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
        UnpaidOrder unpaidOrder = getItem(position);
        if (unpaidOrder == null) {
            return;
        }

        holder.tvOrderName.setText(unpaidOrder.getClient().getClientName());
        holder.tvOrderDate.setText(unpaidOrder.getDate());
        holder.tvOrderTime.setText(unpaidOrder.getTime());
        holder.tvOrderPrice.setText(String.format("%,d", unpaidOrder.getPrice()) + " VND");
        //Read image from file
        try {
            File f=new File(unpaidOrder.getClient().getImageDir());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            holder.imageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e) {
            Resources res = holder.imageView.getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.ava_client_default);
            holder.imageView.setImageBitmap(bitmap);
        }

    }

    public class UnpaidOrderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvOrderName;
        private final TextView tvOrderDate;
        private final TextView tvOrderTime;
        private final TextView tvOrderPrice;
        private final RelativeLayout item;
        private final ImageView imageView;

        public UnpaidOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderName = itemView.findViewById(R.id.order_name);
            tvOrderDate = itemView.findViewById(R.id.order_day);
            tvOrderTime = itemView.findViewById(R.id.order_time);
            tvOrderPrice = itemView.findViewById(R.id.order_price);
            item = itemView.findViewById(R.id.order_item);
            imageView = itemView.findViewById(R.id.order_avatar);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION){
                        listener.onItemClick(getItem(pos));
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
