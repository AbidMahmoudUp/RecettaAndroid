package Trnity.ITP.Recetta.Model.entities

data class Inventory (val id : String,val idUser : String ,val ingrediant: Set<IngredientInventory>)