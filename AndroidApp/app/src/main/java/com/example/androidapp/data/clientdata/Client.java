package com.example.androidapp.data.clientdata;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "client_table")
public class Client {
    //Attribute
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String clientName;

    private String phoneNumber;

    private String address;

    public Client(String clientName, String phoneNumber, String address) {
        this.clientName = clientName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

}
