package com.example.flatfridge.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FoodItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FoodItemDatabase: RoomDatabase() {

    abstract fun foodItemDao(): FoodItemDao

    companion object{
        @Volatile
        private var INSTANCE: FoodItemDatabase? = null

        fun getDatabase(context: Context): FoodItemDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodItemDatabase::class.java,
                    "food_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}