package Trnity.ITP.Recetta.Data.Local

import javax.inject.Inject

class RecipeEntityRepository@Inject constructor(private val recipeDao: RecipeDao) {
    suspend fun addFavorite(recipe: RecipeEntity) {
        recipeDao.insertFavorite(recipe)
    }

    suspend fun getFavorites(): List<RecipeEntity> {
        return recipeDao.getAllFavorites()
    }

    suspend fun removeFavorite(recipe: RecipeEntity) {
        recipeDao.deleteFavorite(recipe)
    }

    suspend fun isFavorite(recipeId: String): Boolean {
        return recipeDao.getFavoriteById(recipeId) != null
    }
}