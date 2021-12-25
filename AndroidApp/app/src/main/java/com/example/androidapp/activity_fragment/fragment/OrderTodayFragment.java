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
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.activity_fragment.activity.NewOrderActivity;
import com.example.androidapp.activity_fragment.activity.OrderInfoTodayActivity;
import com.example.androidapp.R;
import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.historydata.HistoryOrder;
import com.example.androidapp.data.historydata.HistoryOrderViewModel;
import com.example.androidapp.data.menudata.Dish;
import com.example.androidapp.data.orderdata.Order;
import com.example.androidapp.data.orderdata.OrderAdapter;
import com.example.androidapp.data.orderdata.OrderViewModel;
import com.example.androidapp.data.upcomingorderdata.UpcomingOrder;
import com.example.androidapp.data.upcomingorderdata.UpcomingOrderViewModel;

import org.joda.time.DateTimeComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderTodayFragment extends Fragment {
    private Button btnAddNewOrder;
    private TextView numOrderToday;
    public static final int ADD_ORDER_REQUEST = 1;
    public static final int CONFIRM_ORDER_REQUEST = 2;
    //View model
    private OrderViewModel orderViewModel;
    private UpcomingOrderViewModel upcomingOrderViewModel;
    public static List<Dish> mOrderListDish = new ArrayList<>();
    //sound
    private MediaPlayer sound = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_today,
                container, false);

        RecyclerView rcvData = (RecyclerView) view.findViewById(R.id.order_today_recycler);
        rcvData.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //Sound
        sound = MediaPlayer.create(getActivity(), R.raw.confirm_sound);

        numOrderToday = (TextView)view.findViewById(R.id.order_num);

        final OrderAdapter orderAdapter = new OrderAdapter();
        rcvData.setAdapter(orderAdapter);
        //Set up view model
        upcomingOrderViewModel = new ViewModelProvider(this).get(UpcomingOrderViewModel.class);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.getAllOrder().observe(getActivity(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                //Update Recycle View
                orderAdapter.submitList(orders);
                //Display number of order today
                numOrderToday.setText(String.format("%d", orders.size()));

            }
        });


        //Send data to Order Info when click order
        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order order) {
                Intent intent = new Intent(getActivity(), OrderInfoTodayActivity.class);
                intent.putExtra(OrderInfoTodayActivity.EXTRA_ORDER_ID, order.getId());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_ORDER_NAME, order.getClient().getClientName());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_ORDER_ADDRESS, order.getClient().getAddress());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_ORDER_TIME, order.getTime());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_ORDER_DATE, order.getDate());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_ORDER_NUMBER, order.getClient().getPhoneNumber());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_CHECK_PAID, order.getPaid());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_CHECK_SHIP, order.getShip());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_ORDER_PRICE, order.getPrice());
                intent.putExtra(OrderInfoTodayActivity.EXTRA_ORDER_IMAGE, order.getClient().getImageDir());
                intent.putParcelableArrayListExtra(OrderInfoTodayActivity.EXTRA_ORDER_DISH_LIST, (ArrayList<? extends Parcelable>) order.getOrderListDish());
                startActivityForResult(intent, CONFIRM_ORDER_REQUEST);
            }
        });

        //Delete item
        orderAdapter.setOnItemClickDelListener(new OrderAdapter.OnItemClickDelListener() {
            @Override
            public void onItemClickDel(Order order) {
                confirmDelDialog(order);
            }
        });

        //Button to launch New Today Order Activity
        btnAddNewOrder = (Button) view.findViewById(R.id.add_new_today_order);
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
        //Add new order
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

            //Only compare the date
            DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            //Get the current date
            Date today  = calendar.getTime();
            try {
                Date orderDate = simpleDateFormat.parse(date);
                int ret = dateTimeComparator.compare(orderDate, today);
                if (ret > 0){
                    //Move order to upcoming order if order's day > today.
                    UpcomingOrder upcomingOrder = new UpcomingOrder(client, date, time, price, false, mOrderListDish);
                    upcomingOrderViewModel.insert(upcomingOrder);
                } else { //else add to new order's today
                    Order order = new Order(client, date, time, price, false, false, mOrderListDish);
                    orderViewModel.insert(order);
                }

            } catch (ParseException ex) {
                Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
            }


            //Update order (paid, ship)
        } else if (requestCode == CONFIRM_ORDER_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(OrderInfoTodayActivity.EXTRA_ORDER_ID, -1);
            String name = data.getStringExtra(OrderInfoTodayActivity.EXTRA_ORDER_NAME);
            String address = data.getStringExtra(OrderInfoTodayActivity.EXTRA_ORDER_ADDRESS);
            String number = data.getStringExtra(OrderInfoTodayActivity.EXTRA_ORDER_NUMBER);
            String time = data.getStringExtra(OrderInfoTodayActivity.EXTRA_ORDER_TIME);
            String date = data.getStringExtra(OrderInfoTodayActivity.EXTRA_ORDER_DATE);
            String imageDir = data.getStringExtra(OrderInfoTodayActivity.EXTRA_ORDER_IMAGE);

            boolean paid = data.getBooleanExtra(OrderInfoTodayActivity.EXTRA_CHECK_PAID, false);
            boolean ship = data.getBooleanExtra(OrderInfoTodayActivity.EXTRA_CHECK_SHIP, false);
            boolean confirmShip = data.getBooleanExtra(OrderInfoTodayActivity.EXTRA_CHECK_CONFIRM_SHIP, false);

            if (id == -1){
                Toast.makeText(getActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                return;
            }
            Client client = new Client(name, number, address, imageDir);
            mOrderListDish = data.getParcelableArrayListExtra(OrderInfoTodayActivity.EXTRA_ORDER_DISH_LIST);
            int price = calculateOrderPrice(mOrderListDish);

            Order order = new Order(client, date, time, price, ship, paid, mOrderListDish);
            order.setId(id);
            orderViewModel.update(order);
            //if shipped, then move to history
            if(confirmShip) {
                HistoryOrderViewModel historyOrderViewModel;
                historyOrderViewModel = new ViewModelProvider(this).get(HistoryOrderViewModel.class);
                //Move to history all success order
                HistoryOrder historyOrder = new HistoryOrder(client, order.getDate(), order.getTime(), order.getPrice(), order.getShip(), order.getPaid(), order.getOrderListDish());
                historyOrderViewModel.insert(historyOrder);
            }
        }
    }
    int calculateOrderPrice(List<Dish> listDish){
        int price = 0;
        for (Dish dish : listDish) {
            price += dish.getPrice() * dish.getQuantity();
        }
        return price;
    }
    private void confirmDelDialog(Order order) {
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
                orderViewModel.delete(order);
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