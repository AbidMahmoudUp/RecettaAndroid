package Trnity.ITP.Recetta.View.Components



import Trnity.ITP.Recetta.View.Components.Items.NavItem
import androidx.compose.material3.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource


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
