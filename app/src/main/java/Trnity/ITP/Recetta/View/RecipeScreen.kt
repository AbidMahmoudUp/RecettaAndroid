package Trnity.ITP.Recetta.View


import Trnity.ITP.Recetta.Model.entities.Ingredient
import Trnity.ITP.Recetta.Model.entities.IngredientRecipe
import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.View.Components.Items.HomeCardItem
import Trnity.ITP.Recetta.View.Components.RecipeIngredientCard
import Trnity.ITP.Recetta.ViewModel.InventoryViewModel
import Trnity.ITP.Recetta.ViewModel.RecipeViewModel
import Trnity.ITP.Recetta.ui.theme.AppBarCollapsedHeight
import Trnity.ITP.Recetta.ui.theme.AppBarExpendedHeight
import Trnity.ITP.Recetta.ui.theme.DarkGray
import Trnity.ITP.Recetta.ui.theme.Gray
import Trnity.ITP.Recetta.ui.theme.LightGray
import Trnity.ITP.Recetta.ui.theme.Pink
import Trnity.ITP.Recetta.ui.theme.Shapes
import Trnity.ITP.Recetta.ui.theme.Transparent
import Trnity.ITP.Recetta.ui.theme.White
import android.content.Context
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlin.math.max
import kotlin.math.min

@Composable
fun RecipeScreen(
    recipe: Recipe,
    navController: NavController,
    inventoryViewModel: InventoryViewModel = hiltViewModel(),
    viewModel: RecipeViewModel = hiltViewModel()
) {
    inventoryViewModel.clearErrorMessage()

    // Observe the favorite state
    val isFavorite by viewModel.isFavorite.collectAsState()

    // Load the recipe details and check if it's favorited
    LaunchedEffect(recipe.id) {
        viewModel.fetchRecipe(recipe.id)
        viewModel.checkIfFavorite(recipe.id) // Check favorite status for this recipe
    }

    val scrollState = rememberLazyListState()
    //val recipe = viewModel.recipe.collectAsState().value

    Box(Modifier.padding(0.dp, 0.dp, 0.dp, 80.dp)) {
        Content(recipe, scrollState, inventoryViewModel, navController)

        // Call ParallaxToolbar with the required parameters
        ParallaxToolbar(
            recipe = recipe,
            scrollState = scrollState,
            isFavorite = isFavorite,
            onFavoriteClick = { isNowFavorite ->
                if (isNowFavorite) {
                    viewModel.toggleFavorite(recipe)
                } else {
                    viewModel.toggleFavorite(recipe)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParallaxToolbar(
    recipe: Recipe,
    scrollState: LazyListState,
    isFavorite: Boolean,
    onFavoriteClick: (Boolean) -> Unit
) {
    val directImageUrl = "http://192.168.43.232:3000/uploads/"
    val imageHeight = AppBarExpendedHeight - AppBarCollapsedHeight
    val maxOffset = with(LocalDensity.current) { imageHeight.roundToPx() }
    val offset = min(scrollState.firstVisibleItemScrollOffset, maxOffset)
    val offsetProgress = max(0f, offset * 3f - 2f * maxOffset) / maxOffset

    // Convert imageHeight to pixels (Float) for gradient
    val imageHeightPx = with(LocalDensity.current) { imageHeight.toPx() }

    Box {
        // Parallax Image with Gradient Overlay
        Box(
            modifier = Modifier
                .height(imageHeight)
                .offset { IntOffset(x = 0, y = -offset) }
                .graphicsLayer { alpha = 1f - offsetProgress }
        ) {
            var image = ""
            if(recipe.image.contains(" "))
            {
               image =  recipe.image.replace(" ","+")
            }
            else{
                image = recipe.image
            }

            AsyncImage(
                model = "$directImageUrl$image",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            println("aaaaaa here here here ")
            println("$directImageUrl$image")
            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Transparent, White),
                            startY = imageHeightPx * 0.4f,
                            endY = imageHeightPx
                        )
                    )
            )

            // Category label
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = recipe.category.ifBlank { "Category" }, // Default if category is blank
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clip(Shapes.small)
                        .background(LightGray)
                        .padding(vertical = 6.dp, horizontal = 16.dp)
                )
            }
        }

        // Title and icon row positioned below the image
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(
                        x = 0,
                        y = maxOffset - offset
                    )
                } // Offset to place below the image
                .background(White)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Centered Title with Navigation and Favorite Icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Action */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = null,
                        tint = Gray
                    )
                }

                // Title centered in the middle
                Text(
                    text = recipe.title.ifBlank { "Recipe Title" }, // Default if title is blank
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(horizontal = 16.dp) // Space between icons and title
                        .scale(1f - 0.15f * offsetProgress)
                        .width(200.dp)
                )

                IconButton(onClick = { onFavoriteClick(!isFavorite) }) {
                    Icon(
                        painter = painterResource(id = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite),
                        contentDescription = null,
                        tint = if (isFavorite) Color.Red else Gray
                    )
                }
            }
        }
    }
}



