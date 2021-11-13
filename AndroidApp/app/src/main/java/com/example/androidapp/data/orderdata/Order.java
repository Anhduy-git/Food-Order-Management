package com.example.androidapp.data.orderdata;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.androidapp.data.clientdata.Client;

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

    //Constructor
    public Order(Client client, String date, String time,
                 int price, boolean ship, boolean paid) {
        this.client = client;
        this.date = date;
        this.time = time;
        this.price = price;
        this.ship = ship;
        this.paid = paid;
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
}
