package com.example.androidapp.orderdata;

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

    //Constructor
    public Order(String clientName, String phoneNumber, String address, String date, String time, int price) {
        this.clientName = clientName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.date = date;
        this.time = time;
        this.price = price;
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

    //Setter
    public void setId(int id) {
        this.id = id;
    }
}
