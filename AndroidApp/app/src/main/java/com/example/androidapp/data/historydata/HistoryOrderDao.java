package com.example.androidapp.data.historydata;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.androidapp.data.orderdata.Order;

import java.util.List;

@Dao
public interface HistoryOrderDao {
    @Insert
    void insert(HistoryOrder historyOrder);

    @Query("SELECT * FROM history_table ORDER BY clientName ASC")
    LiveData<List<HistoryOrder>> getAllHistoryOrder();
}
