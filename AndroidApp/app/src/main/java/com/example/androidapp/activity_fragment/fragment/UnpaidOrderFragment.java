package com.example.androidapp.activity_fragment.fragment;

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
import com.example.androidapp.activity_fragment.activity.OrderInfoTodayActivity;
import com.example.androidapp.activity_fragment.activity.OrderInfoUnpaidActivity;
import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.menudata.Dish;
import com.example.androidapp.data.unpaiddata.UnpaidOrder;
import com.example.androidapp.data.unpaiddata.UnpaidOrderAdapter;
import com.example.androidapp.data.unpaiddata.UnpaidOrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class UnpaidOrderFragment extends Fragment {
    public static final int CONFIRM_UNPAID_ORDER_REQUEST = 1;
    private UnpaidOrderViewModel unpaidOrderViewModel;
    private boolean paid = false;
    public static List<Dish> mOrderListDish = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Button to launch Add New Order Activity
        View view = inflater.inflate(R.layout.fragment_unpaid_order,
                container, false);


        RecyclerView rcvData = (RecyclerView) view.findViewById(R.id.unpaid_order_recycler);;
        //rcvData.setHasFixedSize(true);
        rcvData.setLayoutManager(new LinearLayoutManager(view.getContext()));

        final UnpaidOrderAdapter unpaidOrderAdapter = new UnpaidOrderAdapter();
        rcvData.setAdapter(unpaidOrderAdapter);

        unpaidOrderViewModel = new ViewModelProvider(this).get(UnpaidOrderViewModel.class);
        unpaidOrderViewModel.getAllUnpaidOrder().observe(getActivity(), new Observer<List<UnpaidOrder>>() {
            @Override
            public void onChanged(List<UnpaidOrder> unpaidOrders) {
                //Update Recycle View
                unpaidOrderAdapter.submitList(unpaidOrders);
            }
        });

        //Send data to Order Info when click order
        unpaidOrderAdapter.setOnItemClickListener(new UnpaidOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UnpaidOrder unpaidOrder) {
                Intent intent = new Intent(getActivity(), OrderInfoUnpaidActivity.class);
                intent.putExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_ID, unpaidOrder.getId());
                intent.putExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_NAME, unpaidOrder.getClient().getClientName());
                intent.putExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_ADDRESS, unpaidOrder.getClient().getAddress());
                intent.putExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_IMAGE, unpaidOrder.getClient().getImage());
                intent.putExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_TIME, unpaidOrder.getTime());
                intent.putExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_DATE, unpaidOrder.getDate());
                intent.putExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_NUMBER, unpaidOrder.getClient().getPhoneNumber());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_ORDER_PRICE, unpaidOrder.getPrice());

                intent.putParcelableArrayListExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_DISH_LIST, (ArrayList<? extends Parcelable>) unpaidOrder.getOrderListDish());
                startActivityForResult(intent, CONFIRM_UNPAID_ORDER_REQUEST);
            }
        });
        return view;
    }
    //Get data return from Intent to update order
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONFIRM_UNPAID_ORDER_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_ID, -1);
            String name = data.getStringExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_NAME);
            String address = data.getStringExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_ADDRESS);
            String number = data.getStringExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_NUMBER);
            String time = data.getStringExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_TIME);
            String date = data.getStringExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_DATE);
            byte[] image = data.getByteArrayExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_IMAGE);

            if (id == -1){
                Toast.makeText(getActivity(), "Unpaid Order can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            mOrderListDish = data.getParcelableArrayListExtra(OrderInfoUnpaidActivity.EXTRA_ORDER_DISH_LIST);
            int price = calculateOrderPrice(mOrderListDish);
            Client client = new Client(name, number, address, image);

            UnpaidOrder unpaidOrder = new UnpaidOrder(client, date, time, price, paid, mOrderListDish);
            unpaidOrder.setId(id);
            unpaidOrderViewModel.delete(unpaidOrder);
            Toast.makeText(getActivity(), "Order updated successfully", Toast.LENGTH_SHORT).show();
        }

    }
    int calculateOrderPrice(List<Dish> listDish){
        int price = 0;
        for (Dish dish : listDish) {
            price += dish.getPrice() * dish.getQuantity();
        }
        return price;
    }

}
