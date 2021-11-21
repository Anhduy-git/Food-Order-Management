package com.example.androidapp.data.historydata;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.androidapp.data.AppDatabase;
import com.example.androidapp.data.orderdata.Order;
import com.example.androidapp.data.orderdata.OrderDao;
import com.example.androidapp.data.orderdata.OrderRepository;

import java.util.List;

public class HistoryOrderRepository {

    private HistoryOrderDao historyOrderDao;
    private LiveData<List<HistoryOrder>> allHistoryOrder;
    public HistoryOrderRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        historyOrderDao = database.historyOrderDao();
        allHistoryOrder = historyOrderDao.getAllHistoryOrder();
    }
    public void insert(HistoryOrder historyOrder){
        new HistoryOrderRepository.InsertNoteAsyncTask(historyOrderDao).execute(historyOrder);
    }


    public LiveData<List<HistoryOrder>> getAllHistoryOrder(){
        return allHistoryOrder;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<HistoryOrder, Void, Void> {
        private HistoryOrderDao historyOrderDao;
        private InsertNoteAsyncTask(HistoryOrderDao historyOrderDao){
            this.historyOrderDao = historyOrderDao;
        }
        @Override
        protected Void doInBackground(HistoryOrder... historyOrders){
            historyOrderDao.insert(historyOrders[0]);
            return null;
        }
    }

}
