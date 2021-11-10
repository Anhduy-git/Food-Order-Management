package com.example.androidapp.data.upcomingorderdata;

import androidx.room.Entity;

import com.example.androidapp.data.orderdata.Order;

@Entity(tableName = "upcoming_order_table")
public class UpcomingOrder extends Order {
    public UpcomingOrder(String clientName, String phoneNumber, String address, String date, String time, int price, boolean paid){
        super(clientName, phoneNumber, address,  date,  time, price, false, paid);
    }
}
