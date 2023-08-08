package com.example.coctailbar.data.cocktail_data_source

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromString(stringListString: String): List<String> {
        if(stringListString.isBlank()) return emptyList()
        return stringListString.split(",").map { it }
    }

    @TypeConverter
    fun toString(stringList: List<String>): String {
        return stringList.joinToString(separator = ",")
    }

}