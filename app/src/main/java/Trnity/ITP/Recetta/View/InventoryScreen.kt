package Trnity.ITP.Recetta.View

import Trnity.ITP.Recetta.Model.entities.IngredientInventory
import Trnity.ITP.Recetta.Model.entities.IngredientRecipe
import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.View.Components.IngrediantInventoryCard

import Trnity.ITP.Recetta.ViewModel.InventoryViewModel
import Trnity.ITP.Recetta.ViewModel.RecipeViewModel
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InventoryScreen(
    navController: NavController,
    viewModel: InventoryViewModel = hiltViewModel(),
    recipeViewModel: RecipeViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    val inventory = viewModel.inventory.collectAsState().value
    val isLoadingRecipe by recipeViewModel.isLoading.observeAsState(false)
    var progress by remember { mutableStateOf(0f) }
    var showLoadingScreen by remember { mutableStateOf(false) }
    val generatedRecipes by recipeViewModel.generatedRecipes.observeAsState(emptyList())

    // Use mutableStateMapOf to keep track of selected items and their quantities
    val selectedIngredients = remember { mutableStateMapOf<IngredientInventory, Int>() }
    val (isSelectionMode, setSelectionMode) = remember { mutableStateOf(false) } // Selection mode state

    val preferences = LocalContext.current.getSharedPreferences("checkbox", Context.MODE_PRIVATE)
    val userId = preferences.getString("userId", "")

    LaunchedEffect(Unit) {
        viewModel.fetchInventory(userId.toString())
    }
    LaunchedEffect(isLoadingRecipe, showLoadingScreen) {
        if (showLoadingScreen && !isLoadingRecipe ) {
            val list = generatedRecipes.toList()
            Log.d("List :", list.toString())
            Log.d("Generated List :", generatedRecipes.toString())
            var recipesJson : String
            if(list.isNotEmpty())
            {
                recipesJson = Uri.encode(Gson().toJson(list))
            }
            else
            {
                recipesJson = Uri.encode(Gson().toJson(list))
            }
            Log.d("JSON RECIPE", recipesJson)
            recipeViewModel.resetRecipes()
            Log.d("List after :", list.toString())
            Log.d("Generated List after :", generatedRecipes.toString())
            navController.navigate("GeneratedList/$recipesJson") {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = false
                }
                launchSingleTop = true
                restoreState = false
            }
        }
    }


        if (showLoadingScreen) {
            RecipeGenerationLoadingContent(
                progress = progress,
                onProgressComplete = { showLoadingScreen = false },
                incrementProgress = {
                    progress = (progress + 0.01f).coerceAtMost(1f)
                }
            )
        }
        else{
    Column(modifier = Modifier.padding(16.dp)) {
        // Top bar
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF039be5)
                )
            }
            Text(text = if (isSelectionMode) "Select Items" else "Food Manager")
            Spacer(modifier = Modifier.width(120.dp))

            IconButton(onClick = {
                navController.navigate("scan")
            }, content = {
                Icon(
                    painter = painterResource(id = R.drawable.scaningrediant),
                    contentDescription = "Scan Ingredient"
                )
            })
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isSelectionMode) {
                Text(
                    text = "Cancel",
                    color = Color.Red,
                    modifier = Modifier.clickable {
                        selectedIngredients.clear()
                        setSelectionMode(false) // Exit selection mode
                    }
                )
                Box(
                    modifier = Modifier
                        .clickable {
                            Log.d("GenerateButton", "Generate Recipes Clicked")
                            showLoadingScreen = true

                            // Ensure selectedIngredients is not empty before proceeding
                            if (selectedIngredients.isNotEmpty()) {
                                val filteredIngredients = inventory.ingredients.filter { selectedIngredients.containsKey(it) }
                                    .map { IngredientRecipe(it.ingredient, selectedIngredients[it] ?: 0) }

                                Log.d("GenerateButton", "Filtered Ingredients: $filteredIngredients")

                                // Generate recipes
                                recipeViewModel.generateRecipes(filteredIngredients.toSet()) // Send as a set to avoid duplicates
                            } else {
                                Log.d("GenerateButton", "No ingredients selected.")
                            }
                        }

                ) {
                    Text(
                        text = "Generate Recipes",
                        color = Color.Blue,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                Text(text = "Ingredients (${inventory.ingredients.size})")

                Text(
                    text = "+ Add item",
                    color = Color(0xFFF46D42),
                    modifier = Modifier.clickable {
                        navController.navigate("AddIngrediant")
                    }
                )
            }
        }

        if (inventory.ingredients.isEmpty()) {
            noIngrediantSection(R.drawable.crying_tomato,"Oops, no ingredients!","We couldn’t find any ingredients in your\n list. Start by adding some items from \n your pantry, or use the camera feature to \n fill your inventory quickly. Let’s get \n cooking!\n")
        } else {
            SwipeRefresh(state = swipeRefreshState, onRefresh = { viewModel.fetchInventory(userId.toString()) }) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 64.dp)
                ) {
                    val ingredientList = inventory.ingredients.toList()
                    items(ingredientList.size) { index ->
                        val ingredient = ingredientList[index]
                        val isSelected = selectedIngredients.containsKey(ingredient)
                        val quantity = selectedIngredients[ingredient] ?: 0

                        IngrediantInventoryCard(
                            ingredient = ingredient,
                            isSelected = isSelected,
                            isSelectionMode = isSelectionMode,
                            quantity = quantity,
                            onLongClick = {
                                selectedIngredients[ingredient] = 0 // Initialize quantity
                                setSelectionMode(true) // Enter selection mode
                            },
                            onIncrement = {
                                val currentQuantity = selectedIngredients[ingredient] ?: 0
                                if (currentQuantity < ingredient.qte) {
                                    selectedIngredients[ingredient] = currentQuantity + 1
                                    Log.d("InventoryScreen", "Updated quantity for ${ingredient.ingredient.name}: ${selectedIngredients[ingredient]}")
                                }
                            },
                            onDecrement = {
                                val currentQuantity = selectedIngredients[ingredient] ?: 0
                                if (currentQuantity > 1) {
                                    selectedIngredients[ingredient] = currentQuantity - 1
                                    Log.d("InventoryScreen", "Updated quantity for ${ingredient.ingredient.name}: ${selectedIngredients[ingredient]}")
                                } else {
                                    selectedIngredients.remove(ingredient)
                                    Log.d("InventoryScreen", "Removed ${ingredient.ingredient.name} from selection.")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
    }
}








@Composable
fun noIngrediantSection(image :Int , title : String  , description :String ) {
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
        , verticalArrangement = Arrangement.Center
    ){
        Spacer(modifier = Modifier.height(175.dp))
        Image(painter = painterResource(image), contentDescription = "cryingTomato", modifier = Modifier
            .width(89.dp)
            .height(94.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(title, fontWeight = FontWeight.Bold , fontSize = 20.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(description
            , textAlign = TextAlign.Center, fontSize = 14.sp , color = Color(0xFF706C6C)

        )
    }


}
