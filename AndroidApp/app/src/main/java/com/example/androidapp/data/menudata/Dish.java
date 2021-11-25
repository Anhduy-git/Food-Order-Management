package com.example.androidapp.data.menudata;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "dish_table")
public class Dish implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int dishID;

    private String name;
    private int price;
    private int quantity;

    public Dish(String name, int price) {
        this.name = name;
        this.price = price;
        this.quantity = 0;
    }

    protected Dish(Parcel in) {
        dishID = in.readInt();
        name = in.readString();
        price = in.readInt();
        quantity = in.readInt();
    }

    public static final Creator<Dish> CREATOR = new Creator<Dish>() {
        @Override
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

    //Setters and Getters
    public int getDishID() {
        return dishID;
    }

    public void setDishID(int dishID) {
        this.dishID = dishID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(dishID);
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeInt(quantity);
    }
}


