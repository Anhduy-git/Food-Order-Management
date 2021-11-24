package com.example.androidapp.activity_fragment.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.androidapp.R;
import com.example.androidapp.data.menudata.Dish;

import com.example.androidapp.data.menudata.DishOrderAdapter;
import com.example.androidapp.data.menudata.DishOrderInfoAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrderInfoTodayActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_ID =
            "com.example.androidapp.EXTRA_ORDER_ID";

    public static final String EXTRA_ORDER_NAME =
            "com.example.androidapp.EXTRA_ORDER_NAME";
    public static final String EXTRA_ORDER_ADDRESS =
            "com.example.androidapp.EXTRA_ORDER_ADDRESS";
    public static final String EXTRA_ORDER_NUMBER =
            "com.example.androidapp.EXTRA_ORDER_NUMBER";
    public static final String EXTRA_ORDER_DATE =
            "com.example.androidapp.EXTRA_ORDER_DATE";
    public static final String EXTRA_ORDER_TIME =
            "com.example.androidapp.EXTRA_ORDER_TIME";
    public static final String EXTRA_CHECK_PAID =
            "com.example.androidapp.EXTRA_CHECK_PAID";
    public static final String EXTRA_CHECK_SHIP =
            "com.example.androidapp.EXTRA_CHECK_SHIP";
    public static final String EXTRA_ORDER_DISH_LIST =
            "com.example.androidapp.EXTRA_ORDER_DISH_LIST";

    private TextView tvOrderName;
    private TextView tvOrderAddress;
    private TextView tvOrderNumber;
    private TextView tvOrderTime;
    private TextView tvOrderDate;
    private Button btnBack;
    private Button btnShip;
    private CheckBox checkPaid;
    private boolean ship;
    private boolean paid;
    private RecyclerView rcvData;
    private List<Dish> mListDish = new ArrayList<>();
    final DishOrderInfoAdapter dishOrderInfoAdapter = new DishOrderInfoAdapter(mListDish);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info_today);
        //Reset
        ship = false;
        paid = false;
        initUi();
        initRecyclerView();

        //Get data from intent to display UI
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ORDER_ID)){
            tvOrderName.setText(intent.getStringExtra(EXTRA_ORDER_NAME));
            tvOrderAddress.setText(intent.getStringExtra(EXTRA_ORDER_ADDRESS));
            tvOrderTime.setText(intent.getStringExtra(EXTRA_ORDER_TIME));
            tvOrderNumber.setText(intent.getStringExtra(EXTRA_ORDER_NUMBER));
            tvOrderDate.setText(intent.getStringExtra(EXTRA_ORDER_DATE));
            ship = intent.getBooleanExtra(EXTRA_CHECK_SHIP, ship);
            paid = intent.getBooleanExtra(EXTRA_CHECK_PAID, paid);
            mListDish = intent.getParcelableArrayListExtra(EXTRA_ORDER_DISH_LIST);
        }
        //display list dish
        dishOrderInfoAdapter.setDish(mListDish);

        //Check if Paid for checkbox:
        if (paid){
            checkPaid.setChecked(true);
        }
        //Check if ship for disable button
        if (ship){
            btnShip.setVisibility(View.INVISIBLE);
        }

        //Convert to String
        String strOrderName = tvOrderName.getText().toString().trim();
        String strOrderAddress = tvOrderAddress.getText().toString().trim();
        String strOrderNumber = tvOrderNumber.getText().toString().trim();
        String strOrderDate = tvOrderDate.getText().toString().trim();
        String strOrderTime = tvOrderTime.getText().toString().trim();

        //Checkbox to confirm paid
        checkPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paid == false){
                    paid = true;

                } else {
                    paid = false;

                }
            }
        });
        //Ship button to confirm ship and return data
        btnShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ship = true;
                Intent data = new Intent();
                data.putExtra(EXTRA_CHECK_SHIP, ship);
                data.putExtra(EXTRA_CHECK_PAID, paid);
                data.putExtra(EXTRA_ORDER_NAME, strOrderName);
                data.putExtra(EXTRA_ORDER_ADDRESS, strOrderAddress);
                data.putExtra(EXTRA_ORDER_DATE, strOrderDate);
                data.putExtra(EXTRA_ORDER_TIME, strOrderTime);
                data.putExtra(EXTRA_ORDER_NUMBER, strOrderNumber);
                data.putParcelableArrayListExtra(EXTRA_ORDER_DISH_LIST, (ArrayList<? extends Parcelable>) mListDish);
                int id = getIntent().getIntExtra(EXTRA_ORDER_ID, -1);
                if (id != -1) {
                    data.putExtra(EXTRA_ORDER_ID, id);
                }
                setResult(RESULT_OK, data);
                finish();
            }
        });
        //Button back to OrderTodayFragment
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(EXTRA_CHECK_PAID, paid);
                data.putExtra(EXTRA_CHECK_SHIP, ship);
                data.putExtra(EXTRA_ORDER_NAME, strOrderName);
                data.putExtra(EXTRA_ORDER_ADDRESS, strOrderAddress);
                data.putExtra(EXTRA_ORDER_DATE, strOrderDate);
                data.putExtra(EXTRA_ORDER_TIME, strOrderTime);
                data.putExtra(EXTRA_ORDER_NUMBER, strOrderNumber);
                data.putParcelableArrayListExtra(EXTRA_ORDER_DISH_LIST, (ArrayList<? extends Parcelable>) mListDish);
                int id = getIntent().getIntExtra(EXTRA_ORDER_ID, -1);
                if (id != -1) {
                    data.putExtra(EXTRA_ORDER_ID, id);
                }
                setResult(RESULT_OK, data);
                finish();
            }
        });


    }


    private void initUi () {
        tvOrderName = findViewById(R.id.order_name);
        tvOrderAddress = findViewById(R.id.order_address);
        tvOrderDate = findViewById(R.id.order_day);
        tvOrderNumber = findViewById(R.id.order_phone);
        tvOrderTime = findViewById(R.id.order_time);
        btnBack = findViewById(R.id.btn_back);
        checkPaid = findViewById(R.id.order_paid_checkbox);
        btnShip = findViewById(R.id.order_ship_btn);

    }
    private void initRecyclerView() {
        //Dish view holder and recycler view and displaying
        rcvData = findViewById(R.id.order_dish_recycler);

        rcvData.setAdapter(dishOrderInfoAdapter);
        rcvData.setLayoutManager(new LinearLayoutManager(this));
    }

}