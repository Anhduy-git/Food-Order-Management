package com.example.androidapp.data.unpaiddata;

import androidx.room.Entity;

import com.example.androidapp.data.orderdata.Order;

@Entity(tableName = "unpaid_order_table")
public class UnpaidOrder extends Order {
    public UnpaidOrder(String clientName, String phoneNumber, String address, String date, String time, int price, boolean paid){
        super(clientName, phoneNumber, address,  date,  time, price, true, paid);
    }
}
