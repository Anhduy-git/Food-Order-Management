package com.example.androidapp.data.unpaiddata;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface UnpaidOrderDao {
    @Insert
    void insert(UnpaidOrder unpaidOrder);

    @Update
    void update(UnpaidOrder unpaidOrder);

    @Delete
    void delete(UnpaidOrder unpaidOrder);

    @Query("SELECT * FROM unpaid_order_table order by datetime(substr(date, 7, 4) || '-' || substr(date, 4, 2) || '-' || substr(date, 1, 2) || ' ' || time || ':' || '00') DESC")
    LiveData<List<UnpaidOrder>> getAllUnpaidOrder();
}
