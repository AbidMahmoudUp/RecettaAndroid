package Trnity.ITP.Recetta.Data.remote.api

import Trnity.ITP.Recetta.Model.entities.Inventory
import Trnity.ITP.Recetta.Model.entities.Plat
import retrofit2.http.GET
import retrofit2.http.Path

interface InventoryApiService {

    @GET("inventory/{id}")
    suspend fun getInventory(@Path("id") id: String): Inventory

    @GET("Plat/all")
    suspend fun getAllInventory(): List<Inventory>
}