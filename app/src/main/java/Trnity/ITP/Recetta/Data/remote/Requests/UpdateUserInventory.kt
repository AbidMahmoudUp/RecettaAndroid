package Trnity.ITP.Recetta.Data.remote.Requests

import Trnity.ITP.Recetta.Model.entities.IngredientRecipe

data class UpdateUserInventory (  val ingredients : Set<IngredientRecipe> )