package Trnity.ITP.Recetta.Data.remote.api

import Trnity.ITP.Recetta.Model.entities.Ingredient
import retrofit2.http.GET
import retrofit2.http.Path

interface IngredientApiService {

    @GET("ingredient/{id}")
    suspend fun getIngredient(@Path("id") id: String): Ingredient

    @GET("api/ingrediant")
    suspend fun getAllIngredients(): List<Ingredient>
}