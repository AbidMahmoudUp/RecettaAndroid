package Trnity.ITP.Recetta.View

import Trnity.ITP.Recetta.Model.entities.IngredientRecipe
import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.ViewModel.IngredientViewModel
import Trnity.ITP.Recetta.ViewModel.RecipeViewModel
import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.util.lerp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun GenerateRecipeScreen(navController: NavController,recipeViewModel : RecipeViewModel = hiltViewModel() ,ingredientViewModel: IngredientViewModel = hiltViewModel()) {
    val isLoading by recipeViewModel.isLoading.observeAsState(false)
    val generatedRecipes by recipeViewModel.generatedRecipes.observeAsState(emptyList())

    var listIngredientQte by remember { mutableStateOf(mutableSetOf<IngredientRecipe>()) }
  //  val focusManager = LocalFocusManager.current
    var progress by remember { mutableStateOf(0f) }
    var showLoadingScreen by remember { mutableStateOf(false) }
    val isSaveButtonVisible by remember {
        derivedStateOf { listIngredientQte.isNotEmpty() }
    }



    // if the loading is done let s navigate to the generation list root
    LaunchedEffect(isLoading, showLoadingScreen) {
        if (showLoadingScreen && !isLoading ) {
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


    Box(modifier = Modifier.fillMaxSize() ,contentAlignment = Alignment.Center )
    {

        if (showLoadingScreen) {
            RecipeGenerationLoadingContent(
                progress = progress,
                onProgressComplete = { showLoadingScreen = false },
                incrementProgress = {
                    progress = (progress + 0.01f).coerceAtMost(1f)
                }
            )
        } else {
            GenerateRecipeContent(
                navController = navController,
                ingredientViewModel = ingredientViewModel,
                listIngredientQte = listIngredientQte,
                onIngredientsUpdated = { updatedList ->
                    listIngredientQte = updatedList.toMutableSet()
                },
                onGenerateClick = {
                    showLoadingScreen = true
                    recipeViewModel.generateRecipes(listIngredientQte)
                },
                saveButton = isSaveButtonVisible
            )
        }
    }
}





@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun GenerateRecipeContent(
    navController: NavController,
    ingredientViewModel: IngredientViewModel,
    listIngredientQte: MutableSet<IngredientRecipe>,
    onIngredientsUpdated: (Set<IngredientRecipe>) -> Unit,
    onGenerateClick: () -> Unit,

    saveButton : Boolean
) {
    val ingredients by ingredientViewModel.ingredients.collectAsState()
    val categories = listOf("All", "Fruit", "Vegetables", "Meat", "Nuts")
    val focusManager = LocalFocusManager.current

    var selectedCategory by remember { mutableStateOf("All") }
    var searchText by remember { mutableStateOf("") }
   // var listIngredientQte by remember { mutableStateOf(mutableSetOf<IngredientRecipe>()) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                focusManager.clearFocus() // Clear focus when tapping outside
            }
    ) {
        // Top Row: Back button and title
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate Recipe" , fontWeight = FontWeight.Bold)
            if (saveButton) {
                val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")

                val animatedColor by infiniteTransition.animateColor(
                    initialValue = Color(0xFF60DDAD),
                    targetValue = Color(0xFF4285F4),
                    animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
                    label = "color"
                )
                Text(
                    text = "Generate",
                    modifier = Modifier.clickable {
                        Log.d("Ingredient QTE TEST", listIngredientQte.toString())
                        onGenerateClick() // Trigger the lambda on click
                    },
                    color = animatedColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchText,
                maxLines = 1,
                onValueChange = { newText -> searchText = newText },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                label = { Text(text="Search",  color = Color.Black, modifier =Modifier.background(Color.Transparent)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Black
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Gray,
                    focusedLabelColor = Color(0xFFF46D42),
                    unfocusedLabelColor = Color.Black,
                    focusedIndicatorColor = Color(0xFFF46D42),
                    unfocusedIndicatorColor = Color.Black,
                    disabledIndicatorColor = Color.Gray,
                    cursorColor = Color(0xFFF46D42)
                )
            )
            Text(
                "Categories",
                modifier = Modifier.align(Alignment.Start),
                style = MaterialTheme.typography.titleMedium
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories.size) { index ->
                    val category = categories[index]
                    val imageResId = when (category) {
                        "All" -> R.drawable.apps  // Map to apps.xml
                        "Fruit" -> R.drawable.nutrition  // Map to nutrution.xml
                        "Vegetables" -> R.drawable.vegtable  // Existing image for vegetables
                        "Meat" -> R.drawable.kebab_dining  // Map to kebab_dining.xml
                        "Spices" -> R.drawable.salinity  // Map to salinity.xml
                        else -> R.drawable.apps  // Default image if needed
                    }
                    CategoryTab(
                        text = category,
                        isSelected = category == selectedCategory,
                        onClick = { selectedCategory = category },
                        imageResId = imageResId
                    )
                }
            }

            val filteredIngredients = ingredients.filter { ingredient ->
                fun doesMatchSearchQuery(ingredientName: String, query: String): Boolean {
                    return ingredientName.contains(query, ignoreCase = true)
                }

                // Filter based on category
                (selectedCategory == "All" || ingredient.categorie == selectedCategory) &&
                        doesMatchSearchQuery(ingredient.name, searchText)
            }
            CardGridExample(
                ingredients = filteredIngredients,
                listOfIngredients = listIngredientQte.toMutableSet(),
                onIngredientsUpdated = { updatedList ->
                    onIngredientsUpdated(updatedList)
                }
            )
        }
    }
}
@Composable
fun RecipeGenerationLoadingContent(
    progress: Float,
    onProgressComplete: () -> Unit,
    incrementProgress: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 4 })

    LaunchedEffect(Unit) {
        while (progress < 1f) {
            delay(1000) // Increment progress every 100ms
            incrementProgress()
        }
        onProgressComplete()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Horizontal Pager for images
        HorizontalPager(state = pagerState) { page ->
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                ).absoluteValue
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                Image(
                    painter = painterResource(id = getImageResourceForPage(page)),
                    contentDescription = "Image $page",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Progress Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp)
                .align(Alignment.BottomCenter)
            .offset(y = (-56).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Loading Recipes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .padding(top = 8.dp),
                color = Color.Yellow
            )
        }
    }
}
fun getImageResourceForPage(page: Int): Int {
    return when (page) {
        0 -> R.drawable.image1
        1 -> R.drawable.image2
        2 -> R.drawable.image3
        3 -> R.drawable.image4
        else -> R.drawable.image1 // Default case
    }
}