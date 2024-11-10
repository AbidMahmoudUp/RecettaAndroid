package Trnity.ITP.Recetta.Data.remote.api

import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.Model.entities.User
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApiService {
    @GET("Recipe/{id}")
    suspend fun getRecipe(@Path("id") id: String): Recipe

    @GET("Recipe/all")
    suspend fun getAllRecipes(): List<Recipe>
}