package Trnity.ITP.Recetta.Model.repositories

import Trnity.ITP.Recetta.Data.remote.Requests.UpdateUserInventory
import Trnity.ITP.Recetta.Model.entities.Inventory
import Trnity.ITP.Recetta.Model.entities.Recipe
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path

interface InventoryRepository {
    suspend fun updateInventory( id: String,request :  UpdateUserInventory ): Inventory
    suspend fun getInventory(id:String): Inventory
    suspend fun startCooking(id:String, request: UpdateUserInventory) :Inventory
    suspend fun updateInventoryWithImage(id: String, img: MultipartBody.Part):Boolean
    suspend fun scanRecipe(fileImage: MultipartBody.Part): Response<Recipe>?

}