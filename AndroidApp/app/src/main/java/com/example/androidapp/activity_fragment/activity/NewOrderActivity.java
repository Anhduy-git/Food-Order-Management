package com.example.androidapp.activity_fragment.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.androidapp.R;
import com.example.androidapp.data.menudata.Dish;
import com.example.androidapp.data.menudata.DishOrderAdapter;

import org.joda.time.DateTimeComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
    public static final String EXTRA_CHECK_PAID =
            "com.example.androidapp.EXTRA_CHECK_PAID";
    public static final String EXTRA_CHECK_SHIP =
            "com.example.androidapp.EXTRA_CHECK_SHIP";
    public static final String EXTRA_ORDER_DISH_LIST =
            "com.example.androidapp.EXTRA_ORDER_DISH_LIST";

    public static final int CHOOSE_CLIENT_REQUEST = 1;
    public static final int CHOOSE_DISH_REQUEST= 2;

    private EditText editOrderName;
    private TextView editOrderTime;
    private TextView editOrderDate;
    private ImageView addOrderDate;
    private ImageView addOrderTime;
    private EditText editOrderAddress;
    private EditText editOrderNumber;
    private Button btnAddOrder;
    private Button btnBack;
    private Button btnAddDish;
    private Button btnAddClient;
    private RecyclerView rcvData;
    private List<Dish> mListDish = new ArrayList<>();
    final DishOrderAdapter dishOrderAdapter = new DishOrderAdapter(mListDish);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        initUi();
        initRecyclerView();

        setupDateTimePicker();

        //Confirm add order
        btnAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (! mListDish.isEmpty()) {
                    addOrder();
                }
                else {
                    Toast.makeText(NewOrderActivity.this, "Please add a dish", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Button back to OrderTodayFragment
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Button to choose a new dish from menu
        btnAddDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewOrderActivity.this, SubMenuActivity.class);
                startActivityForResult(intent, CHOOSE_DISH_REQUEST);
            }
        });

        //Button to choose client from contact
        btnAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewOrderActivity.this, SubContactActivity.class);
                startActivityForResult(intent, CHOOSE_CLIENT_REQUEST);
            }
        });
    }

    private void initUi () {
        editOrderName = findViewById(R.id.add_order_name);
        editOrderAddress = findViewById(R.id.add_order_address);
        editOrderNumber = findViewById(R.id.add_order_number);
        btnBack = findViewById(R.id.btn_back);
        btnAddOrder = findViewById(R.id.add_new_order);
        btnAddDish = findViewById(R.id.new_dish_btn);
        btnAddClient = findViewById(R.id.new_client_btn);
        editOrderDate = findViewById(R.id.order_date_tv);
        editOrderTime = findViewById(R.id.order_time_tv);
        addOrderDate = findViewById(R.id.add_order_date);
        addOrderTime = findViewById(R.id.add_order_time);
    }

    private void initRecyclerView() {
        //Dish view holder and recycler view and displaying
        rcvData = findViewById(R.id.order_dish_recycler);

        rcvData.setAdapter(dishOrderAdapter);
        rcvData.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupDateTimePicker() {
        addOrderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(editOrderDate);
            }
        });
        addOrderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(editOrderTime);
            }
        });
    }

    private void showDateDialog(final TextView date_in) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                date_in.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new DatePickerDialog(NewOrderActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog(final TextView time_in) {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                time_in.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new TimePickerDialog(NewOrderActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

    }

    //Add order to database
    private void addOrder() {
        String strOrderName = editOrderName.getText().toString().trim();
        String strOrderAddress = editOrderAddress.getText().toString().trim();
        String strOrderNumber = editOrderNumber.getText().toString().trim();
        String strOrderDate = editOrderDate.getText().toString().trim();
        String strOrderTime = editOrderTime.getText().toString().trim();

        //Only compare the date
        DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //Check if fields are empty, if so then don't add to database
        if (TextUtils.isEmpty(strOrderName) || TextUtils.isEmpty(strOrderAddress)
                || TextUtils.isEmpty(strOrderNumber) || TextUtils.isEmpty(strOrderDate)
                || TextUtils.isEmpty(strOrderTime)) {
            Toast.makeText(this, "Blank", Toast.LENGTH_SHORT).show();
            return;
        }
        //Get the current date
        Date today = calendar.getTime();
        //Get order date
        try {
            Date orderDate = simpleDateFormat.parse(strOrderDate);
            //Check if the day is not in the pass
            int ret = dateTimeComparator.compare(orderDate, today);
            if (ret < 0){
                Toast.makeText(this, "Can't add order in the past here", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent data = new Intent();
            data.putExtra(EXTRA_ORDER_NAME, strOrderName);
            data.putExtra(EXTRA_ORDER_ADDRESS, strOrderAddress);
            data.putExtra(EXTRA_ORDER_DATE, strOrderDate);
            data.putExtra(EXTRA_ORDER_TIME, strOrderTime);
            data.putExtra(EXTRA_ORDER_NUMBER, strOrderNumber);
            data.putParcelableArrayListExtra(EXTRA_ORDER_DISH_LIST, (ArrayList<? extends Parcelable>) mListDish);

            setResult(RESULT_OK, data);
            finish();
        } catch (ParseException ex) {
            Toast.makeText(NewOrderActivity.this, "Parse Exception", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("WrongConstant")
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

            Toast.makeText(NewOrderActivity.this, "Client added successfully", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == CHOOSE_DISH_REQUEST && resultCode == RESULT_OK) {
            Dish dish = data.getParcelableExtra(SubMenuActivity.EXTRA_DISH);
            int dishQuantity = data.getIntExtra(SubMenuActivity.EXTRA_DISH_QUANTITY, 0);
            dish.setQuantity(dishQuantity);
            mListDish.add(dish);

            //Display the chosen dish to the current order
            dishOrderAdapter.setDish(mListDish);
            dishOrderAdapter.submitList(mListDish);
        }
    }
}