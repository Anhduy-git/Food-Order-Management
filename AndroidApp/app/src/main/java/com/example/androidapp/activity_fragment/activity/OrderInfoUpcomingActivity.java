package com.example.androidapp.activity_fragment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.androidapp.R;

public class OrderInfoUpcomingActivity extends AppCompatActivity {

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

    private TextView tvOrderName;
    private TextView tvOrderAddress;
    private TextView tvOrderNumber;
    private TextView tvOrderTime;
    private TextView tvOrderDate;
    private Button btnBack;
    private CheckBox checkPaid;
    private boolean paid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info_upcoming);
        //Reset
        paid = false;

        initUi();
        //Get data from intent to display UI
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ORDER_ID)){
            tvOrderName.setText(intent.getStringExtra(EXTRA_ORDER_NAME));
            tvOrderAddress.setText(intent.getStringExtra(EXTRA_ORDER_ADDRESS));
            tvOrderTime.setText(intent.getStringExtra(EXTRA_ORDER_TIME));
            tvOrderNumber.setText(intent.getStringExtra(EXTRA_ORDER_NUMBER));
            tvOrderDate.setText(intent.getStringExtra(EXTRA_ORDER_DATE));
            paid = intent.getBooleanExtra(EXTRA_CHECK_PAID, paid);
        }

        //Check if Paid for checkbox:
        if (paid){
            checkPaid.setChecked(true);
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

        //Button back to OrderTodayFragment
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(EXTRA_CHECK_PAID, paid);
                data.putExtra(EXTRA_ORDER_NAME, strOrderName);
                data.putExtra(EXTRA_ORDER_ADDRESS, strOrderAddress);
                data.putExtra(EXTRA_ORDER_DATE, strOrderDate);
                data.putExtra(EXTRA_ORDER_TIME, strOrderTime);
                data.putExtra(EXTRA_ORDER_NUMBER, strOrderNumber);
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
        btnBack = findViewById(R.id.order_info_2_back_btn);
        checkPaid = findViewById(R.id.order_paid_checkbox);


    }

}