@Composable
fun CircularButton(
    @DrawableRes iconResouce: Int,
    color: Color = Gray,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(),
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(containerColor = White, contentColor = color),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp), // Specify elevation here
        modifier = Modifier
            .width(38.dp)
            .height(38.dp)
    ) {
        Icon(painterResource(id = iconResouce), contentDescription = null)
    }
}


@Composable
fun Content(recipe: Recipe, scrollState: LazyListState,viewModel: InventoryViewModel ,navController: NavController) {
    LazyColumn(contentPadding = PaddingValues(top = AppBarExpendedHeight), state = scrollState) {
        item {
            BasicInfo(recipe)
            Description(recipe)
           // ServingCalculator()
            IngredientsHeader(recipe)
            ShoppingListButton(recipe,viewModel,navController)
            Reviews()
            Images()
        }
    }
}

@Composable
fun Images() {
    Row(Modifier.padding(bottom = 16.dp).background(Color.Red), horizontalArrangement = Arrangement.SpaceBetween) {
        Image(
            painter = painterResource(id = R.drawable.strawberry_pie_2),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .clip(Shapes.small)
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Image(
            painter = painterResource(id = R.drawable.strawberry_pie_3),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .clip(Shapes.small)
        )
    }
}

@Composable
fun Reviews() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Column {
            Text(text = "Reviews", fontWeight = Bold)
        }
        Button(
            onClick = { /*TODO*/ },
            elevation = null,
            colors = ButtonDefaults.buttonColors(
                containerColor = Transparent, contentColor = Pink
            ),
            modifier = Modifier.padding(0.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("See all")
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListButton(recipe: Recipe , viewModel: InventoryViewModel,navController: NavController ) {

    val preferences = LocalContext.current.getSharedPreferences("checkbox", Context.MODE_PRIVATE)
    val userId = preferences.getString("userId","")
    val errorMessage by viewModel.errorMessage

    Button(
        onClick = {
                  viewModel.updateInventoryForRequieredRecipe(userId!!,recipe.ingredients.toSet())
        },
        elevation = null,
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF46D42),
            contentColor = Color.White
        ), modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp , )
    ) {
        Text(text = "Start Cooking Now ", modifier = Modifier.padding(8.dp))
    }
    if(errorMessage != null)
    {
        var showDialog by remember { mutableStateOf(errorMessage != null) }
        AnimatedVisibility(
            visible = showDialog,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)) + slideInVertically(
                initialOffsetY = { it }, // Start from below the screen
                animationSpec = tween(durationMillis = 300)
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 300)) + slideOutVertically(
                targetOffsetY = { it }, // Slide down to dismiss
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            AlertDialog(
                onDismissRequest = { viewModel.clearErrorMessage() },
                content = {
                    Box(contentAlignment = Alignment.Center) {
                        // Background and content of the alert dialog
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(16.dp)
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.alert_assets),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMessage.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2F2F2F),
                                modifier = Modifier.padding(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            Row(horizontalArrangement = Arrangement.End) {
                                Button(
                                    modifier = Modifier.padding(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFFFC610F
                                        )
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    onClick = {
                                        navController.navigate("AddIngrediant") {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            //println("currentRoute is : "+item.route)
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                ) {
                                    Text("Add the needed ingredients", textAlign = TextAlign.Center)
                                }
                            }
                        }


                        Image(
                            painter = painterResource(id = R.drawable.close_icon),
                            contentDescription = "",
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(8.dp)
                                .offset(x = -10.dp, y = -160.dp)
                                .clickable {
                                    viewModel.clearErrorMessage()
                                    showDialog = false
                                }

                        )
                    }
                }
            )
        }

    }

}

