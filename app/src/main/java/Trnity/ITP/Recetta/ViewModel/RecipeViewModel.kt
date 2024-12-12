package Trnity.ITP.Recetta.ViewModel

import Trnity.ITP.Recetta.Data.Local.RecipeEntity
import Trnity.ITP.Recetta.Data.Local.RecipeEntityRepository
import Trnity.ITP.Recetta.Model.entities.Ingredient
import Trnity.ITP.Recetta.Model.entities.IngredientRecipe
import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.Model.repositories.IngredientRepository
import Trnity.ITP.Recetta.Model.repositories.RecipeRepository
import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val RDBRepository : RecipeEntityRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes
    private val _generatedRecipes = MutableLiveData<List<Recipe>>()
    var generatedRecipes: LiveData<List<Recipe>> = _generatedRecipes
    private val _recipe = MutableStateFlow(Recipe())
    val recipe: StateFlow<Recipe> = _recipe
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    private val _favoriteRecipes = MutableStateFlow<List<RecipeEntity>>(emptyList())
    val favoriteRecipes: StateFlow<List<RecipeEntity>> = _favoriteRecipes
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite
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

    fun resetRecipes()
    {
        _generatedRecipes.value = emptyList()
        generatedRecipes = _generatedRecipes
    }
    @SuppressLint("SuspiciousIndentation")
    fun generateRecipes(ingredients: Set<IngredientRecipe>) {
        _isLoading.postValue(true)
        val requestContent = ingredients.toString()
        var gson = Gson()
        var jsonString = gson.toJson(ingredients)
        Log.d("Json Parser" , jsonString)
        val promptText = jsonString.toString() //+ secondPartOfRequest
        val requestBody = mapOf("ingredients" to promptText)
        Log.d("AI Debug", "Generated Request: $promptText")

        viewModelScope.launch(Dispatchers.IO) {
            try {

                val responseBody = repository.generateRecipe(requestBody)
                withContext(Dispatchers.Main) {
                    _generatedRecipes.value = responseBody.toList()
                    Log.d("AI Request", "Request: $requestBody")
                    Log.d("Generated Recipes Size", _generatedRecipes.value?.size.toString())
                         Log.d("AI Response", "Response: $responseBody")
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _generatedRecipes.postValue(emptyList())
                    _isLoading.postValue(false)
                }
                Log.e("AI Error", "Exception occurred: ${e.message}")
            }
        }
    }

    fun toggleFavoriteWithConfirmation(recipe: RecipeEntity) {
        val currentlyFavorite = _favoriteRecipes.value.contains(recipe)
        if (currentlyFavorite) {
            removeFavorite(recipe) // Remove if currently a favorite
        } else {
            addFavorite(recipe) // Add if not a favorite
        }
    }

    fun addFavorite(recipe: RecipeEntity) {
        viewModelScope.launch {
            RDBRepository.addFavorite(recipe)
            loadFavorites()

        }
    }

    fun removeFavorite(recipe: RecipeEntity) {
        viewModelScope.launch {
            RDBRepository.removeFavorite(recipe)
            loadFavorites()
        }
    }
    fun loadFavorites() {
        viewModelScope.launch {
            val updatedFavorites = RDBRepository.getFavorites()
            _favoriteRecipes.value = updatedFavorites
        }
    }

    fun removeFavorite(recipe: Recipe) {
        viewModelScope.launch {
            RDBRepository.removeFavorite(recipe.toEntity())
        }
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            val currentlyFavorite = RDBRepository.isFavorite(recipe.id)
            if (currentlyFavorite) {
                RDBRepository.removeFavorite(recipe.toEntity())
                _isFavorite.value = false
            } else {
                RDBRepository.addFavorite(recipe.toEntity())
                _isFavorite.value = true
            }
        }
    }
    fun checkIfFavorite(recipeId: String) {
        viewModelScope.launch {
            _isFavorite.value = RDBRepository.isFavorite(recipeId)
        }
    }

}
private fun Recipe.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        imageRecipe = this.image,
        category = this.category,
        cookingTime = this.cookingTime,
        energy = this.energy,
        rating = this.rating,
        ingredients = this.ingredients,
        instructions = this.instructions

    )
}
