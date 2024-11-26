package Trnity.ITP.Recetta.Model.repositories

import Trnity.ITP.Recetta.Model.entities.Ingredient
import Trnity.ITP.Recetta.Model.entities.IngredientRecipe
import Trnity.ITP.Recetta.Model.entities.Recipe
import okhttp3.ResponseBody
import java.net.CacheRequest

interface RecipeRepository {

    suspend fun getAllRecipe(): List<Recipe>

    suspend fun getRecipe(id:String): Recipe

    suspend fun generateRecipe( request: Map<String, String> ) : Set<Recipe>
}