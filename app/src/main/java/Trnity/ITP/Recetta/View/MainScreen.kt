import Trnity.ITP.Recetta.View.Components.AppDrawer
import Trnity.ITP.Recetta.View.Components.BottomNavigationBar
import Trnity.ITP.Recetta.View.Components.DrawerContent
import Trnity.ITP.Recetta.Routes.MainNavigation
import Trnity.ITP.Recetta.ViewModel.DrawerViewModel
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    var bottomBarHeight by remember { mutableStateOf(0f) }


        Scaffold(

            bottomBar = { BottomNavigationBar(navController){height -> bottomBarHeight =height} }
        ) {
            MainNavigation(navController)
        }
    }


fun navigateTo(navController: NavController, destination: String) {
    navController.navigate(destination)
}
