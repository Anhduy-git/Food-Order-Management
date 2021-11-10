package com.example.androidapp.activity_fragment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.R;
import com.example.androidapp.activity_fragment.fragment.ClientFragment;
import com.example.androidapp.activity_fragment.fragment.MenuFragment;
import com.example.androidapp.activity_fragment.fragment.OrderTodayFragment;
import com.example.androidapp.activity_fragment.fragment.UnpaidOrderFragment;
import com.example.androidapp.activity_fragment.fragment.UpcomingOrderFragment;
import com.example.androidapp.data.orderdata.Order;
import com.example.androidapp.data.orderdata.OrderViewModel;
import com.example.androidapp.data.unpaiddata.UnpaidOrder;
import com.example.androidapp.data.unpaiddata.UnpaidOrderViewModel;
import com.example.androidapp.data.upcomingorderdata.UpcomingOrder;
import com.example.androidapp.data.upcomingorderdata.UpcomingOrderViewModel;
import com.google.android.material.navigation.NavigationView;



import java.util.Calendar;

import java.util.List;

import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout mDrawerLayout;

    private static final int FRAGMENT_ORDER_TODAY = 0;
    private static final int FRAGMENT_UPCOMING_ORDER = 1;
    private static final int FRAGMENT_CLIENT = 2;
    private static final int FRAGMENT_MENU = 3;
    private static final int ACTIVITY_HISTORY = 4;
    private static final int FRAGMENT_UNPAID_ORDER = 5;

    private int mCurrentFragment = FRAGMENT_ORDER_TODAY;
    private OrderViewModel orderViewModel;
    private UnpaidOrderViewModel unpaidOrderViewModel;
    private UpcomingOrderViewModel upcomingOrderViewModel;
    private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Test
        Log.d("test", "On create");

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Today");

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        replaceFragment(new OrderTodayFragment());


        //Get the current day
        int today = calendar.get(Calendar.DAY_OF_MONTH);

        //Setup View Model
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        unpaidOrderViewModel = new ViewModelProvider(this).get(UnpaidOrderViewModel.class);
        upcomingOrderViewModel = new ViewModelProvider(this).get(UpcomingOrderViewModel.class);


        //Update Unpaid Order and History
        orderViewModel.getAllOrder().observe(MainActivity.this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                for (Order order : orders) {
                    int orderDay = Integer.parseInt(order.getDate());
                    if (orderDay < today) {
                        //if shipped
                        if (!order.getShip()) {
                            //if paid then move only to history success order
                            if (order.getPaid()) {
                                //
                            } else { // move to history success and unpaid order.
                                UnpaidOrder unpaidOrder = new UnpaidOrder(order.getClientName(), order.getPhoneNumber(),
                                        order.getAddress(), order.getDate(), order.getTime(), order.getPrice(), false);
                                unpaidOrderViewModel.insert(unpaidOrder);
                            }
                        } else {
                            //Move to history cancel
                        }
                        //Remove all old order
                        orderViewModel.delete(order);
                    }

                }
            }
        });
        //Update Upcoming Order to Order Today
        upcomingOrderViewModel.getAllUpcomingOrder().observe(MainActivity.this, new Observer<List<UpcomingOrder>>() {
            @Override
            public void onChanged(List<UpcomingOrder> upcomingOrders) {
                for (UpcomingOrder upcomingOrder : upcomingOrders) {
                    //Get the day of upcomingOrder
                    int upcomingOrderDay = Integer.parseInt(upcomingOrder.getDate());
                    //Check if the upcomingOrderDay is today
                    if (upcomingOrderDay == today) {
                        Order order = new Order(upcomingOrder.getClientName(), upcomingOrder.getPhoneNumber(),
                                upcomingOrder.getAddress(), upcomingOrder.getDate(), upcomingOrder.getTime(), upcomingOrder.getPrice(), false, upcomingOrder.getPaid());
                        //add upcomingOrder to today's Order
                        orderViewModel.insert(order);
                        //remove that upcomingOrder
                        upcomingOrderViewModel.delete(upcomingOrder);
                    }

                }
            }
        });
        //Update Unpaid Order when confirm paid
        unpaidOrderViewModel.getAllUnpaidOrder().observe(MainActivity.this, new Observer<List<UnpaidOrder>>() {
            @Override
            public void onChanged(List<UnpaidOrder> unpaidOrders) {
                for (UnpaidOrder unpaidOrder : unpaidOrders) {
                    //Check if paid
                    if (unpaidOrder.getPaid()) {
                        unpaidOrderViewModel.delete(unpaidOrder);
                    }

                }
            }
        });
    }

    //test
//    @Override
//    public void onStart(){
//        super.onStart();
//        Log.d("test", "onStart");
//    }
//    @Override
//    public void onPause(){
//        super.onPause();
//        Log.d("test", "onPause");
//    }
//    @Override
//    public void onRestart(){
//        super.onRestart();
//        Log.d("test", "onRestart");
//    }
//    @Override
//    public void onResume(){
//        super.onResume();
//        Log.d("test", "onResume");
//    }
//    @Override
//    public void onStop(){
//        super.onStop();
//        Log.d("test", "onStop");
//    }
//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        Log.d("test", "onDestroy");
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.order_today) {
            if (mCurrentFragment != FRAGMENT_ORDER_TODAY) {
                replaceFragment(new OrderTodayFragment());
                getSupportActionBar().setTitle("Order Today");
                mCurrentFragment = FRAGMENT_ORDER_TODAY;
            }
        } else if (id == R.id.upcoming_order) {
            if (mCurrentFragment != FRAGMENT_UPCOMING_ORDER) {
                replaceFragment(new UpcomingOrderFragment());
                getSupportActionBar().setTitle("Upcoming Order");
                mCurrentFragment = FRAGMENT_UPCOMING_ORDER;
            }
        } else if (id == R.id.client) {
            if (mCurrentFragment != FRAGMENT_CLIENT) {
                replaceFragment(new ClientFragment());
                getSupportActionBar().setTitle("Client");
                mCurrentFragment = FRAGMENT_CLIENT;
            }
        } else if (id == R.id.menu) {
            if (mCurrentFragment != FRAGMENT_MENU) {
                replaceFragment(new MenuFragment());
                getSupportActionBar().setTitle("Menu");
                mCurrentFragment = FRAGMENT_MENU;
            }
        } else if (id == R.id.history) {
            if (mCurrentFragment != ACTIVITY_HISTORY) {
                mCurrentFragment = ACTIVITY_HISTORY;
                Intent intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.unpaid_order) {
            if (mCurrentFragment != FRAGMENT_UNPAID_ORDER) {
                replaceFragment(new UnpaidOrderFragment());
                getSupportActionBar().setTitle("Unpaid Order");
                mCurrentFragment = FRAGMENT_UNPAID_ORDER;
            }
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment (Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
}