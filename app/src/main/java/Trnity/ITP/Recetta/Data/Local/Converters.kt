package Trnity.ITP.Recetta.Data.Local;

import Trnity.ITP.Recetta.Model.entities.IngredientRecipe
import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromIngredientsList(value: List<IngredientRecipe>): String {
        Log.d("FromIngredientList" , value.toString())
        Log.d("Json" , gson.toJson(value))
        return gson.toJson(value)
    }

    @TypeConverter
    fun toIngredientsList(value: Any): List<IngredientRecipe> {
        return when (value) {
            is String -> {
                // Handle the normal case where the value is a JSON string
                val listType = object : TypeToken<List<IngredientRecipe>>() {}.type
                gson.fromJson(value, listType)
            }
            is List<*> -> {
                // Handle the edge case where the value is already a list
                @Suppress("UNCHECKED_CAST")
                value as List<IngredientRecipe>
            }
            else -> {
                throw IllegalArgumentException("Unexpected type for toIngredientsList: ${value::class.java.name}")
            }
        }
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
