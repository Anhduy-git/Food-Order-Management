package com.example.androidapp.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;

import java.util.ArrayList;
import java.util.List;

//Adapter for RecyclerView
public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder>{

    private List<Dish> mListDish = new ArrayList<>();

    public void setDish(List<Dish> mListDish) {
        this.mListDish = mListDish;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_dish_recycler, parent, false);

        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Dish dish = mListDish.get(position);
        if (dish == null) {
            return;
        }

        holder.tvDishName.setText(dish.getName());
        holder.tvDishPrice.setText(String.valueOf(dish.getPrice()));
    }

    @Override
    public int getItemCount() {
        if (mListDish != null) {
            return mListDish.size();
        }
        return 0;
    }

    public class DishViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDishName;
        private TextView tvDishPrice;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDishName = itemView.findViewById(R.id.dish_name);
            tvDishPrice = itemView.findViewById(R.id.dish_price);
        }
    }

}
