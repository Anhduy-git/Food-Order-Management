package com.example.androidapp.data.unpaiddata;

import androidx.room.Entity;

import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.orderdata.Order;

@Entity(tableName = "unpaid_order_table")
public class UnpaidOrder extends Order {
    public UnpaidOrder(Client client, String date, String time, int price, boolean paid){
        super(client,  date,  time, price, true, paid);
    }
}
