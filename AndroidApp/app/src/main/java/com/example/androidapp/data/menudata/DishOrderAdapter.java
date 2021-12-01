package com.example.androidapp.data.menudata;

import android.util.Log;
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

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.androidapp.R;
import com.example.androidapp.data.ImageConverter;

import java.util.ArrayList;
import java.util.List;

//Adapter for RecyclerView
public class DishOrderAdapter extends RecyclerView.Adapter<DishOrderAdapter.DishViewHolder>{
    private List<Dish> mListDish;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public DishOrderAdapter(List<Dish> mListDish) {
        this.mListDish = mListDish;
        //Open 1 card only when delete
        viewBinderHelper.setOpenOnlyOne(true);
    }

    public void setDish(List<Dish> mListDish) {
        this.mListDish = mListDish;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (mListDish != null) {
            return mListDish.size();
        }
        return 0;
    }
    //Get the dish position
    public Dish getDishAt(int postition) {
        return mListDish.get(postition);
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_dish_order_recycler, parent, false);

        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Dish dish = mListDish.get(position);
        if (dish == null) {
            return;
        }
        //Provide id object
        viewBinderHelper.bind(holder.swipeRevealLayout, Integer.toString(dish.getDishID()));

        holder.tvDishName.setText(dish.getName());
        holder.tvDishPrice.setText(String.format("%,d", dish.getPrice()) + " VND");
        holder.tvDishQuantity.setText(String.valueOf(dish.getQuantity()));
        //TODO: ERROR here
        //holder.imageView.setImageBitmap(ImageConverter.convertByteArray2Image(dish.getImage()));

        holder.layoutDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListDish.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }




    public class DishViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDishName;
        private TextView tvDishPrice;
        private ImageView imageView;
        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout layoutDel;
        private TextView tvDishQuantity;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDishName = itemView.findViewById(R.id.dish_name);
            tvDishPrice = itemView.findViewById(R.id.dish_price);
            imageView = itemView.findViewById(R.id.dish_pic_view);
            swipeRevealLayout = itemView.findViewById(R.id.swipe_reveal_layout);
            layoutDel = itemView.findViewById(R.id.menu_item_del);
            tvDishQuantity = itemView.findViewById(R.id.order_info_num_dish);
        }
    }

}
