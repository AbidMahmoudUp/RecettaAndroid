package Trnity.ITP.Recetta.Data.Local;

import Trnity.ITP.Recetta.Model.entities.IngredientRecipe
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromIngredientsList(value: List<IngredientRecipe>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toIngredientsList(value: String): List<IngredientRecipe> {
        val listType = object : TypeToken<List<IngredientRecipe>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromInstructionsList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toInstructionsList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
}
