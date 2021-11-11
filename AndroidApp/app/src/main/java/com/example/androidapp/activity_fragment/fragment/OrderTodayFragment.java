package com.example.androidapp.activity_fragment.fragment;


import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.activity_fragment.activity.NewTodayOrderActivity;
import com.example.androidapp.activity_fragment.activity.OrderInfoTodayActivity;
import com.example.androidapp.R;
import com.example.androidapp.data.orderdata.Order;
import com.example.androidapp.data.orderdata.OrderAdapter;
import com.example.androidapp.data.orderdata.OrderViewModel;
import com.example.androidapp.data.upcomingorderdata.UpcomingOrder;
import com.example.androidapp.data.upcomingorderdata.UpcomingOrderViewModel;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class OrderTodayFragment extends Fragment {
    public Button btnAddNewOrder;
    public static final int ADD_ORDER_REQUEST = 1;
    public static final int CONFIRM_ORDER_REQUEST = 2;
    //View model
    private OrderViewModel orderViewModel;
    private UpcomingOrderViewModel upcomingOrderViewModel;

    private boolean paid;
    private boolean ship;
    private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
    private int today = (calendar.get(Calendar.DAY_OF_MONTH));



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_today,
                container, false);


        RecyclerView rcvData = (RecyclerView) view.findViewById(R.id.order_today_recycler);;
        //rcvData.setHasFixedSize(true);
        rcvData.setLayoutManager(new LinearLayoutManager(view.getContext()));

        final OrderAdapter orderAdapter = new OrderAdapter();
        rcvData.setAdapter(orderAdapter);
        //Set up view model
        upcomingOrderViewModel = new ViewModelProvider(this).get(UpcomingOrderViewModel.class);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        orderViewModel.getAllOrder().observe(getActivity(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orderEntities) {
                //Update Recycle View
                orderAdapter.setOrder(orderEntities);
            }
        });

//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
//                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                orderViewModel.delete(orderAdapter.getOrderAt(viewHolder.getAdapterPosition()));
//                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
//            }
//        }).attachToRecyclerView(rcvData);

        //Sent data to Order Info when click order
        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order order) {
                Intent intent = new Intent(getActivity(), OrderInfoTodayActivity.class);
                intent.putExtra(OrderInfoTodayActivity.EXTRA_ORDER_ID, order.getId());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_ORDER_NAME, order.getClientName());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_ORDER_ADDRESS, order.getAddress());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_ORDER_TIME, order.getTime());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_ORDER_DATE, order.getDate());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_ORDER_NUMBER, order.getPhoneNumber());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_CHECK_PAID, order.getPaid());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_CHECK_SHIP, order.getShip());
                startActivityForResult(intent, CONFIRM_ORDER_REQUEST);
            }
        });
        //Delete item
        orderAdapter.setOnItemClickDelListener(new OrderAdapter.OnItemClickDelListener() {
            @Override
            public void onItemClickDel(Order order) {
                orderViewModel.delete(order);
            }
        });


        //Button to launch New Today Order Activity
        btnAddNewOrder = (Button) view.findViewById(R.id.add_new_today_order);
        btnAddNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewTodayOrderActivity.class);
                startActivityForResult(intent, ADD_ORDER_REQUEST);
            }
        });



        return view;
    }

    //Get data return from Intent to update order
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Add new order
        if (requestCode == ADD_ORDER_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(NewTodayOrderActivity.EXTRA_ORDER_NAME);
            String address = data.getStringExtra(NewTodayOrderActivity.EXTRA_ORDER_ADDRESS);
            String number = data.getStringExtra(NewTodayOrderActivity.EXTRA_ORDER_NUMBER);
            String time = data.getStringExtra(NewTodayOrderActivity.EXTRA_ORDER_TIME);
            String date = data.getStringExtra(NewTodayOrderActivity.EXTRA_ORDER_DATE);
            int intOrderDay = Integer.parseInt(date);
            //Reset ship and paid immediately after add new order.
            ship = false;
            paid = false;
            if (intOrderDay > today){
                //Move order to upcomming order if order's day > today.
                UpcomingOrder upcomingOrder = new UpcomingOrder(name, number,
                        address, date, time, 1000, paid);
                upcomingOrderViewModel.insert(upcomingOrder);
            } else { //else add to new order's today
                Order order = new Order(name, number, address, date, time, 1000, ship, paid);
                orderViewModel.insert(order);
            }

            //Update order
        } else if (requestCode == CONFIRM_ORDER_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(OrderInfoTodayActivity.EXTRA_ORDER_ID, -1);
            String name = data.getStringExtra(OrderInfoTodayActivity.EXTRA_ORDER_NAME);
            String address = data.getStringExtra(OrderInfoTodayActivity.EXTRA_ORDER_ADDRESS);
            String number = data.getStringExtra(OrderInfoTodayActivity.EXTRA_ORDER_NUMBER);
            String time = data.getStringExtra(OrderInfoTodayActivity.EXTRA_ORDER_TIME);
            String date = data.getStringExtra(OrderInfoTodayActivity.EXTRA_ORDER_DATE);
            paid = data.getBooleanExtra(OrderInfoTodayActivity.EXTRA_CHECK_PAID, paid);
            ship = data.getBooleanExtra(OrderInfoTodayActivity.EXTRA_CHECK_SHIP, ship);
            if (id == -1){
                Toast.makeText(getActivity(), "Order can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            Order order = new Order(name, number, address, date, time, 1000, ship, paid);
            order.setId(id);
            orderViewModel.update(order);
            Toast.makeText(getActivity(), "Order updated successfully", Toast.LENGTH_SHORT).show();
        } else {

            //Do nothing

        }
    }
}