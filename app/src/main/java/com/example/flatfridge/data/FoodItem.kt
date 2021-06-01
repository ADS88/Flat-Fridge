package com.example.flatfridge.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val foodPhoto : Bitmap,
    val foodName: String,
    val expiryDate: Long)
