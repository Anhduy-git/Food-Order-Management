package com.example.androidapp.data.clientdata;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "client_table")
public class Client {
    //Attribute
    @PrimaryKey(autoGenerate = true)
    private int clientId;

    private String clientName;
    private String phoneNumber;
    private String address;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public Client(String clientName, String phoneNumber, String address, byte[] image) {
        this.clientName = clientName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.image = image;
    }


    public int getClientId() {
        return clientId;
    }
    public void setClientId(int id) {
        this.clientId = id;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
