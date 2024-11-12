package Trnity.ITP.Recetta.Model.entities

import java.time.LocalDate

data class IngredientInventory(

    val ingrediant: Ingredient,
    val qte : Int,
    val date: String
)
