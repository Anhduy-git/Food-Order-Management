package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewOrderActivity extends AppCompatActivity {

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

    private EditText editOrderName;
    private EditText editOrderTime;
    private EditText editOrderDate;
    private EditText editOrderAddress;
    private EditText editOrderNumber;
    private Button btnAddOrder;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        initUi();

        //Confirm add order
        btnAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrder();
            }
        });
        //Button back to OrderTodayFragment
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    private void initUi () {
        editOrderName = findViewById(R.id.add_order_name);
        editOrderAddress = findViewById(R.id.add_order_address);
        editOrderDate = findViewById(R.id.add_order_date);
        editOrderNumber = findViewById(R.id.add_order_number);
        editOrderTime = findViewById(R.id.add_order_time);
        btnBack = findViewById(R.id.new_order_back_btn);
        btnAddOrder = findViewById(R.id.confirm_add_new_order);
    }

    //Add order to database
    private void addOrder() {
        String strOrderName = editOrderName.getText().toString().trim();
        String strOrderAddress = editOrderAddress.getText().toString().trim();
        String strOrderNumber = editOrderNumber.getText().toString().trim();
        String strOrderDate = editOrderDate.getText().toString().trim();
        String strOrderTime = editOrderTime.getText().toString().trim();

        //Check if fields are empty, if so then don't add to database
        if (TextUtils.isEmpty(strOrderName) || TextUtils.isEmpty(strOrderAddress)
                || TextUtils.isEmpty(strOrderNumber) || TextUtils.isEmpty(strOrderDate)
                || TextUtils.isEmpty(strOrderTime)) {
            Toast.makeText(this, "Blank", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_ORDER_NAME, strOrderName);
        data.putExtra(EXTRA_ORDER_ADDRESS, strOrderAddress);
        data.putExtra(EXTRA_ORDER_DATE, strOrderDate);
        data.putExtra(EXTRA_ORDER_TIME, strOrderTime);
        data.putExtra(EXTRA_ORDER_NUMBER, strOrderNumber);
        data.putExtra(EXTRA_ORDER_NAME, strOrderName);

        setResult(RESULT_OK, data);
        finish();
    }

}