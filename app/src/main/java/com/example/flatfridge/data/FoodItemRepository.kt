package com.example.flatfridge.data

import androidx.lifecycle.LiveData

class FoodItemRepository(private val foodItemDao: FoodItemDao) {
    val readAllData: LiveData<List<FoodItem>> = foodItemDao.getAllFoodItems()
    suspend fun addFoodItem(foodItem: FoodItem) = foodItemDao.addFoodItem(foodItem)
    suspend fun deleteFoodItem(foodItem: FoodItem) = foodItemDao.deleteFoodItem(foodItem )

}