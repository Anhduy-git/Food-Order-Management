package com.example.androidapp.orderdata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {
    private OrderRepository repository;
    private LiveData<List<OrderEntity>> allOrder;

    public OrderViewModel(@NonNull Application application){
        super(application);
        repository = new OrderRepository(application);
        allOrder = repository.getAllOrder();
    }
    public void insert(OrderEntity orderEntity){
        repository.insert(orderEntity);
    }
    public void update(OrderEntity orderEntity){
        repository.update(orderEntity);
    }
    public void delete(OrderEntity orderEntity){
        repository.delete(orderEntity);
    }
    public LiveData<List<OrderEntity>> getAllOrder(){
        return allOrder;
    }
}