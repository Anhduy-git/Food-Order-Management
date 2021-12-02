package com.example.androidapp.data.menudata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.data.ImageConverter;

import java.util.ArrayList;
import java.util.List;

//Adapter for RecyclerView
public class DishSelectAdapter extends RecyclerView.Adapter<DishSelectAdapter.DishSelectViewHolder> implements Filterable {
    private List<Dish> mListDish;
    private List<Dish> mListDishFull;


    private OnItemClickListener listener;

    public DishSelectAdapter(List<Dish> mListDish) {
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
    public DishSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_dish_order_select_recycler, parent, false);

        return new DishSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishSelectViewHolder holder, int position) {
        Dish dish = mListDish.get(position);
        if (dish == null) {
            return;
        }

        holder.tvDishName.setText(dish.getName());
        holder.tvDishPrice.setText(String.format("%,d", dish.getPrice()) + " VND");
        holder.imageView.setImageBitmap(ImageConverter.convertByteArray2Image(dish.getImage()));
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

    public class DishSelectViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDishName;
        private TextView tvDishPrice;
        private ImageView imageView;
        private RelativeLayout item;

        public DishSelectViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDishName = itemView.findViewById(R.id.dish_name);
            tvDishPrice = itemView.findViewById(R.id.dish_price);
            imageView = itemView.findViewById(R.id.dish_pic_view);
            item = itemView.findViewById(R.id.menu_item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setFocusableInTouchMode(true);
                    v.requestFocus();
                    v.setFocusableInTouchMode(false);
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(mListDish.get(position));
                    }
                }
            });
            item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        // run scale animation and make it bigger
                        Animation anim = AnimationUtils.loadAnimation(item.getContext(), R.anim.scale_in);
                        item.startAnimation(anim);
                        anim.setFillAfter(true);
                    } else {
                        // run scale animation and make it smaller
                        Animation anim = AnimationUtils.loadAnimation(item.getContext(), R.anim.scale_out);
                        item.startAnimation(anim);
                        anim.setFillAfter(true);
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
