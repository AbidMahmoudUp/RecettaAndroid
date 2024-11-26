package Trnity.ITP.Recetta.ViewModel

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
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val ingredientRepository: IngredientRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes
    private val _generatedRecipes = MutableLiveData<List<Recipe>>()
    val generatedRecipes: LiveData<List<Recipe>> get() = _generatedRecipes
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

    @SuppressLint("SuspiciousIndentation")
    fun generateRecipes(ingredients: Set<IngredientRecipe>) {
        val firstPartOfRequest = "JSON RESPONSE ONLY for recipes using ONLY these ingredients: "

      /*  val secondPartOfRequest = ingredients.joinToString(separator = ", ", postfix = ".") {
            "${it.qte} ${it.ingredient?.name}"
        }*/

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
               // val rawResponse = responseBody.string()
              //  Log.d("Raw AI Response", "Response: $rawResponse")
              //  println("Raw AI Response: $rawResponse")
                  _generatedRecipes.postValue(responseBody.toList())
                Log.d("AI Request", "Request: $requestBody")
                Log.d("AI Response", "Response: $responseBody")
            } catch (e: Exception) {
                _generatedRecipes.postValue(emptyList<Recipe>())
                Log.e("AI Error", "Exception occurred: ${e.message}")
            }
        }
    }

}
