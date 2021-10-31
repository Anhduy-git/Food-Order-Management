package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnClient;
    Button btnMenu;
    Button btnOrder;
    Button btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button to launch Client Activity
        btnClient = (Button)findViewById(R.id.client);
        btnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Client.class);
                startActivity(i);
            }
        });
        //Button to launch Menu Activity
        btnMenu = (Button)findViewById(R.id.menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Menu.class);
                startActivity(i);
            }
        });
        //Button to launch Order Activity
        btnOrder = (Button)findViewById(R.id.order);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Order.class);
                startActivity(i);
            }
        });

    }
}