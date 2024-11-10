package Trnity.ITP.Recetta.Data.remote.repository

import Trnity.ITP.Recetta.Data.remote.api.IngredientApiService
import Trnity.ITP.Recetta.Model.entities.Ingredient
import Trnity.ITP.Recetta.Model.repositories.IngredientRepository
import kotlinx.coroutines.delay

class IngredientRepositoryImplementation(private val ingrediantApiService : IngredientApiService) :IngredientRepository {

    override suspend fun getIngredient(id: String): Ingredient {
        return ingrediantApiService.getIngredient(id)
    }

    override suspend fun getAllIngredients(): List<Ingredient> {
        return ingrediantApiService.getAllIngredients()
    }
}