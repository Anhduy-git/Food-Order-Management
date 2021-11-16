package com.example.androidapp.activity_fragment.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidapp.R;

import java.util.Calendar;
import java.util.TimeZone;

public class NewTodayOrderActivity extends AppCompatActivity {

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
    public static final int CHOOSE_CLIENT_REQUEST = 1;

    private EditText editOrderName;
    private EditText editOrderTime;
    private EditText editOrderDate;
    private EditText editOrderAddress;
    private EditText editOrderNumber;
    private Button btnAddOrder;
    private Button btnBack;
    private Button btnAddDish;
    private Button btnAddClient;
    private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
    private int today = (calendar.get(Calendar.DAY_OF_MONTH));

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
        //Button to add new dish
        btnAddDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewTodayOrderActivity.this, SubMenuActivity.class);
                startActivity(intent);
            }
        });
        //Button to choose client from contact
        btnAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewTodayOrderActivity.this, SubContactActivity.class);
                startActivityForResult(intent, CHOOSE_CLIENT_REQUEST);
            }
        });


    }


    private void initUi () {
        editOrderName = findViewById(R.id.add_order_name);
        editOrderAddress = findViewById(R.id.add_order_address);
        editOrderDate = findViewById(R.id.add_order_date);
        editOrderNumber = findViewById(R.id.add_order_number);
        editOrderTime = findViewById(R.id.add_order_time);
        btnBack = findViewById(R.id.btn_back);
        btnAddOrder = findViewById(R.id.add_new_order);
        btnAddDish = findViewById(R.id.new_dish_btn);
        btnAddClient = findViewById(R.id.new_client_btn);

    }

    //Add order to database
    private void addOrder() {
        String strOrderName = editOrderName.getText().toString().trim();
        String strOrderAddress = editOrderAddress.getText().toString().trim();
        String strOrderNumber = editOrderNumber.getText().toString().trim();
        String strOrderDate = editOrderDate.getText().toString().trim();
        String strOrderTime = editOrderTime.getText().toString().trim();
        int intOrderDate = Integer.parseInt(strOrderDate);
        //Check if fields are empty, if so then don't add to database
        if (TextUtils.isEmpty(strOrderName) || TextUtils.isEmpty(strOrderAddress)
                || TextUtils.isEmpty(strOrderNumber) || TextUtils.isEmpty(strOrderDate)
                || TextUtils.isEmpty(strOrderTime)) {
            Toast.makeText(this, "Blank", Toast.LENGTH_SHORT).show();
            return;
        }
        //Check if the day is not in the pass
//        if (intOrderDate < today){
//            Toast.makeText(this, "Can't add order in the pass here", Toast.LENGTH_SHORT).show();
//            return;
//        }

        Intent data = new Intent();
        data.putExtra(EXTRA_ORDER_NAME, strOrderName);
        data.putExtra(EXTRA_ORDER_ADDRESS, strOrderAddress);
        data.putExtra(EXTRA_ORDER_DATE, strOrderDate);
        data.putExtra(EXTRA_ORDER_TIME, strOrderTime);
        data.putExtra(EXTRA_ORDER_NUMBER, strOrderNumber);
        data.putExtra(EXTRA_CHECK_PAID, false);
        data.putExtra(EXTRA_CHECK_SHIP, false);

        setResult(RESULT_OK, data);
        finish();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_CLIENT_REQUEST && resultCode == RESULT_OK) {
            String clientName = data.getStringExtra(SubContactActivity.EXTRA_NAME);
            String clientPhoneNumber = data.getStringExtra(SubContactActivity.EXTRA_PHONE_NUMBER);
            String clientAddress = data.getStringExtra(SubContactActivity.EXTRA_ADDRESS);

            //Display client's info after having chosen from existing contact
            editOrderName.setText(clientName);
            editOrderNumber.setText(clientPhoneNumber);
            editOrderAddress.setText(clientAddress);

            Toast.makeText(NewTodayOrderActivity.this, "Client added successfully", Toast.LENGTH_SHORT).show();
        }
    }

}