@Composable
fun IngredientsList(recipe: Recipe) {
    val ingredients = recipe.ingredients


    EasyGrid(nColumns = 3, items = ingredients) { ingredientRecipe ->
        RecipeIngredientCard(recipeIngredient = ingredientRecipe)
    }
}

@Composable
fun <T> EasyGrid(nColumns: Int, items: List<T>, content: @Composable (T) -> Unit) {
    Column(Modifier.padding(16.dp)) {
        for (i in items.indices step nColumns) {
            Row {
                for (j in 0 until nColumns) {
                    if (i + j < items.size) {
                        Box(
                            contentAlignment = Alignment.TopCenter,
                            modifier = Modifier.weight(1f)
                        ) {
                            content(items[i + j])
                        }
                    } else {
                        Spacer(Modifier.weight(1f, fill = true))
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientsHeader(recipe: Recipe) {

    var selectedTab by remember { mutableStateOf("Ingredients") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            //.clip(RoundedCornerShape(8.dp))
            .background(LightGray)
            .fillMaxWidth()
            .height(44.dp)
    ) {
        TabButton(
            text = "Ingredients",
            active = selectedTab == "Ingredients",
            modifier = Modifier.weight(1f),
            onClick = { selectedTab = "Ingredients" }
        )

        TabButton(
            text = "Steps",
            active = selectedTab == "Steps",
            modifier = Modifier.weight(1f),
            onClick = { selectedTab = "Steps" }
        )

    }
      when (selectedTab) {
           "Ingredients" -> {
               // Display the Ingredients list
               IngredientsList(recipe = recipe)
           }
           "Steps" -> {

               StepsList(steps = recipe.instructions)
           }
       }
}
@Composable
fun StepsList(steps: List<String>) {

   if(steps.isEmpty())
   {
       noIngrediantSection()
   }
    else{

    Column(Modifier.padding(16.dp)) {
        steps.forEach { step ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Instruction Icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color(0xFFFC610F)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = step,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)

                )
            }
        }
    }
    }
}

@Composable
fun TabButton(text: String, active: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.fillMaxHeight(),
        elevation = null,
        colors = if (active) ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF46D42),
            contentColor = White
        ) else ButtonDefaults.buttonColors(
            containerColor = LightGray,
            contentColor = DarkGray
        )
    ) {
        Text(text)
    }
}


@Composable
fun ServingCalculator() {
    var value by remember { mutableStateOf(6) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(Shapes.medium)
            .background(LightGray)
            .padding(horizontal = 16.dp)
    ) {
        Text(text = "Serving", Modifier.weight(1f), fontWeight = FontWeight.Medium)

        CircularButton(iconResouce = R.drawable.ic_minus, color = Pink) {
            if (value > 1) value--
        }

        Text(text = "$value", Modifier.padding(16.dp), fontWeight = FontWeight.Medium)

        CircularButton(iconResouce = R.drawable.ic_plus, color = Pink) {
            value++
        }
    }
}


@Composable
fun Description(recipe: Recipe) {
    Text(
        text = recipe.description,
        fontWeight = Medium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    )
}

@Composable
fun BasicInfo(recipe: Recipe) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        InfoColumn(iconResouce = R.drawable.ic_clock, text = recipe.cookingTime)
        InfoColumn(iconResouce = R.drawable.ic_flame, text = recipe.energy)
        InfoColumn(iconResouce = R.drawable.ic_star, text = recipe.rating)
    }
}
@Composable
fun InfoColumn(@DrawableRes iconResouce: Int, text: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconResouce),
            contentDescription = null,
            tint = Pink,
            modifier = Modifier.height(24.dp)
        )
        Text(
            text = text ?: "N/A", // Default text if null
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
fun noIngrediantSection() {
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,


    ){
        Row(modifier = Modifier
            .height(150.dp).padding(vertical = 8.dp)) {
            Image(painter = painterResource(R.drawable.something_went_wrong), contentDescription = "someting went wrong", modifier = Modifier
                .width(120.dp)
                .height(120.dp))
            Column(modifier = Modifier.fillMaxHeight(),verticalArrangement = Arrangement.Center) {
                Text("Oops, Something went wrong !", fontWeight = FontWeight.Bold , fontSize = 18.sp, textAlign = TextAlign.Center)
            }
        }

    }


}