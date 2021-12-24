package com.example.androidapp.data.orderdata;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insert(Order order);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);

    @Query("SELECT * FROM order_table order by datetime(substr(date, 7, 4) || '-' || substr(date, 4, 2) || '-' || substr(date, 1, 2) || ' ' || time || ':' || '00') DESC")
    LiveData<List<Order>> getAllOrder();

    @Query("DELETE FROM order_table")
    void deleteAllOrder();

}
