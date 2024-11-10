package Trnity.ITP.Recetta.ViewModel

import Trnity.ITP.Recetta.Model.entities.Ingredient
import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.Model.repositories.IngredientRepository
import Trnity.ITP.Recetta.Model.repositories.RecipeRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel@Inject constructor(
    private val repository: RecipeRepository // Injects IngredientRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val ingredients: StateFlow<List<Recipe>> = _recipes


    fun fetchAllIngredients() {
        viewModelScope.launch {
            _recipes.value = repository.getAllRecipe()
        }
    }
}