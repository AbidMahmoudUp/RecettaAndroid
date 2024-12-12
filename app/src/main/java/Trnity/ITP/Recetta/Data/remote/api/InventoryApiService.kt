package Trnity.ITP.Recetta.Data.remote.api

import Trnity.ITP.Recetta.Data.remote.Requests.UpdateUserInventory
import Trnity.ITP.Recetta.Model.entities.Inventory
import Trnity.ITP.Recetta.Model.entities.Recipe
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Multipart
import retrofit2.http.Part

interface InventoryApiService {

    @GET("inventory/{id}")
    suspend fun getInventory(@Path("id") id: String): Inventory

    @PATCH("inventory/addIngredients/{id}")
    suspend fun updateInventory(@Path("id") id: String , @Body request:  UpdateUserInventory): Inventory

    @PATCH("inventory/substractIngredients/{id}")
    suspend fun startCooking(@Path("id")id:String , @Body request: UpdateUserInventory) :Inventory

    @Multipart
    @POST("inventory/updateInventoryWithImage/{id}")
    suspend fun updateInventoryWithImage(@Path("id") id: String, @Part request: MultipartBody.Part) : Response<Inventory>
    @Multipart
    @POST("generative-ia-recipe")
    suspend fun scanRecipe(@Part file: MultipartBody.Part) : Response<Recipe>?
}