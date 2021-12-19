package com.example.androidapp.data.historydata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.androidapp.data.orderdata.Order;

import java.util.List;

public class HistoryOrderViewModel extends AndroidViewModel {
    private HistoryOrderRepository repository;
    private LiveData<List<HistoryOrder>> allHistoryOrder;
    private LiveData<List<HistoryOrder>> allHistorySuccessOrder;
    private LiveData<List<HistoryOrder>> allHistoryCancelOrder;

    public HistoryOrderViewModel(@NonNull Application application){
        super(application);
        repository = new HistoryOrderRepository(application);
        allHistoryOrder = repository.getAllHistoryOrder();
        allHistorySuccessOrder = repository.getAllHistorySuccessOrder();
        allHistoryCancelOrder = repository.getAllHistoryCancelOrder();
    }
    public void insert(HistoryOrder historyOrder){
        repository.insert(historyOrder);
    }
    public void delete(HistoryOrder historyOrder){
        repository.delete(historyOrder);
    }
    public LiveData<List<HistoryOrder>> getAllHistoryOrder(){
        return allHistoryOrder;
    }
    public LiveData<List<HistoryOrder>> getAllHistorySuccessOrder(){
        return allHistorySuccessOrder;
    }
    public LiveData<List<HistoryOrder>> getAllHistoryCancelOrder(){
        return allHistoryCancelOrder;
    }

}
