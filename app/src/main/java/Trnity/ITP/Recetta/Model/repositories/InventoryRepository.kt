package Trnity.ITP.Recetta.Model.repositories

import Trnity.ITP.Recetta.Data.remote.Requests.UpdateUserInventory
import Trnity.ITP.Recetta.Model.entities.Inventory

interface InventoryRepository {
    suspend fun updateInventory( id: String,request :  UpdateUserInventory ): Inventory
    suspend fun getInventory(id:String): Inventory
}