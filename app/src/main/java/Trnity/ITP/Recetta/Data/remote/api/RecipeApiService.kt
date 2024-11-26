package Trnity.ITP.Recetta.Data.remote.api

import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.Model.entities.User
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RecipeApiService {
    @GET("plat/{id}")
    suspend fun getRecipe(@Path("id") id: String): Recipe

    @GET("plat")
    suspend fun getAllRecipes(): List<Recipe>

    @POST("generative-ia")
    suspend fun generateRecipes(@Body prompt: Map<String, String>) :Set<Recipe>
}