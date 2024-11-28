package Trnity.ITP.Recetta.Model.repositories

import Trnity.ITP.Recetta.Data.remote.Requests.UpdateUserInventory
import Trnity.ITP.Recetta.Model.entities.Inventory
import retrofit2.http.Body
import retrofit2.http.Path

interface InventoryRepository {
    suspend fun updateInventory( id: String,request :  UpdateUserInventory ): Inventory
    suspend fun getInventory(id:String): Inventory
    suspend fun startCooking(id:String, request: UpdateUserInventory) :Inventory

}