package com.example.androidapp.data.upcomingorderdata;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UpcomingOrderDao {
    @Insert
    void insert(UpcomingOrder upcomingOrder);

    @Update
    void update(UpcomingOrder upcomingOrder);

    @Delete
    void delete(UpcomingOrder upcomingOrder);

    @Query("SELECT * FROM upcoming_order_table ORDER BY date ASC")
    LiveData<List<UpcomingOrder>> getAllUpcomingOrder();

    @Query("SELECT * FROM upcoming_order_table WHERE date = :tomorrow")
    List<UpcomingOrder> getNumOrderTomorrow(String tomorrow);

}