package Trnity.ITP.Recetta.View


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
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlin.math.max
import kotlin.math.min


@Composable
fun RecipeScreen(recipeId: String ,viewModel: RecipeViewModel = hiltViewModel()) {

    LaunchedEffect(recipeId) {
        viewModel.fetchRecipe(recipeId)
    }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val scrollState = rememberLazyListState()
    val recipe = viewModel.recipe.collectAsState().value

    println("--------------------------------------- TestRecipeScreen ------------------------------------------------------")
    println(recipe)
    Box(Modifier.padding(0.dp,0.dp,0.dp,80.dp)) {
        Content(recipe, scrollState)
        ParallaxToolbar(recipe, scrollState)
    }
}
// Compose the given content with a drop shadow on all
// non-transparent pixels
@Composable fun Shadowed(modifier: Modifier, color: Color, offsetX: Dp, offsetY: Dp, blurRadius: Dp, content: @Composable () -> Unit) {
    val density = LocalDensity.current
    val offsetXPx = with(density) { offsetX.toPx() }.toInt()
    val offsetYPx = with(density) { offsetY.toPx() }.toInt()
    val blurRadiusPx = kotlin.math.ceil(with(density) {
        blurRadius.toPx()
    }).toInt()

    // Modifier to render the content in the shadow color, then
    // blur it by blurRadius
    val shadowModifier = Modifier
        .drawWithContent {
            val matrix = shadowColorMatrix(color)
            val filter = ColorFilter.colorMatrix(matrix)
            val paint = Paint().apply {
                colorFilter = filter
            }
            drawIntoCanvas { canvas ->
                canvas.saveLayer(Rect(0f, 0f, size.width, size.height), paint)
                drawContent()
                canvas.restore()
            }
        }
        .blur(radius = blurRadius, BlurredEdgeTreatment.Unbounded)
        .padding(all = blurRadius) // Pad to prevent clipping blur

    // Layout based solely on the content, placing shadow behind it
    Layout(modifier = modifier, content = {
        // measurables[0] = content, measurables[1] = shadow
        content()
        Box(modifier = shadowModifier) { content() }
    }) { measurables, constraints ->
        // Allow shadow to go beyond bounds without affecting layout
        val contentPlaceable = measurables[0].measure(constraints)
        val shadowPlaceable = measurables[1].measure(Constraints(maxWidth = contentPlaceable.width + blurRadiusPx * 2, maxHeight = contentPlaceable.height + blurRadiusPx * 2))
        layout(width = contentPlaceable.width, height = contentPlaceable.height) {
            shadowPlaceable.placeRelative(x = offsetXPx - blurRadiusPx, y = offsetYPx - blurRadiusPx)
            contentPlaceable.placeRelative(x = 0, y = 0)
        }
    }
}

// Return a color matrix with which to paint our content
// as a shadow of the given color
private fun shadowColorMatrix(color: Color): ColorMatrix {
    return ColorMatrix().apply {
        set(0, 0, 0f) // Do not preserve original R
        set(1, 1, 0f) // Do not preserve original G
        set(2, 2, 0f) // Do not preserve original B

        set(0, 4, color.red * 255) // Use given color's R
        set(1, 4, color.green * 255) // Use given color's G
        set(2, 4, color.blue * 255) // Use given color's B
        set(3, 3, color.alpha) // Multiply by given color's alpha
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParallaxToolbar(recipe: Recipe, scrollState: LazyListState) {
    val directImageUrl = recipe.imageRecipe.replace("https://drive.google.com/file/d/", "https://drive.google.com/uc?export=download&id=")
        .replace("/view?usp=drive_link", "")
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
            AsyncImage(
                model = directImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

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
                    text = recipe.category ?: "Category", // Default if category is null
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
                    text = recipe.title ?: "Recipe Title", // Default if title is null
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(horizontal = 16.dp) // Space between icons and title
                        .scale(1f - 0.15f * offsetProgress)
                )

                IconButton(onClick = { /* Action */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_favorite),
                        contentDescription = null,
                        tint = Gray
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
fun Content(recipe: Recipe, scrollState: LazyListState) {
    LazyColumn(contentPadding = PaddingValues(top = AppBarExpendedHeight), state = scrollState) {
        item {
            BasicInfo(recipe)
            Description(recipe)
            ServingCalculator()
            IngredientsHeader()
            IngredientsList(recipe)
            ShoppingListButton()
            Reviews(recipe)
            Images()
        }
    }
}

@Composable
fun Images() {
    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
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
fun Reviews(recipe: Recipe) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Reviews", fontWeight = Bold)
          //  Text(recipe.reviews, color = DarkGray)
        }
        Button(
            onClick = { /*TODO*/ }, elevation = null, colors = ButtonDefaults.buttonColors(
                containerColor = Transparent, contentColor = Pink
            )
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

@Composable
fun ShoppingListButton() {
    Button(
        onClick = { /*TODO*/ },
        elevation = null,
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = LightGray,
            contentColor = Color.Black
        ), modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Add to shopping list", modifier = Modifier.padding(8.dp))
    }

}

@Composable
fun IngredientsList(recipe: Recipe) {
    val ingredients = recipe.ingredients

    // Display a grid of RecipeIngredientCard for each ingredient
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
fun IngredientCard(
    iconResource: Int?,
    title: String?,
    subtitle: String?,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(bottom = 16.dp)
    ) {
        Card(
            shape = Shapes.large,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .padding(bottom = 8.dp)
                .background(LightGray)
        ) {
            Image(
                painter = painterResource(id = iconResource ?: R.drawable.food_image), // Default image if null
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
        }
        Text(
            text = title ?: "Unknown Ingredient", // Default title if null
            modifier = Modifier.width(100.dp),
            fontSize = 14.sp,
            fontWeight = Medium
        )
        Text(
            text = subtitle ?: "No category", // Default subtitle if null
            color = DarkGray,
            modifier = Modifier.width(100.dp),
            fontSize = 14.sp
        )
    }
}

@Composable
fun IngredientsHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clip(Shapes.medium)
            .background(LightGray)
            .fillMaxWidth()
            .height(44.dp)
    ) {
        TabButton("Ingredients", true, Modifier.weight(1f))
        TabButton("Tools", false, Modifier.weight(1f))
        TabButton("Steps", false, Modifier.weight(1f))
    }
}

@Composable
fun TabButton(text: String, active: Boolean, modifier: Modifier) {
    Button(
        onClick = { /*TODO*/ },
        shape = Shapes.medium,
        modifier = modifier.fillMaxHeight(),
        elevation = null,
        colors = if (active) ButtonDefaults.buttonColors(
            containerColor = Pink,
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