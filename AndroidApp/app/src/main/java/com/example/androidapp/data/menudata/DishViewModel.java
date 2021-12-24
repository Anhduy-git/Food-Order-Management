package com.example.androidapp.data.menudata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DishViewModel extends AndroidViewModel {
    private DishRepository repository;
    private LiveData<List<Dish>> allDishes;


    public DishViewModel(@NonNull Application application) {
        super(application);
        repository = new DishRepository(application);
        allDishes = repository.getAllDishes();
    }

    public void insertDish(Dish dish) {
        repository.insertDish(dish);
    }

    public void updateDish(Dish dish) {
        repository.updateDish(dish);
    }

    public void deleteDish(Dish dish) {
        repository.deleteDish(dish);
    }

    public LiveData<List<Dish>> getAllDishes() {
        return allDishes;
    }
}
