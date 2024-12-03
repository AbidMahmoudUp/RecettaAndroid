package Trnity.ITP.Recetta.Routes

import GeneratedRecipeListScreen
import HomeScreen
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.UpdateUserDto
import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.View.AddIngredient
import Trnity.ITP.Recetta.View.Components.Items.NavItem
import Trnity.ITP.Recetta.View.EditProfileScreen
import Trnity.ITP.Recetta.View.FavoriteScreen
import Trnity.ITP.Recetta.View.GenerateRecipeScreen
import Trnity.ITP.Recetta.View.InventoryScreen
import Trnity.ITP.Recetta.View.ProfileScreen
import Trnity.ITP.Recetta.View.RecipeScreen
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = NavItem.Home.route
    ) {
        composable(NavItem.Home.route) { HomeScreen(navController) }
        composable(NavItem.Favorites.route) { FavoriteScreen(navController) }
        composable(NavItem.Profile.route) { ProfileScreen(navController) }
        composable(NavItem.Inventory.route) { InventoryScreen(navController)  }
        composable("AddIngrediant") { AddIngredient(navController = navController)  }
        composable("GenerateRecipe") { GenerateRecipeScreen(navController = navController)  }
        composable("GeneratedList/{recipes}", arguments = listOf(navArgument("recipes") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val recipesJson = backStackEntry.arguments?.getString("recipes")
            val recipes = Gson().fromJson(recipesJson, Array<Recipe>::class.java).toList()
            GeneratedRecipeListScreen(navController = navController, recipes = recipes)
        }


        composable(route = "editProfile"+"/{responseJson}" ,
            arguments = listOf(
                navArgument("responseJson") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) {
                backStackEntry ->
            val responseJson = backStackEntry.arguments?.getString("responseJson")
            val response = Gson().fromJson(responseJson, UpdateUserDto::class.java)
            EditProfileScreen(navController = navController , userData = response)
        }


        composable(
            "recipeScreen/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) {backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")
            Log.d("MainNavigation", "Navigating to RecipeScreen with recipeId: $recipeId")

            if (recipeId != null) {
                RecipeScreen(recipeId = recipeId, navController = navController)

            }
        }

    }
}