package com.example.androidapp.orderdata;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.androidapp.orderdata.OrderEntity;
import com.example.androidapp.orderdata.OrderDao;
import com.example.androidapp.orderdata.OrderDatabase;

import java.util.List;

public class OrderRepository {
    private OrderDao orderDao;
    private LiveData<List<OrderEntity>> allOrder;
    public OrderRepository(Application application){
        OrderDatabase database = OrderDatabase.getInstance(application);
        orderDao = database.orderDao();
        allOrder = orderDao.getAllOrder();
    }
    public void insert(OrderEntity orderEntity){
        new InsertNoteAsyncTask(orderDao).execute(orderEntity);
    }
    public void update(OrderEntity orderEntity){
        new UpdateNoteAsyncTask(orderDao).execute(orderEntity);
    }
    public void delete(OrderEntity orderEntity){new DeleteNoteAsyncTask(orderDao).execute(orderEntity);
    }

    public LiveData<List<OrderEntity>> getAllOrder(){
        return allOrder;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<OrderEntity, Void, Void>{
        private OrderDao orderDao;
        private InsertNoteAsyncTask(OrderDao orderDao){
            this.orderDao = orderDao;
        }
        @Override
        protected Void doInBackground(OrderEntity... orderEntities){
            orderDao.insert(orderEntities[0]);
            return null;
        }
    }
    private static class UpdateNoteAsyncTask extends AsyncTask<OrderEntity, Void, Void>{
        private OrderDao orderDao;
        private UpdateNoteAsyncTask(OrderDao orderDao){
            this.orderDao = orderDao;
        }
        @Override
        protected Void doInBackground(OrderEntity... orderEntities){
            orderDao.update(orderEntities[0]);
            return null;
        }
    }
    private static class DeleteNoteAsyncTask extends AsyncTask<OrderEntity, Void, Void>{
        private OrderDao orderDao;
        private DeleteNoteAsyncTask(OrderDao orderDao){
            this.orderDao = orderDao;
        }
        @Override
        protected Void doInBackground(OrderEntity... orderEntities){
            orderDao.delete(orderEntities[0]);
            return null;
        }
    }

}
