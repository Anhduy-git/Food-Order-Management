package com.example.androidapp.orderdata;

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
    void insert(OrderEntity orderEntity);

    @Update
    void update(OrderEntity orderEntity);

    @Delete
    void delete(OrderEntity orderEntity);

    @Query("SELECT * FROM order_table ORDER BY date ASC")
    LiveData<List<OrderEntity>> getAllOrder();

}
