package com.example.androidapp.data.unpaiddata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import java.util.List;

public class UnpaidOrderViewModel extends AndroidViewModel {
    private UnpaidOrderRepository repository;
    private LiveData<List<UnpaidOrder>> allUnpaidOrder;

    public UnpaidOrderViewModel(@NonNull Application application){
        super(application);
        repository = new UnpaidOrderRepository(application);
        allUnpaidOrder = repository.getAllUnpaidOrder();
    }
    public void insert(UnpaidOrder unpaidOrder){
        repository.insert(unpaidOrder);
    }
    public void update(UnpaidOrder unpaidOrder){
        repository.update(unpaidOrder);
    }
    public void delete(UnpaidOrder unpaidOrder){
        repository.delete(unpaidOrder);
    }
    public LiveData<List<UnpaidOrder>> getAllUnpaidOrder(){
        return allUnpaidOrder;
    }
}