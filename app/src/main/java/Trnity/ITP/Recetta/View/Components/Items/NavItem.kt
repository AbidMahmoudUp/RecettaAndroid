package Trnity.ITP.Recetta.View.Components.Items

import Trnity.ITP.Recetta.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.unit.dp

sealed class NavItem(val route: String, val icon: Int, val title: String)
{
    object Home : NavItem("home", R.drawable.home, "Home")
    object Favorites : NavItem("favorites", R.drawable.favorite, "Favorites")
    object Profile : NavItem("profile", R.drawable.person, "Profile")
    object Inventory : NavItem("inventory",R.drawable.inventory,"inventory")
    object login : NavItem("login",0,"")

}
