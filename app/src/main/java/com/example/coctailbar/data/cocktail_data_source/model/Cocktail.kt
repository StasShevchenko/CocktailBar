package com.example.coctailbar.data.cocktail_data_source.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cocktail(
    @PrimaryKey(autoGenerate = true)
    val cocktailId: Long,
    val cocktailName: String,
    val cocktailDescription: String?,
    val cocktailRecipe: String?,
    val cocktailIngredients: List<String>?
)
