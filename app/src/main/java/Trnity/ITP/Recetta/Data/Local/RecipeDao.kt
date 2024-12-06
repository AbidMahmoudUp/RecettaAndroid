package Trnity.ITP.Recetta.Data.Local
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecipeDao {

    @Insert
    suspend fun insertFavorite(recipe: RecipeEntity) // Added the @Insert annotation

    @Query("SELECT * FROM favorite_recipes")
    suspend fun getAllFavorites(): List<RecipeEntity>

    @Delete
    suspend fun deleteFavorite(recipe: RecipeEntity)

    @Query("SELECT * FROM favorite_recipes WHERE id = :recipeId LIMIT 1")
    suspend fun getFavoriteById(recipeId: String): RecipeEntity?

}
