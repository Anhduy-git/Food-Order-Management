package com.example.androidapp.data.historydata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import java.util.List;

public class HistoryOrderViewModel extends AndroidViewModel {
    private HistoryOrderRepository repository;
    private LiveData<List<HistoryOrder>> allHistoryOrder;

    public HistoryOrderViewModel(@NonNull Application application){
        super(application);
        repository = new HistoryOrderRepository(application);
        allHistoryOrder = repository.getAllHistoryOrder();
    }
    public void insert(HistoryOrder historyOrder){
        repository.insert(historyOrder);
    }
    public LiveData<List<HistoryOrder>> getAllHistoryOrder(){
        return allHistoryOrder;
    }

}
