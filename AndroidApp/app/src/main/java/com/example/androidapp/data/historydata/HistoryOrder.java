package com.example.androidapp.data.historydata;

import androidx.room.Entity;

import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.menudata.Dish;
import com.example.androidapp.data.orderdata.Order;

import java.util.List;

@Entity(tableName = "history_table")
public class HistoryOrder extends Order {
    public HistoryOrder(Client client, String date, String time, int price, boolean ship, boolean paid, List<Dish> orderListDish){
        super(client,  date,  time, price, ship, paid, orderListDish);
    }
}
