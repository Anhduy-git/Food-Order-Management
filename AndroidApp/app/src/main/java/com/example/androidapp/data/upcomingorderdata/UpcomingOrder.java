package com.example.androidapp.data.upcomingorderdata;

import androidx.room.Entity;

import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.menudata.Dish;
import com.example.androidapp.data.orderdata.Order;

import java.util.List;

@Entity(tableName = "upcoming_order_table")
public class UpcomingOrder extends Order {
    public UpcomingOrder(Client client, String date, String time, int price, boolean paid, List<Dish> orderListDish){
        super(client, date,  time, price, false, paid, orderListDish);
    }
}
