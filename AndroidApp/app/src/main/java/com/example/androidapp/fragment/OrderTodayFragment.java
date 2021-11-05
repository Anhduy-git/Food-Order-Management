package com.example.androidapp.fragment;


import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.androidapp.NewOrderActivity;
import com.example.androidapp.OrderInfo1Activity;
import com.example.androidapp.R;
import com.example.androidapp.orderdata.Order;
import com.example.androidapp.orderdata.OrderAdapter;
import com.example.androidapp.orderdata.OrderViewModel;

import java.util.List;

public class OrderTodayFragment extends Fragment {
    public Button btnAddNewOrder;
    public static final int ADD_ORDER_REQUEST = 1;
    public static final int VIEW_ORDER_REQUEST = 2;
    //View model
    private OrderViewModel orderViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Button to launch Add New Order Activity
        View view = inflater.inflate(R.layout.fragment_order_today,
                container, false);


        RecyclerView rcvData = (RecyclerView) view.findViewById(R.id.order_today_recycler);;
        //rcvData.setHasFixedSize(true);
        rcvData.setLayoutManager(new LinearLayoutManager(view.getContext()));

        final OrderAdapter orderAdapter = new OrderAdapter();
        rcvData.setAdapter(orderAdapter);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.getAllOrder().observe(getActivity(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orderEntities) {
                //Update Recycle View
                orderAdapter.setOrder(orderEntities);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                orderViewModel.delete(orderAdapter.getOrderAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(rcvData);

        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order order) {
                Intent intent = new Intent(getActivity(), OrderInfo1Activity.class);
                intent.putExtra(OrderInfo1Activity.EXTRA_ORDER_ID, order.getId());
                intent.putExtra(OrderInfo1Activity.EXTRA_ORDER_NAME_READ_ONLY, order.getClientName());
                intent.putExtra(OrderInfo1Activity.EXTRA_ORDER_ADDRESS_READ_ONLY, order.getAddress());
                intent.putExtra(OrderInfo1Activity.EXTRA_ORDER_TIME_READ_ONLY, order.getTime());
                intent.putExtra(OrderInfo1Activity.EXTRA_ORDER_DATE_READ_ONLY, order.getDate());
                intent.putExtra(OrderInfo1Activity.EXTRA_ORDER_NUMBER_READ_ONLY, order.getPhoneNumber());
                startActivityForResult(intent, VIEW_ORDER_REQUEST);
            }
        });


        btnAddNewOrder = (Button) view.findViewById(R.id.add_new_order);
        btnAddNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewOrderActivity.class);
                startActivityForResult(intent, ADD_ORDER_REQUEST);
            }
        });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ORDER_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(NewOrderActivity.EXTRA_ORDER_NAME);
            String address = data.getStringExtra(NewOrderActivity.EXTRA_ORDER_ADDRESS);
            String number = data.getStringExtra(NewOrderActivity.EXTRA_ORDER_NUMBER);
            String time = data.getStringExtra(NewOrderActivity.EXTRA_ORDER_TIME);
            String date = data.getStringExtra(NewOrderActivity.EXTRA_ORDER_DATE);



            Order order = new Order(name, number, address, date, time, 1000);
            orderViewModel.insert(order);
        }
        //Toast a message if user press back button and don't add anything
        else {
            Toast.makeText(getActivity(), "Order not added", Toast.LENGTH_SHORT).show();
        }
    }
}



