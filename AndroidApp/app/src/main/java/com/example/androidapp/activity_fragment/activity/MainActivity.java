package com.example.androidapp.activity_fragment.activity;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.androidapp.R;
import com.example.androidapp.activity_fragment.fragment.ClientFragment;
import com.example.androidapp.activity_fragment.fragment.MenuFragment;
import com.example.androidapp.activity_fragment.fragment.OrderTodayFragment;
import com.example.androidapp.activity_fragment.fragment.UnpaidOrderFragment;
import com.example.androidapp.activity_fragment.fragment.UpcomingOrderFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btnClient;
    Button btnMenu;
    Button btnOrder;
    Button btnHistory;
    private DrawerLayout mDrawerLayout;

    private static final int FRAGMENT_ORDER_TODAY = 0;
    private static final int FRAGMENT_UPCOMING_ORDER = 1;
    private static final int FRAGMENT_CLIENT = 2;
    private static final int FRAGMENT_MENU = 3;
    private static final int ACTIVITY_HISTORY = 4;
    private static final int FRAGMENT_UNPAID_ORDER = 5;

    private int mCurrentFragment = FRAGMENT_ORDER_TODAY;


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
    }

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