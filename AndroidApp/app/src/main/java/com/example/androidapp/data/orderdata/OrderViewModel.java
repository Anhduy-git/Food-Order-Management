package com.example.androidapp.data.orderdata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {
    private OrderRepository repository;
    private LiveData<List<Order>> allOrder;

    public OrderViewModel(@NonNull Application application){
        super(application);
        repository = new OrderRepository(application);
        allOrder = repository.getAllOrder();
    }
    public void insert(Order order){
        repository.insert(order);
    }
    public void update(Order order){
        repository.update(order);
    }
    public void delete(Order order){
        repository.delete(order);
    }
    public LiveData<List<Order>> getAllOrder(){
        return allOrder;
    }
    public void deleteAllOrder() {
        repository.deleteAllOrder();
    }

}