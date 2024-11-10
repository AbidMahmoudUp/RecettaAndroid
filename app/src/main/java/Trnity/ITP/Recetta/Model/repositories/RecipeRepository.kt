package Trnity.ITP.Recetta.Model.repositories

import Trnity.ITP.Recetta.Model.entities.Recipe

interface RecipeRepository {

    suspend fun getAllRecipe(): List<Recipe>

    suspend fun getRecipe(id:String): Recipe
}