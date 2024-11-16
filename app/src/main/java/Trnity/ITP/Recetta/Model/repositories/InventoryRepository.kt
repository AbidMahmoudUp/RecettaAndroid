package Trnity.ITP.Recetta.Model.repositories

import Trnity.ITP.Recetta.Model.entities.Inventory

interface InventoryRepository {
    //suspend fun getAllInventory(): List<Inventory>
    suspend fun getInventory(id:String): Inventory
}