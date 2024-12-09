package Trnity.ITP.Recetta.Data.remote.repository

import Trnity.ITP.Recetta.Data.remote.Requests.UpdateUserInventory
import Trnity.ITP.Recetta.Data.remote.api.InventoryApiService
import Trnity.ITP.Recetta.Model.entities.Inventory
import Trnity.ITP.Recetta.Model.repositories.InventoryRepository
import okhttp3.MultipartBody

class InventoryRepositoryImplementation(private  val inventoryApiService : InventoryApiService) : InventoryRepository{
    override suspend fun updateInventory(id: String,request:  UpdateUserInventory): Inventory {
        return  inventoryApiService.updateInventory(id,request)
    }

    override  suspend fun getInventory(id: String): Inventory {
        return inventoryApiService.getInventory(id)
    }

    override suspend fun startCooking(id: String, request: UpdateUserInventory): Inventory {
        return inventoryApiService.startCooking(id,request)
    }

    override suspend fun updateInventoryWithImage(id: String, img: MultipartBody.Part) : Boolean  {
        return try {
            val response = inventoryApiService.updateInventoryWithImage(id, img)
            response.isSuccessful // Return true if the request was successful, otherwise false
        } catch (e: Exception) {
            // Log the error if needed
            false // Return false if there's an error
        }
    }


}