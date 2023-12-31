package com.example.coctailbar.presentation.add_edit_cocktail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.coctailbar.MyApplication
import com.example.coctailbar.data.cocktail_data_source.CocktailsDao
import com.example.coctailbar.data.cocktail_data_source.model.Cocktail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddEditCocktailViewModel(
    private val cocktailsDao: CocktailsDao,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var currentCocktailId = -1L
        private set
    private var cocktailName = ""
    private var description = ""
    private var recipe = ""

    private val _currentCocktail: MutableStateFlow<Cocktail?> = MutableStateFlow(null)
    val currentCocktail: StateFlow<Cocktail?> = _currentCocktail

    private val _cocktailIngredients: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val cocktailIngredients: StateFlow<List<String>> = _cocktailIngredients

    private val _cocktailNameErrorState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val cocktailNameErrorState: StateFlow<Boolean> = _cocktailNameErrorState

    init {
        currentCocktailId = savedStateHandle.get<Long>("cocktailId") ?: -1L
        if (currentCocktailId != -1L) {
            viewModelScope.launch(Dispatchers.IO) {
                _currentCocktail.value = cocktailsDao.getCocktailById(currentCocktailId).first()
                _cocktailIngredients.value = _currentCocktail.value!!.cocktailIngredients
            }
        }
    }
    fun setCocktailName(cocktailName: String) {
        this.cocktailName = cocktailName
        _cocktailNameErrorState.value = false
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun setRecipe(recipe: String) {
        this.recipe = recipe
    }

    fun addIngredient(ingredient: String) {
        val currentIngredientList = _cocktailIngredients.value.toMutableList()
        currentIngredientList.add(ingredient)
        _cocktailIngredients.value = currentIngredientList
    }

    fun removeIngredient(index: Int) {
        val currentIngredientList = _cocktailIngredients.value.toMutableList()
        currentIngredientList.removeAt(index)
        _cocktailIngredients.value = currentIngredientList
    }

    fun saveCocktail(): Boolean{
        if (cocktailName.isBlank()) {
            _cocktailNameErrorState.value = true
            return false
        }
        viewModelScope.launch(Dispatchers.IO) {
            cocktailsDao.insertCocktail(Cocktail(
                cocktailId = if(currentCocktailId != -1L) currentCocktailId else 0,
                cocktailName = cocktailName,
                cocktailDescription = description.ifBlank { null },
                cocktailRecipe = recipe.ifBlank { null },
                cocktailIngredients = cocktailIngredients.value
            ))
        }
        return true
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val cocktailsDao = (this[APPLICATION_KEY] as MyApplication).db.cocktailsDao
                AddEditCocktailViewModel(
                    cocktailsDao = cocktailsDao,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }
}