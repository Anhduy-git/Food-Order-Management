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

    @Query("SELECT * FROM unpaid_order_table ORDER BY date ASC")
    LiveData<List<UnpaidOrder>> getAllUnpaidOrder();
}
