package Trnity.ITP.Recetta.View.Components

import GeneratedRecipeListScreen
import HomeScreen
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.LoginResponse
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.UpdateUserDto
import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.View.AddIngredient
import Trnity.ITP.Recetta.View.ProfileScreen
import Trnity.ITP.Recetta.View.AuthScreens.Screen

import Trnity.ITP.Recetta.View.Components.Items.HomeCardItem
import Trnity.ITP.Recetta.View.Components.Items.NavItem
import Trnity.ITP.Recetta.View.EditProfileScreen
import Trnity.ITP.Recetta.View.FavoriteScreen
import Trnity.ITP.Recetta.View.GenerateRecipeScreen
import Trnity.ITP.Recetta.View.InventoryScreen
import Trnity.ITP.Recetta.View.RecipeGenerationLoadingScreen
import Trnity.ITP.Recetta.View.RecipeScreen
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson

@Composable
fun BottomNavigationBar(navController: NavController,onHeightChange: (Float) -> Unit) {
    val items = listOf(NavItem.Home, NavItem.Favorites, NavItem.Profile,NavItem.Inventory) // list of my items
    NavigationBar( modifier = Modifier.onGloballyPositioned { coordinates ->
        val height = coordinates.size.height.toFloat()
        onHeightChange(height)

    }) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            println("currentRoute is : "+item.route)
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = NavItem.Home.route
    ) {
        composable(NavItem.Home.route) { HomeScreen(navController) }
        composable(NavItem.Favorites.route) { FavoriteScreen(navController)}
        composable(NavItem.Inventory.route) {InventoryScreen(navController)}
        composable("AddIngrediant") { AddIngredient(navController = navController)}
        composable("GenerateRecipe") { GenerateRecipeScreen(navController = navController)}
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
        composable("LoadingPage") { RecipeGenerationLoadingScreen(navController = navController)}
        composable("GeneratedList") { GeneratedRecipeListScreen(navController = navController)}
        composable(route = NavItem.Profile.route) {ProfileScreen(navController) }
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