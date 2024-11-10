package Trnity.ITP.Recetta.View.Components
import Trnity.ITP.Recetta.View.Components.Items.DrawerItem
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    drawerState: DrawerState,
    items: List<DrawerItem>,
    onItemClick: (DrawerItem) -> Unit,
    scope: CoroutineScope,
    drawerHeight: Float
) {
    ModalDrawerSheet(
        modifier = Modifier.width(100.dp).height(drawerHeight.dp) // Set drawer width to 100.dp

    ) {
        DrawerContent(
            items = items,
            onItemClick = { item ->
                scope.launch { drawerState.close() }
                onItemClick(item)
            }
        )
    }
}
