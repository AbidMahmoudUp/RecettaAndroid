package Trnity.ITP.Recetta.Model.entities

data class Inventory (
    val _id : String ="",val user : String ="" ,val ingrediants: Set<IngredientInventory> = emptySet()
)
