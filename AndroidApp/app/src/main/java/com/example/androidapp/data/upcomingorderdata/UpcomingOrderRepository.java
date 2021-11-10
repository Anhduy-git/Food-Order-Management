package com.example.androidapp.data.upcomingorderdata;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.androidapp.data.AppDatabase;


import java.util.List;

public class UpcomingOrderRepository {
    private UpcomingOrderDao upcomingOrderDao;
    private LiveData<List<UpcomingOrder>> allUpcomingOrder;
    public UpcomingOrderRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        upcomingOrderDao = database.upcomingOrderDao();
        allUpcomingOrder = upcomingOrderDao.getAllUpcomingOrder();
    }
    public void insert(UpcomingOrder upcomingOrder){
        new UpcomingOrderRepository.InsertNoteAsyncTask(upcomingOrderDao).execute(upcomingOrder);
    }
    public void update(UpcomingOrder upcomingOrder){
        new UpcomingOrderRepository.UpdateNoteAsyncTask(upcomingOrderDao).execute(upcomingOrder);
    }
    public void delete(UpcomingOrder upcomingOrder){new UpcomingOrderRepository.DeleteNoteAsyncTask(upcomingOrderDao).execute(upcomingOrder);
    }

    public LiveData<List<UpcomingOrder>> getAllUpcomingOrder(){
        return allUpcomingOrder;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<UpcomingOrder, Void, Void> {
        private UpcomingOrderDao upcomingOrderDao;
        private InsertNoteAsyncTask(UpcomingOrderDao upcomingOrderDao){
            this.upcomingOrderDao = upcomingOrderDao;
        }
        @Override
        protected Void doInBackground(UpcomingOrder... upcomingOrders){
            upcomingOrderDao.insert(upcomingOrders[0]);
            return null;
        }
    }
    private static class UpdateNoteAsyncTask extends AsyncTask<UpcomingOrder, Void, Void>{
        private UpcomingOrderDao upcomingOrderDao;
        private UpdateNoteAsyncTask(UpcomingOrderDao upcomingOrderDao){
            this.upcomingOrderDao = upcomingOrderDao;
        }
        @Override
        protected Void doInBackground(UpcomingOrder... upcomingOrders){
            upcomingOrderDao.update(upcomingOrders[0]);
            return null;
        }
    }
    private static class DeleteNoteAsyncTask extends AsyncTask<UpcomingOrder, Void, Void>{
        private UpcomingOrderDao upcomingOrderDao;
        private DeleteNoteAsyncTask(UpcomingOrderDao upcomingOrderDao){
            this.upcomingOrderDao = upcomingOrderDao;
        }
        @Override
        protected Void doInBackground(UpcomingOrder... upcomingOrders){
            upcomingOrderDao.delete(upcomingOrders[0]);
            return null;
        }
    }
}
