package com.example.coctailbar.presentation.cocktails_list

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
import kotlinx.coroutines.launch

class CocktailsListViewModel(
    private val cocktailsDao: CocktailsDao,
) : ViewModel() {

    private val _cocktailsList: MutableStateFlow<List<Cocktail>> = MutableStateFlow(emptyList())
    val cocktailsList: StateFlow<List<Cocktail>> = _cocktailsList
    init {
        viewModelScope.launch(Dispatchers.IO) {
            cocktailsDao.getAllCocktails().collectLatest {cocktails ->
                _cocktailsList.value = cocktails
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val cocktailsDao = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication).db.cocktailsDao
                CocktailsListViewModel(
                    cocktailsDao = cocktailsDao,
                )
            }
        }
    }
}