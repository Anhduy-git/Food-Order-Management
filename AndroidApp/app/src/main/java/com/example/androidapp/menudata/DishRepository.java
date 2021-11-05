package com.example.androidapp.menudata;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.androidapp.AppDatabase;

import java.util.List;

public class DishRepository {
    private DishDao dishDao;
    private LiveData<List<Dish>> allDishes;

    public DishRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        dishDao = database.dishDao();
        allDishes = dishDao.getAllDishes();
    }

    public void insertDish(Dish dish) {
        new InsertDishAsyncTask(dishDao).execute(dish);
    }

    public void updateDish(Dish dish) {
        new UpdateDishAsyncTask(dishDao).execute(dish);
    }

    public void deleteDish(Dish dish) {
        new DeleteDishAsyncTask(dishDao).execute(dish);
    }

    public void deleteAllDishes() {
        new DeleteAllDishesAsyncTask(dishDao).execute();
    }

    public LiveData<List<Dish>> getAllDishes() {
        return allDishes;
    }

    private static class InsertDishAsyncTask extends AsyncTask<Dish, Void, Void> {
        private DishDao dishDao;

        private InsertDishAsyncTask(DishDao dishDao) {
            this.dishDao = dishDao;
        }

        @Override
        protected Void doInBackground(Dish... dishEntities) {
            dishDao.insertDish(dishEntities[0]);
            return null;
        }
    }

    private static class UpdateDishAsyncTask extends AsyncTask<Dish, Void, Void> {
        private DishDao dishDao;

        private UpdateDishAsyncTask(DishDao dishDao) {
            this.dishDao = dishDao;
        }

        @Override
        protected Void doInBackground(Dish... dishEntities) {
            dishDao.updateDish(dishEntities[0]);
            return null;
        }
    }

    private static class DeleteDishAsyncTask extends AsyncTask<Dish, Void, Void> {
        private DishDao dishDao;

        private DeleteDishAsyncTask(DishDao dishDao) {
            this.dishDao = dishDao;
        }

        @Override
        protected Void doInBackground(Dish... dishEntities) {
            dishDao.deleteDish(dishEntities[0]);
            return null;
        }
    }

    private static class DeleteAllDishesAsyncTask extends AsyncTask<Void, Void, Void> {
        private DishDao dishDao;

        private DeleteAllDishesAsyncTask(DishDao dishDao) {
            this.dishDao = dishDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dishDao.deleteAllDishes();
            return null;
        }
    }
}
