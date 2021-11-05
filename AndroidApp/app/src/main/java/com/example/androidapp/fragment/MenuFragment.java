package com.example.androidapp.fragment;

import static android.app.Activity.RESULT_OK;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.NewDishActivity;
import com.example.androidapp.R;

import com.example.androidapp.menudata.Dish;
import com.example.androidapp.menudata.DishAdapter;
import com.example.androidapp.menudata.DishViewModel;



import java.util.List;

public class MenuFragment extends Fragment {
    public static final int ADD_DISH_REQUEST = 1;
    private DishViewModel dishViewModel;
    private Button btnAddDish;

    public MenuFragment() {
        //Empty
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        initUi(view);

        RecyclerView rcvData = (RecyclerView) view.findViewById(R.id.menu_recycler);;
        //rcvData.setHasFixedSize(true);
        rcvData.setLayoutManager(new LinearLayoutManager(view.getContext()));

        final DishAdapter dishAdapter = new DishAdapter();
        rcvData.setAdapter(dishAdapter);

        dishViewModel = new ViewModelProvider(getActivity()).get(DishViewModel.class);
        dishViewModel.getAllDishes().observe(getActivity(), new Observer<List<Dish>>() {
            @Override
            public void onChanged(List<Dish> dishEntities) {
                dishAdapter.setDish(dishEntities);
            }
        });

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_DISH_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(NewDishActivity.EXTRA_MENU_NAME);
            int price = data.getIntExtra(NewDishActivity.EXTRA_MENU_PRICE, 0);

            Dish dish = new Dish(name, price);
            dishViewModel.insertDish(dish);
        }
        //Toast a message if user press back button and don't add anything
        else {
            Toast.makeText(getActivity(), "Dish not added", Toast.LENGTH_SHORT).show();
        }
    }
}
