package Trnity.ITP.Recetta.Model.repositories

import Trnity.ITP.Recetta.Model.entities.Inventory
import Trnity.ITP.Recetta.Model.entities.Plat

interface InventoryRepository {
    suspend fun getAllInventory(): List<Inventory>
    suspend fun getInventory(id:String): Inventory
}