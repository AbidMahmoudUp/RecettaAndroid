package Trnity.ITP.Recetta.Model.entities

data class IngredientRecipe(

    val ingredient : Ingredient? ,
    var qte : Int,

){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is IngredientRecipe) return false
        return  ingredient?.name == other.ingredient?.name
    }

    override fun hashCode(): Int {
        return ingredient?.name?.hashCode() ?: 0
    }
}
