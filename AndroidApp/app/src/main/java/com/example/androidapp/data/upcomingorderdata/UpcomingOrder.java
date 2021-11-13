package com.example.androidapp.data.upcomingorderdata;

import androidx.room.Entity;

import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.orderdata.Order;

@Entity(tableName = "upcoming_order_table")
public class UpcomingOrder extends Order {
    public UpcomingOrder(Client client, String date, String time, int price, boolean paid){
        super(client, date,  time, price, false, paid);
    }
}
