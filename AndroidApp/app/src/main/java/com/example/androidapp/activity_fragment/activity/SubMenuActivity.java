package com.example.androidapp.activity_fragment.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androidapp.R;
import com.example.androidapp.data.menudata.Dish;
import com.example.androidapp.data.menudata.DishAdapter;
import com.example.androidapp.data.menudata.DishViewModel;

import java.util.ArrayList;
import java.util.List;

public class SubMenuActivity extends AppCompatActivity {


    private int numberOfDish = 1;
    private List<Dish> mListDish;
    private DishViewModel dishViewModel;
    private EditText edtSearchBar;
    private Button btnBack;
    private TextView tvQuantity;
    private Button btnIncreaseDish;
    private Button btnDecreaseDish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu);

        initUi();
        mListDish = new ArrayList<>();

        //Create Recycler View
        RecyclerView rcvData = findViewById(R.id.menu_recycler);;

        //rcvData.setHasFixedSize(true);
        rcvData.setLayoutManager(new LinearLayoutManager(this));

        //Create DISH ADAPTER
        final DishAdapter dishAdapter = new DishAdapter(mListDish);
        rcvData.setAdapter(dishAdapter);

        //Create DISH VIEW MODEL
        dishViewModel = new ViewModelProvider(this).get(DishViewModel.class);
        dishViewModel.getAllDishes().observe(this, new Observer<List<Dish>>() {
            @Override

            //Method DISPLAY the list on screen
            public void onChanged(List<Dish> dishes) {
                dishAdapter.setDish(dishes);
                dishAdapter.submitList(dishes);

            }
        });


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
        //Highlight an item on click
        dishAdapter.setOnItemClickListener(new DishAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Dish dish) {
                //TODO: Highlight item
                //Reset quantity whenever click to an item
                numberOfDish = 1;
                tvQuantity.setText(String.valueOf(numberOfDish));
            }
        });
        //Button increase dish quantity
        btnIncreaseDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfDish = numberOfDish + 1;
                tvQuantity.setText(String.valueOf(numberOfDish));
            }
        });
        //Button decrease dish quantity, minimum is 1
        btnDecreaseDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOfDish > 1) {
                    numberOfDish = numberOfDish - 1;
                    tvQuantity.setText(String.valueOf(numberOfDish));
                }
            }
        });

        //Button back to NewOrderActivity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    private void initUi () {

        tvQuantity = findViewById(R.id.sub_menu_num_dish);

        btnBack = findViewById(R.id.btn_back);
        edtSearchBar = findViewById(R.id.dish_search_bar_sub_menu);
        btnIncreaseDish = findViewById((R.id.sub_menu_plus));
        btnDecreaseDish = findViewById((R.id.sub_menu_minus));
    }
}