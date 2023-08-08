package com.example.coctailbar.data.cocktail_data_source.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cocktail(
    @PrimaryKey(autoGenerate = true)
    val cocktailId: Long = 0,
    val cocktailName: String,
    val cocktailDescription: String? = null,
    val cocktailRecipe: String? = null,
    val cocktailIngredients: List<String>? = null
)
