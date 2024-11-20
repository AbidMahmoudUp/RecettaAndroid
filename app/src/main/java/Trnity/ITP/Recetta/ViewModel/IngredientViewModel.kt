package Trnity.ITP.Recetta.ViewModel

import Trnity.ITP.Recetta.Model.entities.Ingredient
import Trnity.ITP.Recetta.Model.repositories.IngredientRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientViewModel @Inject constructor(
    private val repository: IngredientRepository
) : ViewModel() {

    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredients: StateFlow<List<Ingredient>> = _ingredients
    private val _isLoading  = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Automatically fetch ingredients when ViewModel is created
    init {
        fetchAllIngredients()
    }
    fun loadStuff()
    {
        viewModelScope.launch {
            _isLoading.value = true
            delay(3000)
            _isLoading.value = false
        }

    }
    fun fetchAllIngredients() {
        viewModelScope.launch {
            try {
                // Fetch ingredients from the repository
                _isLoading.value = true
                delay(1000)
                _ingredients.value = repository.getAllIngredients()


                _isLoading.value = false
                println("Fetched ingredients: ${_ingredients.value}")
            } catch (e: Exception) {
                e.printStackTrace()
                // Log or handle errors here
            }
        }
    }
}
