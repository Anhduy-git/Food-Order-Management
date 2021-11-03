package com.example.androidapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.MainActivity;
import com.example.androidapp.NewOrderActivity;
import com.example.androidapp.R;
import com.example.androidapp.orderdata.OrderEntity;
import com.example.androidapp.orderdata.OrderViewModel;

import java.util.List;

public class OrderTodayFragment extends Fragment {
    public Button btnAddNewOrder;

    //View model
    private OrderViewModel orderViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Button to launch Add New Order Activity
        View view = inflater.inflate(R.layout.fragment_order_today,
                container, false);
        btnAddNewOrder = (Button) view.findViewById(R.id.add_new_order);
        btnAddNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewOrderActivity.class);
                startActivity(intent);
            }
        });


        //Data

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.getAllOrder().observe(getActivity(), new Observer<List<OrderEntity>>() {
            @Override
            public void onChanged(List<OrderEntity> orderEntities) {
                //Update Recycle View
            }
        });


        return view;
    }
}



