package com.example.flatfridge.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FoodItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFoodItem(foodItem: FoodItem)

    @Query("SELECT * FROM FoodItem ORDER BY expiryDate asc")
    fun getAllFoodItems(): LiveData<List<FoodItem>>

    @Delete
    suspend fun deleteFoodItem(foodItem: FoodItem)

}