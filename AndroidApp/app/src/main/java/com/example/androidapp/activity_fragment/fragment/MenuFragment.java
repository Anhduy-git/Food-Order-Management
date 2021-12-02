package com.example.androidapp.activity_fragment.fragment;

import static android.app.Activity.RESULT_OK;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.activity_fragment.activity.UpdateDishActivity;
import com.example.androidapp.activity_fragment.activity.NewDishActivity;
import com.example.androidapp.R;

import com.example.androidapp.data.AppDatabase;
import com.example.androidapp.data.ImageConverter;
import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.menudata.Dish;
import com.example.androidapp.data.menudata.DishAdapter;
import com.example.androidapp.data.menudata.DishViewModel;
import com.example.androidapp.data.orderdata.Order;
import com.example.androidapp.data.orderdata.OrderAdapter;


import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {
    public static final int ADD_DISH_REQUEST = 1;
    public static final int EDIT_DISH_REQUEST = 2;

    private List<Dish> mListDish;
    private DishViewModel dishViewModel;
    private Button btnAddDish;
    private EditText edtSearchBar;

    public MenuFragment() {
        //Empty on purpose
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        mListDish = new ArrayList<>();
        initUi(view);

        //Create Recycler View
        RecyclerView rcvData = view.findViewById(R.id.menu_recycler);;
        //rcvData.setHasFixedSize(true);
        rcvData.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //Create DISH ADAPTER
        final DishAdapter dishAdapter = new DishAdapter(mListDish);
        rcvData.setAdapter(dishAdapter);

        //Create DISH VIEW MODEL
        dishViewModel = new ViewModelProvider(getActivity()).get(DishViewModel.class);
        dishViewModel.getAllDishes().observe(getActivity(), new Observer<List<Dish>>() {
            @Override

            //Method DISPLAY the list on screen
            public void onChanged(List<Dish> dishes) {
                //use for filter
                dishAdapter.setDish(dishes);
                //use for animation
                dishAdapter.submitList(dishes);

            }
        });

        //Method DELETE an item by swiping it
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
//                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                //Not implemented on purpose
//                return false;
//            }
//            //This is the swiping to delete method
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                dishViewModel.deleteDish(dishAdapter.getDishAt(viewHolder.getAdapterPosition()));
//                Toast.makeText(getActivity(), "Dish deleted", Toast.LENGTH_SHORT).show();
//            }
//        }).attachToRecyclerView(rcvData);

        //Create search bar listener for SEARCH METHOD
        edtSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Empty on purpose
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Empty on purpose
            }

            @Override
            public void afterTextChanged(Editable s) {
                dishAdapter.getFilter().filter(s.toString());
            }
        });

        //Method CLICK TO VIEW info of an item in Recycler View
        dishAdapter.setOnItemClickListener(new DishAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Dish dish) {
                Intent intent = new Intent(getActivity(), UpdateDishActivity.class);
                intent.putExtra(UpdateDishActivity.EXTRA_ID, dish.getDishID());
                intent.putExtra(UpdateDishActivity.EXTRA_NAME, dish.getName());
                intent.putExtra(UpdateDishActivity.EXTRA_PRICE, dish.getPrice());
                intent.putExtra(UpdateDishActivity.EXTRA_IMAGE, dish.getImage());

                startActivityForResult(intent, EDIT_DISH_REQUEST);
            }
        });
        //Delete item
        dishAdapter.setOnItemClickDelListener(new DishAdapter.OnItemClickDelListener() {
            @Override
            public void onItemClickDel(Dish dish) {
                dishViewModel.deleteDish(dish);
            }
        });

        //Method CLICK the add button
        btnAddDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewDishActivity.class);
                startActivityForResult(intent, ADD_DISH_REQUEST);
            }
        });

        return view;
    }

    //Method to init UI components
    private void initUi (View view) {
        btnAddDish = view.findViewById(R.id.add_dish_button);
        edtSearchBar = view.findViewById(R.id.dish_search_bar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //ADD_DISH_REQUEST (Add a dish to database)
        if (requestCode == ADD_DISH_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(NewDishActivity.EXTRA_MENU_NAME);
            int price = data.getIntExtra(NewDishActivity.EXTRA_MENU_PRICE, 0);
            byte[] image = data.getByteArrayExtra(NewDishActivity.EXTRA_MENU_IMAGE);
            //Log.d("IMAGE", String.valueOf(image));

            Dish dish = new Dish(name, price, image);
            //check if dish exist
            if (!checkDishExist(dish)) {
                dishViewModel.insertDish(dish);
                Toast.makeText(getActivity(), "Dish added successfully", Toast.LENGTH_SHORT).show();
            }

        }
        //EDIT DISH REQUEST (Update an existing dish)
        else if (requestCode == EDIT_DISH_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(UpdateDishActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(getActivity(), "Dish can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(UpdateDishActivity.EXTRA_NAME);
            int price = data.getIntExtra(UpdateDishActivity.EXTRA_PRICE, 0);
            byte[] image = data.getByteArrayExtra(UpdateDishActivity.EXTRA_IMAGE);

            Dish dish = new Dish(name, price, image);
            //check if dish exist
            if (!checkDishExist(dish)) {
                dish.setDishID(id);
                dishViewModel.updateDish(dish);
                Toast.makeText(getActivity(), "Dish updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            //Do nothing
        }
    }
    private boolean checkDishExist(@NonNull Dish dish) {
        List<Dish> list  = AppDatabase.getInstance(getContext()).dishDao().checkDishExist(dish.getName(), dish.getPrice(), dish.getImage());
        return list != null && !list.isEmpty();
    }
}
