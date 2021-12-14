package com.example.androidapp.data.menudata;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.orderdata.Order;
import com.example.androidapp.data.orderdata.OrderAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

//Adapter for RecyclerView
public class DishAdapter extends ListAdapter<Dish, DishAdapter.DishViewHolder> implements Filterable {
    private List<Dish> mListDish;
    private List<Dish> mListDishFull;
    private OnItemClickListener listener;
    private OnItemClickDelListener delListener;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public DishAdapter(List<Dish> mListDish) {
        super(DIFF_CALLBACK);
        this.mListDish = mListDish;
        //Open 1 card only when delete
        viewBinderHelper.setOpenOnlyOne(true);
    }
    //setup for animation
    private static final DiffUtil.ItemCallback<Dish> DIFF_CALLBACK = new DiffUtil.ItemCallback<Dish>() {
        @Override
        public boolean areItemsTheSame(@NonNull Dish oldItem, @NonNull Dish newItem) {
            return oldItem.getDishID() == newItem.getDishID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Dish oldItem, @NonNull Dish newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getPrice() == newItem.getPrice();

        }
    };

    public void setDish(List<Dish> mListDish) {
        this.mListDish = mListDish;
        this.mListDishFull = new ArrayList<>(mListDish);

        notifyDataSetChanged();
    }

    //Get the dish position
    public Dish getDishAt(int postition) {
        return getItem(postition);
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
        Dish dish = getItem(position);
        if (dish == null) {
            return;
        }
        //Provide id object
        viewBinderHelper.bind(holder.swipeRevealLayout, Integer.toString(dish.getDishID()));

        holder.tvDishName.setText(dish.getName());
        holder.tvDishPrice.setText(String.format("%,d", dish.getPrice()) + " VND");
        //read image from file
        try {
            File f=new File(dish.getImageDir(), dish.getName() + "-" + dish.getPrice());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            holder.imgView.setImageBitmap(b);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
        private ImageView imgView;
        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout layoutDel;
        private RelativeLayout item;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDishName = itemView.findViewById(R.id.dish_name);
            tvDishPrice = itemView.findViewById(R.id.dish_price);
            imgView = itemView.findViewById(R.id.dish_pic_view);
            swipeRevealLayout = itemView.findViewById(R.id.swipe_reveal_layout);
            layoutDel = itemView.findViewById(R.id.menu_item_del);
            //This is the main layout in order_item_recycler
            item = itemView.findViewById(R.id.menu_item);
            //Set onClick method for each item
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
            //Set delete when click layout del
            layoutDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Get pos
                    int pos = getAdapterPosition();
                    //Get del dish
                    Dish dish = getDishAt(pos);
                    if (delListener != null && pos != RecyclerView.NO_POSITION){
                        delListener.onItemClickDel(dish);
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

    public interface OnItemClickDelListener{
        void onItemClickDel(Dish dish);
    }
    public void setOnItemClickDelListener(DishAdapter.OnItemClickDelListener delListener){
        this.delListener = delListener;
    }


}
