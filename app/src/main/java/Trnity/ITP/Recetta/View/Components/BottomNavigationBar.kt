package Trnity.ITP.Recetta.View.Components

import Trnity.ITP.Recetta.View.Components.Items.NavItem
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource

@Composable
fun BottomNavigationBar(navController: NavController, onHeightChange: (Float) -> Unit) {
    val items = listOf(NavItem.Home, NavItem.Favorites, NavItem.Profile, NavItem.Inventory)

    NavigationBar(
        modifier = Modifier.onGloballyPositioned { coordinates ->
            val height = coordinates.size.height.toFloat()
            onHeightChange(height)
        }
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
//                        tint = MaterialTheme.colorScheme.onBackground // Optional: Custom icon color
                    )
                },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    if(currentRoute == "home"){
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                interactionSource = remember { MutableInteractionSource() }, // Suppress ripple/hover effects
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent, // Remove the selection circle background
                    unselectedIconColor = Color(0xFF4D4D4D),
                    selectedIconColor = Color(0xFFF46D42),
                    unselectedTextColor = Color.Gray,
                    selectedTextColor = Color(0xFFF46D42)
                )
            )
        }
    }

}