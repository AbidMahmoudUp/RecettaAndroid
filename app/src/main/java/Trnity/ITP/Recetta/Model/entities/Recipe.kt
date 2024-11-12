package Trnity.ITP.Recetta.Model.entities

data class Recipe (
                    val id : String,
                    val title: String,
                    val description: String,
                    val imageRes: Int,
                    val category :String,
                    val cookingTime: String ,
                    val energy:String ,
                    val rating : String ,
                    val ingredients : List<Ingredient>
                    )