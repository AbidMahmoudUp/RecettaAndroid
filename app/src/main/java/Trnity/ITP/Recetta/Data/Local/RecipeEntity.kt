package Trnity.ITP.Recetta.Data.Local;

import Trnity.ITP.Recetta.Model.entities.IngredientRecipe
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite_recipes")
data class RecipeEntity(
    @PrimaryKey @SerializedName("_id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("image") val imageRecipe: String = "",
    @SerializedName("category") val category: String = "",
    @SerializedName("cookingTime") val cookingTime: String = "",
    @SerializedName("energy") val energy: String = "",
    @SerializedName("rating") val rating: String = "",
    @SerializedName("ingredients") @TypeConverters(Converters::class) val ingredients: List<IngredientRecipe> = emptyList(),
    @SerializedName("instructions") @TypeConverters(Converters::class) val instructions: List<String> = emptyList()
)
