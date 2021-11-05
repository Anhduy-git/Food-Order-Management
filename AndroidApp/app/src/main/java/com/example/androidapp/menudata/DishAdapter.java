package com.example.androidapp.menudata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;

import java.util.ArrayList;
import java.util.List;

//Adapter for RecyclerView
public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> implements Filterable {
    private List<Dish> mListDish;
    private List<Dish> mListDishFull;


    private OnItemClickListener listener;

    public DishAdapter(List<Dish> mListDish) {
        this.mListDish = mListDish;
    }

    public void setDish(List<Dish> mListDish) {
        this.mListDish = mListDish;
        this.mListDishFull = new ArrayList<>(mListDish);

        notifyDataSetChanged();
    }

    //Get the dish position
    public Dish getDishAt(int postition) {
        return mListDish.get(postition);
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
        holder.tvDishPrice.setText(String.format("%,d", dish.getPrice()) + " VND");
    }

    @Override
    public int getItemCount() {
        if (mListDish != null) {
            return mListDish.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return dishFilter;
    }

    //Create a filter object for searching
    private Filter dishFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Dish> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mListDishFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Dish item : mListDishFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mListDish.clear();
            mListDish.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class DishViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDishName;
        private TextView tvDishPrice;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDishName = itemView.findViewById(R.id.dish_name);
            tvDishPrice = itemView.findViewById(R.id.dish_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(mListDish.get(position));
                    }
                }
            });
        }
    }

    //Interface to click on a dish item
    public interface OnItemClickListener {
        void onItemClick(Dish dish);
    }

    //Method item click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
