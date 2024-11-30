package Trnity.ITP.Recetta.Data.remote.repository

import Trnity.ITP.Recetta.Data.remote.api.RecipeApiService
import Trnity.ITP.Recetta.Model.entities.Inventory
import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.Model.repositories.RecipeRepository
import Trnity.ITP.Recetta.R
import okhttp3.ResponseBody

class RecipeRepositoryImplementation(private val recipeApiService: RecipeApiService) : RecipeRepository {

   override suspend fun getRecipe(id: String): Recipe {
        return recipeApiService.getRecipe(id)
    }

    override suspend fun generateRecipe(request : Map<String, String> ) :  Set<Recipe>{
        return recipeApiService.generateRecipes(request)
    }

    override suspend fun getAllRecipe(): List<Recipe> {
        return recipeApiService.getAllRecipes()
    }
}