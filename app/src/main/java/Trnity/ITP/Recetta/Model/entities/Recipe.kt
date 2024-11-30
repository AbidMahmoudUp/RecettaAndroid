package Trnity.ITP.Recetta.Model.entities

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("_id") val id: String ="",
    @SerializedName("title") val title: String ="",
    @SerializedName("description") val description: String ="",
    @SerializedName("imageRecipe") val imageRecipe: String = "",
    @SerializedName("category") val category: String = "",
    @SerializedName("cookingTime") val cookingTime: String = "",
    @SerializedName("energy") val energy: String = "",
    @SerializedName("rating") val rating: String = "",
    @SerializedName("ingredients") val ingredients: List<IngredientRecipe> = emptyList(),
    @SerializedName("instructions") val instructions : List<String> = emptyList()
)
