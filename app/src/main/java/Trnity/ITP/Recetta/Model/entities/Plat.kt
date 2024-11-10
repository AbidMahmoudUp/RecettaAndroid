package Trnity.ITP.Recetta.Model.entities

data class Plat(val id : Int , val name : String , val image : String , val ingrediant : Set<Ingredient>  )