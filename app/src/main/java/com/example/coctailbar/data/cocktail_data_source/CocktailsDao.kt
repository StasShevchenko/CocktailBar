package com.example.coctailbar.data.cocktail_data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coctailbar.data.cocktail_data_source.model.Cocktail

@Dao
interface CocktailsDao {
    @Query("SELECT * FROM Cocktail WHERE cocktailId = :cocktailId")
    suspend fun getCocktailById(cocktailId: Long): List<Cocktail>

    @Query("SELECT * FROM Cocktail")
    suspend fun getAllCocktails(): List<Cocktail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCocktail(cocktail: Cocktail)

    @Delete
    fun delete(cocktail: Cocktail)
}