package com.example.androidapp.orderdata;



import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.androidapp.orderdata.OrderEntity;
import com.example.androidapp.orderdata.OrderDao;

@Database(entities = {OrderEntity.class}, version = 1)
public abstract class OrderDatabase extends RoomDatabase {
    private static com.example.androidapp.orderdata.OrderDatabase instance;
    public abstract OrderDao orderDao();
    public static synchronized com.example.androidapp.orderdata.OrderDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    com.example.androidapp.orderdata.OrderDatabase.class, "order_database").fallbackToDestructiveMigration().addCallback(roomCallback).build();

        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new com.example.androidapp.orderdata.OrderDatabase.PopulateDbAsyncTask(instance).execute();
        }
    };
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private OrderDao orderDao;

        private PopulateDbAsyncTask(com.example.androidapp.orderdata.OrderDatabase db){
            orderDao = db.orderDao();
        }
        @Override
        protected Void doInBackground(Void... voids){

            return null;
        }
    }
}

