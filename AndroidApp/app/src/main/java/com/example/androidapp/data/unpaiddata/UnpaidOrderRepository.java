package com.example.androidapp.data.unpaiddata;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.androidapp.data.AppDatabase;

import java.util.List;

public class UnpaidOrderRepository {
    private UnpaidOrderDao unpaidOrderDao;
    private LiveData<List<UnpaidOrder>> allUnpaidOrder;
    public UnpaidOrderRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        unpaidOrderDao = database.unpaidOrderDao();
        allUnpaidOrder = unpaidOrderDao.getAllUnpaidOrder();
    }
    public void insert(UnpaidOrder unpaidOrder){
        new UnpaidOrderRepository.InsertNoteAsyncTask(unpaidOrderDao).execute(unpaidOrder);
    }
    public void update(UnpaidOrder unpaidOrder){
        new UnpaidOrderRepository.UpdateNoteAsyncTask(unpaidOrderDao).execute(unpaidOrder);
    }
    public void delete(UnpaidOrder unpaidOrder){new UnpaidOrderRepository.DeleteNoteAsyncTask(unpaidOrderDao).execute(unpaidOrder);
    }

    public LiveData<List<UnpaidOrder>> getAllUnpaidOrder(){
        return allUnpaidOrder;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<UnpaidOrder, Void, Void> {
        private UnpaidOrderDao unpaidOrderDao;
        private InsertNoteAsyncTask(UnpaidOrderDao unpaidOrderDao){
            this.unpaidOrderDao = unpaidOrderDao;
        }
        @Override
        protected Void doInBackground(UnpaidOrder... unpaidOrders){
            unpaidOrderDao.insert(unpaidOrders[0]);
            return null;
        }
    }
    private static class UpdateNoteAsyncTask extends AsyncTask<UnpaidOrder, Void, Void>{
        private UnpaidOrderDao unpaidOrderDao;
        private UpdateNoteAsyncTask(UnpaidOrderDao unpaidOrderDao){
            this.unpaidOrderDao = unpaidOrderDao;
        }
        @Override
        protected Void doInBackground(UnpaidOrder... unpaidOrders){
            unpaidOrderDao.update(unpaidOrders[0]);
            return null;
        }
    }
    private static class DeleteNoteAsyncTask extends AsyncTask<UnpaidOrder, Void, Void>{
        private UnpaidOrderDao unpaidOrderDao;
        private DeleteNoteAsyncTask(UnpaidOrderDao unpaidOrderDao){
            this.unpaidOrderDao = unpaidOrderDao;
        }
        @Override
        protected Void doInBackground(UnpaidOrder... unpaidOrders){
            unpaidOrderDao.delete(unpaidOrders[0]);
            return null;
        }
    }
}
