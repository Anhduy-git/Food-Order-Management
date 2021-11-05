package com.example.androidapp.activity_fragment.activity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.androidapp.R;

public class OrderInfo1Activity extends AppCompatActivity {

    public static final String EXTRA_ORDER_ID =
            "com.example.androidapp.EXTRA_ORDER_ID";
    public static final String EXTRA_ORDER_NAME_READ_ONLY =
            "com.example.androidapp.EXTRA_ORDER_NAME_READ_ONLY";
    public static final String EXTRA_ORDER_ADDRESS_READ_ONLY =
            "com.example.androidapp.EXTRA_ORDER_ADDRESS_READ_ONLY";
    public static final String EXTRA_ORDER_NUMBER_READ_ONLY =
            "com.example.androidapp.EXTRA_ORDER_NUMBER_READ_ONLY";
    public static final String EXTRA_ORDER_DATE_READ_ONLY =
            "com.example.androidapp.EXTRA_ORDER_DATE_READ_ONLY";
    public static final String EXTRA_ORDER_TIME_READ_ONLY =
            "com.example.androidapp.EXTRA_ORDER_TIME_READ_ONLY";

    private TextView tvOrderName;
    private TextView tvOrderAddress;
    private TextView tvOrderNumber;
    private TextView tvOrderTime;
    private TextView tvOrderDate;
    private Button btnBack;
    private CheckBox btnConfirmShip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info1);

        initUi();

        //Confirm add order
//        btnAddOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addOrder();
//            }
//        });
        //Button back to OrderTodayFragment
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Button Image to confirm ship
        btnConfirmShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Handle confirm
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ORDER_ID)){
            tvOrderName.setText(intent.getStringExtra(EXTRA_ORDER_NAME_READ_ONLY));
            tvOrderAddress.setText(intent.getStringExtra(EXTRA_ORDER_ADDRESS_READ_ONLY));
            tvOrderTime.setText(intent.getStringExtra(EXTRA_ORDER_TIME_READ_ONLY));
            tvOrderNumber.setText(intent.getStringExtra(EXTRA_ORDER_NUMBER_READ_ONLY));
            tvOrderDate.setText(intent.getStringExtra(EXTRA_ORDER_DATE_READ_ONLY));
        }


    }


    private void initUi () {
        tvOrderName = findViewById(R.id.order_name);
        tvOrderAddress = findViewById(R.id.order_address);
        tvOrderDate = findViewById(R.id.order_day);
        tvOrderNumber = findViewById(R.id.order_phone);
        tvOrderTime = findViewById(R.id.order_time);
        btnBack = findViewById(R.id.order_info_1_back_btn);
        btnConfirmShip = findViewById(R.id.order_ship_checkbox);


    }



}