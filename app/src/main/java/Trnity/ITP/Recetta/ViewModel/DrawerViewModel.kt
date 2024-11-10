package Trnity.ITP.Recetta.ViewModel

import Trnity.ITP.Recetta.View.Components.Items.DrawerItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings

class DrawerViewModel {
    val drawerItems = listOf(
        DrawerItem("Home", Icons.Default.Home, "home"),
        DrawerItem("Settings", Icons.Default.Settings, "settings")
    )
}