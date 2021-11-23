package com.example.androidapp.data.orderdata;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidapp.data.menudata.Dish;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insert(Order order);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);

    @Query("SELECT * FROM order_table ORDER BY clientName ASC")
    LiveData<List<Order>> getAllOrder();

    @Query("DELETE FROM order_table")
    void deleteAllOrder();

    /*
    @Query("SELECT dish_list FROM order_table")
    LiveData<List<Dish>> getOrderDishList();
     */

}
