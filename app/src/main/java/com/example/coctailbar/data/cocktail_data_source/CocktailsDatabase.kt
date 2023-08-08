package com.example.coctailbar.data.cocktail_data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.coctailbar.data.cocktail_data_source.model.Cocktail

@Database(entities = [Cocktail::class], version = 1)
@TypeConverters(StringListConverter::class)
abstract class CocktailsDatabase : RoomDatabase() {

    abstract val cocktailsDao: CocktailsDao
    companion object{
        const val DATABASE_NAME = "cocktails_db"
    }
}