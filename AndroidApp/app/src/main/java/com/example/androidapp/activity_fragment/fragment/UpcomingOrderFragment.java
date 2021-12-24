package com.example.androidapp.activity_fragment.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.activity_fragment.activity.NewOrderActivity;
import com.example.androidapp.activity_fragment.activity.OrderInfoTodayActivity;
import com.example.androidapp.activity_fragment.activity.OrderInfoUpcomingActivity;
import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.menudata.Dish;
import com.example.androidapp.data.upcomingorderdata.UpcomingOrder;
import com.example.androidapp.data.upcomingorderdata.UpcomingOrderAdapter;
import com.example.androidapp.data.upcomingorderdata.UpcomingOrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class UpcomingOrderFragment extends Fragment {
    public Button btnAddNewOrder;
    public static final int ADD_ORDER_REQUEST = 1;
    public static final int CONFIRM_ORDER_REQUEST = 2;
    //View model
    private UpcomingOrderViewModel upcomingOrderViewModel;
    public static List<Dish> mOrderListDish = new ArrayList<>();
    //sound
    private MediaPlayer sound = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Button to launch Add New Order Activity
        View view = inflater.inflate(R.layout.fragment_upcoming_order,
                container, false);

        RecyclerView rcvData = (RecyclerView) view.findViewById(R.id.upcoming_recycler);;
        rcvData.setLayoutManager(new LinearLayoutManager(view.getContext()));

        final UpcomingOrderAdapter upcomingOrderAdapter = new UpcomingOrderAdapter();
        rcvData.setAdapter(upcomingOrderAdapter);

        upcomingOrderViewModel = new ViewModelProvider(this).get(UpcomingOrderViewModel.class);
        upcomingOrderViewModel.getAllUpcomingOrder().observe(getActivity(), new Observer<List<UpcomingOrder>>() {
            @Override
            public void onChanged(List<UpcomingOrder> upcomingOrders) {
                //Update Recycle View
                upcomingOrderAdapter.submitList(upcomingOrders);
            }
        });
        //Sound
        sound = MediaPlayer.create(getActivity(), R.raw.confirm_sound);

        //Send data to Order Info when click order
        upcomingOrderAdapter.setOnItemClickListener(new UpcomingOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UpcomingOrder upcomingOrder) {
                Intent intent = new Intent(getActivity(), OrderInfoUpcomingActivity.class);
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_ID, upcomingOrder.getId());
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_NAME, upcomingOrder.getClient().getClientName());
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_PRICE, upcomingOrder.getPrice());
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_ADDRESS, upcomingOrder.getClient().getAddress());
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_IMAGE, upcomingOrder.getClient().getImageDir());
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_TIME, upcomingOrder.getTime());
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_DATE, upcomingOrder.getDate());
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_NUMBER, upcomingOrder.getClient().getPhoneNumber());
                intent.putExtra(OrderInfoUpcomingActivity.EXTRA_CHECK_PAID, upcomingOrder.getPaid());
                intent.putParcelableArrayListExtra(OrderInfoTodayActivity.EXTRA_ORDER_DISH_LIST, (ArrayList<? extends Parcelable>) upcomingOrder.getOrderListDish());
                startActivityForResult(intent, CONFIRM_ORDER_REQUEST);
            }
        });

        //Delete item
        upcomingOrderAdapter.setOnItemClickDelListener(new UpcomingOrderAdapter.OnItemClickDelListener() {
            @Override
            public void onItemClickDel(UpcomingOrder upcomingOrder) {
                confirmDelDialog(upcomingOrder);
            }
        });

        //Button to launch New Upcoming Order Activity
        btnAddNewOrder = (Button) view.findViewById(R.id.add_new_upcoming_order);
        btnAddNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewOrderActivity.class);
                startActivityForResult(intent, ADD_ORDER_REQUEST);
            }
        });
        return view;
    }

    //Get data return from Intent to update order
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ORDER_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(NewOrderActivity.EXTRA_ORDER_NAME);
            String address = data.getStringExtra(NewOrderActivity.EXTRA_ORDER_ADDRESS);
            String number = data.getStringExtra(NewOrderActivity.EXTRA_ORDER_NUMBER);
            String time = data.getStringExtra(NewOrderActivity.EXTRA_ORDER_TIME);
            String date = data.getStringExtra(NewOrderActivity.EXTRA_ORDER_DATE);
            String imageDir = data.getStringExtra(NewOrderActivity.EXTRA_ORDER_IMAGE);
            Client client = new Client(name, number, address, imageDir);
            mOrderListDish = data.getParcelableArrayListExtra(NewOrderActivity.EXTRA_ORDER_DISH_LIST);
            int price = calculateOrderPrice(mOrderListDish);

            UpcomingOrder upcomingOrder = new UpcomingOrder(client, date, time, price, false, mOrderListDish);
            upcomingOrderViewModel.insert(upcomingOrder);

        } else if (requestCode == CONFIRM_ORDER_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_ID, -1);
            String name = data.getStringExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_NAME);
            String address = data.getStringExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_ADDRESS);
            String number = data.getStringExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_NUMBER);
            String time = data.getStringExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_TIME);
            String date = data.getStringExtra(OrderInfoUpcomingActivity.EXTRA_ORDER_DATE);
            String imageDir = data.getStringExtra(NewOrderActivity.EXTRA_ORDER_IMAGE);
            Client client = new Client(name, number, address, imageDir);
            mOrderListDish = data.getParcelableArrayListExtra(NewOrderActivity.EXTRA_ORDER_DISH_LIST);
            int price = calculateOrderPrice(mOrderListDish);
            boolean paid = data.getBooleanExtra(OrderInfoUpcomingActivity.EXTRA_CHECK_PAID, false);
            if (id == -1){
                Toast.makeText(getActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                return;
            }

            UpcomingOrder upcomingOrder = new UpcomingOrder(client, date, time, price, paid, mOrderListDish);
            upcomingOrder.setId(id);
            upcomingOrderViewModel.update(upcomingOrder);
        }
        else {
            //Do nothing
        }
    }

    int calculateOrderPrice(List<Dish> listDish){
        int price = 0;
        for (Dish dish : listDish) {
            price += dish.getPrice() * dish.getQuantity();
        }
        return price;
    }

    private void confirmDelDialog(UpcomingOrder upcomingOrder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog_delete, (RelativeLayout)getView().findViewById(R.id.layout_dialog)
        );
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        //confirm paid btn
        view.findViewById(R.id.confirm_dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                sound.start();
                upcomingOrderViewModel.delete(upcomingOrder);
            }
        });
        //cancel btn
        view.findViewById(R.id.cancel_dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}
