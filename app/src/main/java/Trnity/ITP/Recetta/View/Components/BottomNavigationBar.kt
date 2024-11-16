package Trnity.ITP.Recetta.View.Components

import HomeScreen
import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.View.AddIngridiant
import Trnity.ITP.Recetta.View.Components.Items.HomeCardItem
import Trnity.ITP.Recetta.View.Components.Items.NavItem
import Trnity.ITP.Recetta.View.FavoritesScreen
import Trnity.ITP.Recetta.View.InventoryScreen
import Trnity.ITP.Recetta.View.ProfileScreen
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
        composable(NavItem.Favorites.route) { FavoritesScreen() }
        composable(NavItem.Profile.route) { ProfileScreen() }
        composable(NavItem.Inventory.route) {InventoryScreen(navController)  }
        composable("AddIngrediant") { AddIngridiant(navController = navController)  }
        composable(
            "recipeScreen/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) {backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")
            Log.d("MainNavigation", "Navigating to RecipeScreen with recipeId: $recipeId")

            if (recipeId != null) {
                RecipeScreen(recipeId = recipeId)

            }
        }
    }
}