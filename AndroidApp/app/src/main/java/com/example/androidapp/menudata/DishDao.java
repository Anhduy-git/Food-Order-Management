package com.example.androidapp.menudata;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DishDao {

    @Query("SELECT * FROM dish_table")
    LiveData<List<Dish>> getAllDishes();

    @Insert
    void insertDish(Dish dish);

    @Update
    void updateDish(Dish dish);

    @Delete
    void deleteDish(Dish dish);

    @Query("DELETE FROM dish_table")
    void deleteAllDishes();
}
