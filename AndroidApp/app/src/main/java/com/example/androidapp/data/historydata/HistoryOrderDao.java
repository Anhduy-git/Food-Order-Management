package com.example.androidapp.data.historydata;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.orderdata.Order;

import java.util.List;

@Dao
public interface HistoryOrderDao {
    @Insert
    void insert(HistoryOrder historyOrder);

    @Delete
    void delete(HistoryOrder historyOrder);

    @Query("SELECT * FROM history_table order by datetime(substr(date, 7, 4) || '-' || substr(date, 4, 2) || '-' || substr(date, 1, 2) || ' ' || time || ':' || '00') DESC")
    LiveData<List<HistoryOrder>> getAllHistoryOrder();

    @Query("SELECT * FROM history_table WHERE ship = 1 order by datetime(substr(date, 7, 4) || '-' || substr(date, 4, 2) || '-' || substr(date, 1, 2) || ' ' || time || ':' || '00') DESC")
    LiveData<List<HistoryOrder>> getAllHistorySuccessOrder();

    @Query("SELECT * FROM history_table WHERE ship = 0 order by datetime(substr(date, 7, 4) || '-' || substr(date, 4, 2) || '-' || substr(date, 1, 2) || ' ' || time || ':' || '00') DESC")
    LiveData<List<HistoryOrder>> getAllHistoryCancelOrder();
}
