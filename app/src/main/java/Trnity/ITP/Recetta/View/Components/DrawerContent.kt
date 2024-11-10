@file:OptIn(ExperimentalMaterial3Api::class)

package Trnity.ITP.Recetta.View.Components
import Trnity.ITP.Recetta.View.Components.Items.DrawerItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
@Composable
fun DrawerContent(
    items: List<DrawerItem>,
    onItemClick: (DrawerItem) -> Unit
) {
    Column {
        items.forEach { item ->
            DrawerItem(item = item, onClick = { onItemClick(item) })
        }
    }
}

@Composable
fun DrawerItem(
    item: DrawerItem,
    onClick: () -> Unit
) {
   // ListItem(
     //   leadingContent = { Icon(item.icon, contentDescription = item.title) },
       // headlineText = { Text(item.title) },
      //  modifier = Modifier.clickable(onClick = onClick)
   // )
}