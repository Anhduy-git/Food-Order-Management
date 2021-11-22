package com.example.androidapp.data.orderdata;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.menudata.Dish;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "order_table")
public class Order {

    //Attribute
    @PrimaryKey(autoGenerate = true)
    private int id;

    @Embedded
    private Client client;

    private String date;

    private String time;

    private int price;

    private boolean ship;

    private boolean paid;

    @ColumnInfo(name = "dish_list")
    private List<Dish> orderListDish;

    //Constructor
    public Order(Client client, String date, String time,
                 int price, boolean ship, boolean paid, List<Dish> orderListDish) {
        this.client = client;
        this.date = date;
        this.time = time;
        this.price = price;
        this.ship = ship;
        this.paid = paid;
        this.orderListDish = orderListDish;
    }

    //Getter
    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getPrice() {
        return price;
    }

    public boolean getShip() {
        return ship;
    }

    public boolean getPaid() {
        return paid;
    }

    public List<Dish> getOrderListDish() {
        return orderListDish;
    }

    //Setter
    public void setId(int id) {
        this.id = id;
    }
    public void setShip(boolean ship) {
        this.ship = ship;
    }
    public void setPaid(boolean paid) {
        this.ship = paid;
    }
    public void setOrderListDish(List<Dish> orderListDish) {
        this.orderListDish = orderListDish;
    }
}
