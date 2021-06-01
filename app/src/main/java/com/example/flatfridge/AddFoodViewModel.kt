package com.example.flatfridge

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import androidx.lifecycle.*
import com.example.flatfridge.data.FoodItem
import com.example.flatfridge.data.FoodItemDatabase
import com.example.flatfridge.data.FoodItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

private const val FILE_NAME = "photo.jpg"
private lateinit var photoFile: File

class AddFoodViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private var imageFile: File? = null

    private val _foodName = MutableLiveData<String>()
    private var expiryDate: Long? = null

    private val repository: FoodItemRepository

    init {
        val foodItemDao = FoodItemDatabase.getDatabase(application).foodItemDao()
        repository = FoodItemRepository(foodItemDao)
    }

    fun foodName(): LiveData<String> {
        return _foodName
    }

    fun setImageFile(file: File?){
        this.imageFile = file
    }

    fun getImageFile(): File? {
        return this.imageFile
    }

    fun resetFields(){
        imageFile = null
        _foodName.value = ""
    }

    fun getExpiryDate(): Long? {
        return expiryDate
    }

    fun setExpiryDate(date: Long?){
        this.expiryDate = date
    }

    fun addFoodItem(foodItem: FoodItem ){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFoodItem(foodItem)
        }

    }

}