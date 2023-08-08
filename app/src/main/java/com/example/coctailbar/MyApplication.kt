package com.example.coctailbar

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.coctailbar.data.cocktail_data_source.CocktailsDatabase

class MyApplication : Application() {

    lateinit var db: CocktailsDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            this,
            CocktailsDatabase::class.java,
            CocktailsDatabase.DATABASE_NAME
        ).build()
    }
}