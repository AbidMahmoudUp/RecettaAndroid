package Trnity.ITP.Recetta.Model.entities

import java.time.LocalDate

data class IngredientInventory(

    val ingredient: Ingredient,
    val qte : Int,
    val date: String
)
