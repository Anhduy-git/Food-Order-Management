package com.example.androidapp.data.upcomingorderdata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UpcomingOrderViewModel extends AndroidViewModel {
    private UpcomingOrderRepository repository;
    private LiveData<List<UpcomingOrder>> allUpcomingOrder;

    public UpcomingOrderViewModel(@NonNull Application application){
        super(application);
        repository = new UpcomingOrderRepository(application);
        allUpcomingOrder = repository.getAllUpcomingOrder();
    }
    public void insert(UpcomingOrder upcomingOrder){
        repository.insert(upcomingOrder);
    }
    public void update(UpcomingOrder upcomingOrder){
        repository.update(upcomingOrder);
    }
    public void delete(UpcomingOrder upcomingOrder){
        repository.delete(upcomingOrder);
    }
    public LiveData<List<UpcomingOrder>> getAllUpcomingOrder(){
        return allUpcomingOrder;
    }
}