package com.example.androidapp.activity_fragment.activity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.R;
import com.example.androidapp.activity_fragment.fragment.ClientFragment;
import com.example.androidapp.activity_fragment.fragment.MenuFragment;
import com.example.androidapp.activity_fragment.fragment.OrderTodayFragment;
import com.example.androidapp.activity_fragment.fragment.UnpaidOrderFragment;
import com.example.androidapp.activity_fragment.fragment.UpcomingOrderFragment;
import com.example.androidapp.data.AppDatabase;
import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.orderdata.Order;
import com.example.androidapp.data.orderdata.OrderViewModel;
import com.example.androidapp.data.unpaiddata.UnpaidOrder;
import com.example.androidapp.data.unpaiddata.UnpaidOrderViewModel;
import com.example.androidapp.data.upcomingorderdata.UpcomingOrder;
import com.example.androidapp.data.upcomingorderdata.UpcomingOrderViewModel;
import com.google.android.material.navigation.NavigationView;


import org.joda.time.DateTimeComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;

import java.util.Date;
import java.util.List;



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
    private SimpleDateFormat simpleDateFormat;
    private DateTimeComparator dateTimeComparator;
    private Date today;
    private String tomorrow;
    private Calendar calendarNotification;
    private Calendar calendarToday;
    private Calendar calendarTomorrow;
    public static final String CHANNEL_ID = "CHANNEL 1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        //select the current fragment from intent
        selectFragment();

        //Setup View Model
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        unpaidOrderViewModel = new ViewModelProvider(this).get(UnpaidOrderViewModel.class);
        upcomingOrderViewModel = new ViewModelProvider(this).get(UpcomingOrderViewModel.class);
        //Date format
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //Only compare the date
        dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        //Get today's date
        calendarToday = Calendar.getInstance();
        today = calendarToday.getTime();
        //Get tomorrow's date
        calendarTomorrow = Calendar.getInstance();
        calendarTomorrow.add(Calendar.DAY_OF_YEAR, 1);
        tomorrow = simpleDateFormat.format(calendarTomorrow.getTime());

        //set notify
        updateNumTomorrowOrderAndNotify();

        //Update Upcoming Order to Order Today
        updateUpcomingOrder();

        //Update item from Today Order to Unpaid Order and History
        updateUnpaidOrderAndHistory();


    }


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
//
//        Log.d("test", "On resume");
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
    //Select the current fragment from intent
    private void  selectFragment() {
        int intentFragment = getIntent().getIntExtra("fragmentSelect", -1);

        switch (intentFragment){
            case FRAGMENT_UPCOMING_ORDER:
                replaceFragment(new UpcomingOrderFragment());
                getSupportActionBar().setTitle("Upcoming Order");
                mCurrentFragment = FRAGMENT_UPCOMING_ORDER;
                break;
        }
    }

    private void replaceFragment (Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotificationChannel";
            String description = "Channel for notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }

        }
    }
    private void updateUpcomingOrder() {
        upcomingOrderViewModel.getAllUpcomingOrder().observe(MainActivity.this, new Observer<List<UpcomingOrder>>() {
            @Override
            public void onChanged(List<UpcomingOrder> upcomingOrders) {
                for (UpcomingOrder upcomingOrder : upcomingOrders) {
                    //Get the day of upcomingOrder
                    try {
                        Date upcomingOrderDate = simpleDateFormat.parse(upcomingOrder.getDate());
                        //Check if the upcomingOrderDay is today
                        int ret = dateTimeComparator.compare(upcomingOrderDate, today);
                        if (ret == 0) {
                            Client client = new Client(upcomingOrder.getClient().getClientName(), upcomingOrder.getClient().getPhoneNumber(),
                                    upcomingOrder.getClient().getAddress());
                            Order order = new Order(client, upcomingOrder.getDate(), upcomingOrder.getTime(),
                                    upcomingOrder.getPrice(), false, upcomingOrder.getPaid(), upcomingOrder.getOrderListDish());
                            //add upcomingOrder to today's Order
                            orderViewModel.insert(order);
                            //remove that upcomingOrder
                            upcomingOrderViewModel.delete(upcomingOrder);
                        }

                    } catch (ParseException ex) {

                    }
                }
            }
        });
    }
    private void updateUnpaidOrderAndHistory() {
        orderViewModel.getAllOrder().observe(MainActivity.this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                for (Order order : orders) {
                    try {
                        Date orderDate = simpleDateFormat.parse(order.getDate());
                        int ret = dateTimeComparator.compare(orderDate, today);
                        if (ret < 0) {
                            //if shipped
                            if (order.getShip()) {
                                //if paid then move only to history success order
                                if (order.getPaid()) {
                                    //
                                } else { // move to history success and unpaid order.
                                    Client client = new Client(order.getClient().getClientName(), order.getClient().getPhoneNumber(),
                                            order.getClient().getAddress());
                                    UnpaidOrder unpaidOrder = new UnpaidOrder(client, order.getDate(), order.getTime(), order.getPrice(), false, order.getOrderListDish());
                                    unpaidOrderViewModel.insert(unpaidOrder);
                                }
                            } else {
                                //Move to history cancel
                            }
                            //Remove all old order
                            orderViewModel.delete(order);
                        }
                    } catch (ParseException ex) {

                    }
                }
            }
        });
    }
    private void updateNumTomorrowOrderAndNotify() {
        upcomingOrderViewModel.getAllUpcomingOrder().observe(MainActivity.this, new Observer<List<UpcomingOrder>>() {
            @Override
            public void onChanged(List<UpcomingOrder> upcomingOrders) {
                //get the number of tomorrow order and notify
                int numTomorrowOrder = getNumTomorrowOrder();
                //Notify
                if (numTomorrowOrder > 0) {
                    setNotification(numTomorrowOrder);
                }
            }
        });

    }
    private int getNumTomorrowOrder() {

        List<UpcomingOrder> list = AppDatabase.getInstance(this).upcomingOrderDao().getNumOrderTomorrow(tomorrow);
        return list.size();
    }
    private void setNotification(int numTomorrowOrder) {
        //Notification
        createNotificationChannel();


        //set time daily for notification
        calendarNotification = Calendar.getInstance();
        calendarNotification.set(Calendar.HOUR_OF_DAY, 20);
        calendarNotification.set(Calendar.MINUTE, 0);
        calendarNotification.set(Calendar.SECOND, 0);
        //set notify only 1 time in day
        if (Calendar.getInstance().after(calendarNotification)) {
            calendarNotification.add(Calendar.DAY_OF_MONTH, 1);
        }
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        intent.putExtra("numOrderTomorrow", numTomorrowOrder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //set notify daily
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendarNotification.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
