package com.example.androidapp.activity_fragment.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidapp.R;
import com.example.androidapp.data.ImageConverter;
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
    public static final String EXTRA_ORDER_IMAGE =
            "com.example.androidapp.EXTRA_ORDER_IMAGE";
    public static final String EXTRA_CHECK_PAID =
            "com.example.androidapp.EXTRA_CHECK_PAID";
    public static final String EXTRA_CHECK_SHIP =
            "com.example.androidapp.EXTRA_CHECK_SHIP";
    public static final String EXTRA_ORDER_DISH_LIST =
            "com.example.androidapp.EXTRA_ORDER_DISH_LIST";
    public static final String EXTRA_ORDER_PRICE =
            "com.example.androidapp.EXTRA_ORDER_PRICE";
    public static final int CHOOSE_DISH_REQUEST= 1;

    private TextView tvOrderName;
    private TextView tvOrderAddress;
    private TextView tvOrderNumber;
    private TextView tvOrderTime;
    private TextView tvOrderDate;
    private TextView tvOrderPrice;
    private ImageView imageView;
    private Button btnBack;
    private Button btnShip;
    private Button btnAddDish;
    private CheckBox checkPaid;

    private boolean ship;
    private boolean paid;

    private byte[] image;

    private RecyclerView rcvData;
    private List<Dish> mListDish = new ArrayList<>();
    final DishOrderAdapter dishOrderAdapter = new DishOrderAdapter(mListDish);


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

            int price = intent.getIntExtra(EXTRA_ORDER_PRICE, 0);
            tvOrderPrice.setText(String.valueOf(price));

            tvOrderAddress.setText(intent.getStringExtra(EXTRA_ORDER_ADDRESS));
            tvOrderTime.setText(intent.getStringExtra(EXTRA_ORDER_TIME));
            tvOrderNumber.setText(intent.getStringExtra(EXTRA_ORDER_NUMBER));
            tvOrderDate.setText(intent.getStringExtra(EXTRA_ORDER_DATE));

            ship = intent.getBooleanExtra(EXTRA_CHECK_SHIP, ship);
            paid = intent.getBooleanExtra(EXTRA_CHECK_PAID, paid);

            image = intent.getByteArrayExtra(EXTRA_ORDER_IMAGE);
            imageView.setImageBitmap(ImageConverter.convertByteArray2Image(image));

            mListDish = intent.getParcelableArrayListExtra(EXTRA_ORDER_DISH_LIST);
        }
        //display list dish
        dishOrderAdapter.setDish(mListDish);

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
        int orderPrice = Integer.valueOf(tvOrderPrice.getText().toString().trim());

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
                data.putExtra(EXTRA_ORDER_PRICE, orderPrice);
                data.putExtra(EXTRA_ORDER_ADDRESS, strOrderAddress);
                data.putExtra(EXTRA_ORDER_DATE, strOrderDate);
                data.putExtra(EXTRA_ORDER_TIME, strOrderTime);
                data.putExtra(EXTRA_ORDER_NUMBER, strOrderNumber);
                data.putExtra(EXTRA_ORDER_IMAGE, image);

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
                data.putExtra(EXTRA_ORDER_IMAGE, image);
                data.putParcelableArrayListExtra(EXTRA_ORDER_DISH_LIST, (ArrayList<? extends Parcelable>) mListDish);
                int id = getIntent().getIntExtra(EXTRA_ORDER_ID, -1);
                if (id != -1) {
                    data.putExtra(EXTRA_ORDER_ID, id);
                }
                setResult(RESULT_OK, data);
                finish();
            }
        });
        //Button to choose a new dish from menu
        btnAddDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderInfoTodayActivity.this, SubMenuActivity.class);
                startActivityForResult(intent, CHOOSE_DISH_REQUEST);
            }
        });
    }

    private void initUi () {
        tvOrderPrice = findViewById(R.id.order_price);
        tvOrderName = findViewById(R.id.order_name);
        tvOrderAddress = findViewById(R.id.order_address);
        tvOrderDate = findViewById(R.id.order_day);
        tvOrderNumber = findViewById(R.id.order_phone);
        tvOrderTime = findViewById(R.id.order_time);
        imageView = findViewById(R.id.order_avatar);
        btnBack = findViewById(R.id.btn_back);
        checkPaid = findViewById(R.id.order_paid_checkbox);
        btnShip = findViewById(R.id.order_ship_btn);
        btnAddDish = findViewById(R.id.new_dish_btn);

    }
    private void initRecyclerView() {
        //Dish view holder and recycler view and displaying
        rcvData = findViewById(R.id.order_dish_recycler);

        rcvData.setAdapter(dishOrderAdapter);
        rcvData.setLayoutManager(new LinearLayoutManager(this));
    }
    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_DISH_REQUEST && resultCode == RESULT_OK) {
            Dish dish = data.getParcelableExtra(SubMenuActivity.EXTRA_DISH);
            int dishQuantity = data.getIntExtra(SubMenuActivity.EXTRA_DISH_QUANTITY, 0);
            //check if dish existed
            int checkExist = 0;
            for (int i = 0; i < mListDish.size(); i++) {
                if (mListDish.get(i).getName().equals(dish.getName())) {
                    checkExist = 1;
                    mListDish.get(i).setQuantity(mListDish.get(i).getQuantity() + dishQuantity);
                }
            }
            if (checkExist == 0) {
                dish.setQuantity(dishQuantity);
                mListDish.add(dish);
            }
            //generate id for all dish
            for (int i = 1; i <= mListDish.size(); i++) {
                mListDish.get(i - 1).setDishID(i);
            }
            //Display the chosen dish to the current order
            dishOrderAdapter.setDish(mListDish);
        }
    }

}