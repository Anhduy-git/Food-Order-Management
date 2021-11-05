package com.example.androidapp.orderdata;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.androidapp.AppDatabase;


import java.util.List;

public class OrderRepository {
    private OrderDao orderDao;
    private LiveData<List<Order>> allOrder;
    public OrderRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        orderDao = database.orderDao();
        allOrder = orderDao.getAllOrder();
    }
    public void insert(Order order){
        new InsertNoteAsyncTask(orderDao).execute(order);
    }
    public void update(Order order){
        new UpdateNoteAsyncTask(orderDao).execute(order);
    }
    public void delete(Order order){new DeleteNoteAsyncTask(orderDao).execute(order);
    }

    public LiveData<List<Order>> getAllOrder(){
        return allOrder;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Order, Void, Void>{
        private OrderDao orderDao;
        private InsertNoteAsyncTask(OrderDao orderDao){
            this.orderDao = orderDao;
        }
        @Override
        protected Void doInBackground(Order... orderEntities){
            orderDao.insert(orderEntities[0]);
            return null;
        }
    }
    private static class UpdateNoteAsyncTask extends AsyncTask<Order, Void, Void>{
        private OrderDao orderDao;
        private UpdateNoteAsyncTask(OrderDao orderDao){
            this.orderDao = orderDao;
        }
        @Override
        protected Void doInBackground(Order... orderEntities){
            orderDao.update(orderEntities[0]);
            return null;
        }
    }
    private static class DeleteNoteAsyncTask extends AsyncTask<Order, Void, Void>{
        private OrderDao orderDao;
        private DeleteNoteAsyncTask(OrderDao orderDao){
            this.orderDao = orderDao;
        }
        @Override
        protected Void doInBackground(Order... orderEntities){
            orderDao.delete(orderEntities[0]);
            return null;
        }
    }

}
