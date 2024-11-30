package Trnity.ITP.Recetta.Model.repositories

import Trnity.ITP.Recetta.Model.entities.Ingredient

interface IngredientRepository {

    suspend fun getIngredient(id: String): Ingredient

    suspend fun getAllIngredients(): List<Ingredient>


}