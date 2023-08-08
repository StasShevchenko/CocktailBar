package com.example.coctailbar.presentation.cocktail_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.coctailbar.MyApplication
import com.example.coctailbar.data.cocktail_data_source.CocktailsDao
import com.example.coctailbar.data.cocktail_data_source.model.Cocktail
import com.example.coctailbar.presentation.add_edit_cocktail.AddEditCocktailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CocktailDetailViewModel(
    private val cocktailsDao: CocktailsDao,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var currentCocktailId = -1L

    private val _currentCocktail: MutableStateFlow<Cocktail?> = MutableStateFlow(null)
    val currentCocktail: StateFlow<Cocktail?> = _currentCocktail

    init {
        currentCocktailId = savedStateHandle.get<Long>("cocktailId") ?: -1L
        if (currentCocktailId != -1L) {
            viewModelScope.launch(Dispatchers.IO) {
                cocktailsDao.getCocktailById(currentCocktailId).collectLatest { cocktail ->
                    _currentCocktail.value = cocktail
                }
            }
        }
    }
    fun deleteCocktail() {
        viewModelScope.launch(Dispatchers.IO) {
            currentCocktail.value?.let { cocktailsDao.delete(it) }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val cocktailsDao = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication).db.cocktailsDao
                CocktailDetailViewModel(
                    cocktailsDao = cocktailsDao,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }
}