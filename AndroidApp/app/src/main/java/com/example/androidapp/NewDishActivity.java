package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidapp.data.AppDatabase;
import com.example.androidapp.data.Dish;
import com.example.androidapp.data.DishAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewDishActivity extends AppCompatActivity {
    public static final String EXTRA_NAME =
            "com.example.androidapp.EXTRA_NAME";
    public static final String EXTRA_PRICE =
            "com.example.androidapp.EXTRA_PRICE";

    private EditText edtDishName;
    private EditText edtDishPrice;
    private Button btnAddDish;
    private Button btnBack;

    private DishAdapter dishAdapter;
    private List<Dish> mListDish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish);

        initUi();

        //dishAdapter = new DishAdapter();
        //mListDish = new ArrayList<>();
        //dishAdapter.setData(mListDish);

        btnAddDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initUi () {
        edtDishName = findViewById(R.id.add_dish_name_text);
        edtDishPrice = findViewById(R.id.add_dish_price_text);
        btnAddDish = findViewById(R.id.add_dish_info_button);
        btnBack = findViewById(R.id.new_dish_back_btn);
    }

    //Add dish to database
    private void addDish() {
        String strDishName = edtDishName.getText().toString().trim();
        String strDishPrice = edtDishPrice.getText().toString().trim();

        //Check if fields are empty, if so then don't add to database
        if (TextUtils.isEmpty(strDishName) || TextUtils.isEmpty(strDishPrice)) {
            Toast.makeText(this, "Please insert name and price", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, strDishName);
        data.putExtra(EXTRA_PRICE, Integer.valueOf(strDishPrice));

        setResult(RESULT_OK, data);
        finish();
    }

}