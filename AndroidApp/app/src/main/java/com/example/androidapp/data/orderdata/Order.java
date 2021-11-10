package com.example.androidapp.data.orderdata;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_table")
public class Order {

    //Attribute
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String clientName;

    private String phoneNumber;

    private String address;

    private String date;

    private String time;

    private int price;

    private boolean ship;

    private boolean paid;

    //Constructor
    public Order(String clientName, String phoneNumber, String address,
                 String date, String time, int price, boolean ship, boolean paid) {
        this.clientName = clientName;
        this.phoneNumber = phoneNumber;
        this.address = address;
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

    public String getClientName() {
        return clientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
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
