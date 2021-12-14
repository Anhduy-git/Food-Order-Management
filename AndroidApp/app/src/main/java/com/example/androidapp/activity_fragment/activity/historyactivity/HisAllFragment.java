package com.example.androidapp.activity_fragment.activity.historyactivity;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.activity_fragment.activity.OrderInfoHistoryActivity;
import com.example.androidapp.activity_fragment.activity.OrderInfoTodayActivity;
import com.example.androidapp.activity_fragment.activity.OrderInfoUnpaidActivity;
import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.historydata.HistoryOrder;
import com.example.androidapp.data.historydata.HistoryOrderAdapter;
import com.example.androidapp.data.historydata.HistoryOrderViewModel;
import com.example.androidapp.data.menudata.Dish;
import com.example.androidapp.data.unpaiddata.UnpaidOrder;
import com.example.androidapp.data.unpaiddata.UnpaidOrderAdapter;
import com.example.androidapp.data.unpaiddata.UnpaidOrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class HisAllFragment extends Fragment {
    public static final int VIEW_HISTORY_ORDER_REQUEST = 1;
    private HistoryOrderViewModel historyOrderViewModel;
    public static List<Dish> mOrderListDish = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Button to launch Add New Order Activity
        View view = inflater.inflate(R.layout.fragment_all_history,
                container, false);


        RecyclerView rcvData = (RecyclerView) view.findViewById(R.id.his_all_recycler);;
        //rcvData.setHasFixedSize(true);
        rcvData.setLayoutManager(new LinearLayoutManager(view.getContext()));
        final HistoryOrderAdapter historyOrderAdapter = new HistoryOrderAdapter();
        rcvData.setAdapter(historyOrderAdapter);

        historyOrderViewModel = new ViewModelProvider(this).get(HistoryOrderViewModel.class);
        historyOrderViewModel.getAllHistoryOrder().observe(getActivity(), new Observer<List<HistoryOrder>>() {
            @Override
            public void onChanged(List<HistoryOrder> historyOrders) {
                //Update Recycle View
                historyOrderAdapter.submitList(historyOrders);
            }
        });

        //Send data to Order Info when click order
        historyOrderAdapter.setOnItemClickListener(new HistoryOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(HistoryOrder historyOrder) {
                Intent intent = new Intent(getActivity(), OrderInfoHistoryActivity.class);
                intent.putExtra(OrderInfoHistoryActivity.EXTRA_ORDER_ID, historyOrder.getId());
                intent.putExtra(OrderInfoHistoryActivity.EXTRA_ORDER_NAME, historyOrder.getClient().getClientName());
                intent.putExtra(OrderInfoHistoryActivity.EXTRA_ORDER_ADDRESS, historyOrder.getClient().getAddress());
                intent.putExtra(OrderInfoHistoryActivity.EXTRA_ORDER_TIME, historyOrder.getTime());
                intent.putExtra(OrderInfoHistoryActivity.EXTRA_ORDER_DATE, historyOrder.getDate());
                intent.putExtra(OrderInfoHistoryActivity.EXTRA_ORDER_NUMBER, historyOrder.getClient().getPhoneNumber());
                intent.putExtra(OrderInfoHistoryActivity.EXTRA_ORDER_PRICE, historyOrder.getPrice());
                intent.putExtra(OrderInfoHistoryActivity.EXTRA_ORDER_IMAGE, historyOrder.getClient().getImageDir());
                intent.putParcelableArrayListExtra(OrderInfoHistoryActivity.EXTRA_ORDER_DISH_LIST, (ArrayList<? extends Parcelable>) historyOrder.getOrderListDish());
                startActivityForResult(intent, VIEW_HISTORY_ORDER_REQUEST);
            }
        });
        return view;
    }

}
