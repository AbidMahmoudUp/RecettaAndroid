package Trnity.ITP.Recetta.ViewModel

import Trnity.ITP.Recetta.Model.entities.Ingredient
import Trnity.ITP.Recetta.Model.entities.IngredientRecipe
import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.Model.repositories.IngredientRepository
import Trnity.ITP.Recetta.Model.repositories.RecipeRepository
import android.util.Log
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
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val ingredientRepository: IngredientRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _recipe = MutableStateFlow(Recipe())
    val recipe: StateFlow<Recipe> = _recipe

    // Fetch a single recipe by ID
    suspend fun fetchRecipe(id: String) {
        try {
            val recipe = repository.getRecipe(id)
            println("***********************Recipe from repo-*-------------------")
            println(recipe)

            val detailedIngredients = recipe.ingredients.mapNotNull { ingredientRecipe ->
                ingredientRecipe.ingredient?.let {
                    IngredientRecipe(it, ingredientRecipe.qte)
                }
            }

            _recipe.value = recipe.copy(ingredients = detailedIngredients)
            Log.d("RecipeViewModel", "Fetched Recipe: $recipe")
        } catch (e: Exception) {
            Log.e("RecipeViewModel", "Error fetching recipe: ${e.message}")
        }
    }

    // Fetch all recipes
    fun fetchAllRecipes() {
        viewModelScope.launch {
            try {
                _recipes.value = repository.getAllRecipe()
                Log.d("RecipeViewModel", "Fetched Recipes: ${_recipes.value}")
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Error fetching all recipes: ${e.message}")
            }
        }
    }
}
