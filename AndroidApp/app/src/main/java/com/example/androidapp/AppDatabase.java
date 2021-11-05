package com.example.androidapp;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.androidapp.menudata.Dish;
import com.example.androidapp.menudata.DishDao;

import com.example.androidapp.orderdata.Order;
import com.example.androidapp.orderdata.OrderDao;


//This is the app's main database, don't need to create another one
//Add more entities (tables) to database by listing them inside entities = {...}
@Database(entities = {Dish.class, Order.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "app_database.db";
    private static AppDatabase instance;

    //Add the entities' Dao here
    public abstract DishDao dishDao();
    public abstract OrderDao orderDao();


    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    //.allowMainThreadQueries()
                    //.addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    /*
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

     */

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private DishDao dishDao;
        private OrderDao orderDao;

        private PopulateDbAsyncTask(AppDatabase db) {
            dishDao = db.dishDao();
            orderDao = db.orderDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            //dishDao.insertDish(new Dish("Name", 1));
            return null;
        }

    }
}
