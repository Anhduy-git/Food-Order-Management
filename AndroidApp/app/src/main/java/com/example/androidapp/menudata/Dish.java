package com.example.androidapp.menudata;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dish_table")
public class Dish {
    @PrimaryKey(autoGenerate = true)
    private int dishID;

    private String name;
    private int price;
    private int quantity;

    public Dish(String name, int price) {
        this.name = name;
        this.price = price;
    }

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
}


