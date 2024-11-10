package Trnity.ITP.Recetta.View

import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.View.Components.IngrediantCard
import Trnity.ITP.Recetta.ViewModel.IngrediantViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InventoryScreen(
    navController: NavController,
    viewModel: IngrediantViewModel = hiltViewModel() // Inject viewModel with Hilt
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    val ingredients = viewModel.ingredients.collectAsState().value // Collect ingredients state
    println("the ingrediants are :"+ ingredients)

    Column(modifier = Modifier.padding(16.dp)) {
        // Top bar with navigation and title
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF039be5)
                )
            }
            Text(text = "Food Manager")
            Spacer(modifier = Modifier.width(120.dp))
            Icon(
                painter = painterResource(id = R.drawable.scaningrediant),
                contentDescription = "Scan Ingredient"
            )
        }

        // Ingredient count and Add button
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ingredients (${ingredients.size})")
            Text(
                text = "+ Add item",
                color = Color(0xFFF46D42),
                modifier = Modifier
                    .padding(12.dp, 0.dp, 12.dp, 0.dp)
                    .clickable { navController.navigate("AddIngrediant") }
            )
        }

        // Display ingredients or "No ingredients" message
        if (ingredients.isEmpty()) {
            Text("No ingredients available")
        } else {
            SwipeRefresh(state = swipeRefreshState, onRefresh = viewModel::fetchAllIngredients) {


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 64.dp)
            ) {
                items(ingredients) { ingredient ->
                    IngrediantCard(ingrediant = ingredient)
                }
            }
            }
        }
    }
}
