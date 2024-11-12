package Trnity.ITP.Recetta.Data.remote.repository

import Trnity.ITP.Recetta.Data.remote.api.InventoryApiService
import Trnity.ITP.Recetta.Model.entities.Inventory
import Trnity.ITP.Recetta.Model.repositories.InventoryRepository

class InventoryRepositoryImplementation(private  val inventoryApiService : InventoryApiService) : InventoryRepository{
  override  suspend fun getInventory(id: String): Inventory {
        return inventoryApiService.getInventory(id)
    }

    //override   suspend fun getAllInventory(): List<Inventory> {
      //  return inventoryApiService.getAllInventory()
    //}
}