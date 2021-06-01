package com.example.flatfridge

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.flatfridge.data.FoodItem
import com.example.flatfridge.data.FoodItemDatabase
import com.example.flatfridge.data.FoodItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<FoodItem>>
    private val repository: FoodItemRepository

    init {
        val foodItemDao = FoodItemDatabase.getDatabase(application).foodItemDao()
        repository = FoodItemRepository(foodItemDao)
        readAllData = repository.readAllData
    }

    fun deleteFoodItem(foodItem: FoodItem){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteFoodItem(foodItem)
        }
    }
}