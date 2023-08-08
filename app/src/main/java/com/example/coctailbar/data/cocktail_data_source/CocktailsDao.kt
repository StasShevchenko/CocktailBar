package com.example.coctailbar.data.cocktail_data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coctailbar.data.cocktail_data_source.model.Cocktail
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailsDao {
    @Query("SELECT * FROM Cocktail WHERE cocktailId = :cocktailId")
    fun getCocktailById(cocktailId: Long): Flow<Cocktail>

    @Query("SELECT * FROM Cocktail")
     fun getAllCocktails(): Flow<List<Cocktail>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCocktail(cocktail: Cocktail)

    @Delete
    fun delete(cocktail: Cocktail)
}