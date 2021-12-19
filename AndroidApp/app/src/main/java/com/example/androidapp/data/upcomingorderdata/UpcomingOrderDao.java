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

    @Query("SELECT * FROM upcoming_order_table order by datetime(substr(date, 7, 4) || '-' || substr(date, 4, 2) || '-' || substr(date, 1, 2) || ' ' || time || ':' || '00') DESC")
    LiveData<List<UpcomingOrder>> getAllUpcomingOrder();

    @Query("SELECT * FROM upcoming_order_table WHERE date = :tomorrow")
    List<UpcomingOrder> getNumOrderTomorrow(String tomorrow);

}