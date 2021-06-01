package com.example.flatfridge.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converters {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val smolBitMap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 4, bitmap.height / 4, false)
        smolBitMap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        val bitmap =  BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return bitmap
    }
}