package Trnity.ITP.Recetta.View


import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.View.Components.Items.HomeCardItem
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min


@Composable
fun RecipeScreen(recipe: Recipe) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val scrollState = rememberLazyListState()


    Box {
        Content(recipe, scrollState)
        ParallaxToolbar(recipe, scrollState)
    }
}

@Composable
fun TopBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Transparent) // Ensure background is transparent for layering
    ) {
        IconButton(onClick = { /* Back action */ }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

    }
}

@Composable
fun RecipeImage(imageRes: Int,modifier: Modifier = Modifier) {
    Shadowed(
        modifier = modifier.size(150.dp), // Set the size as needed
        color = Color.Black.copy(alpha = 0.05f), // Shadow color with transparency
        offsetX = -2.dp,
        offsetY = 4.dp,
        blurRadius = 2.dp
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape) // Circle shape for the image
                .background(Color.White) // Background to enhance the shadow effect
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = "Recipe Image",
                modifier = Modifier
                    .fillMaxSize() // Make the image fill the Box
                    .clip(CircleShape), // Clip again to ensure it stays circular
                contentScale = ContentScale.Crop
            )
        }
    }
}
@Composable
fun RecipeTitleSection(title: String) {
    Column {
        Text(title, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        //  Text("Servings: 2", style = MaterialTheme.typography.bodyMedium)
    }
}
@Composable
fun RecipeDescriptionSection(description: String) {
    Text(description, style = MaterialTheme.typography.bodySmall)
}
@Composable
fun RecipeTags() {
    Row {
        listOf("Chinese", "Main", "Spicy").forEach { tag ->
            Chip(tag)
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
fun Chip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun IngredientsSection() {
    Column {
        Text("Ingredients", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Noodles", "Prawns", "Spices").forEach { ingredient ->
                IngredientCardDetails(ingredient)
            }
        }
    }
}

@Composable
fun IngredientCardDetails(ingredient: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(60.dp)
    ) {
        Icon(Icons.Default.Star, contentDescription = "Ingredient Icon") // Replace with actual ingredient image
        Text(ingredient, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun RecipeInstructions() {
    Column {
        Text("Instructions", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
        Step("Step 1: Boil water and add the noodles.")
        Step("Step 2: Add prawns and spices.")
    }
}

@Composable
fun Step(instruction: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Check, contentDescription = "Step Icon")
        Spacer(modifier = Modifier.width(8.dp))
        Text(instruction, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun ActionButtons() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = { /* Add to plan */ }) {
            Text("Add to Plan")
        }
        Button(onClick = { /* Start Cooking */ }) {
            Text("Start Cooking")
        }
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
    val imageHeight = AppBarExpendedHeight - AppBarCollapsedHeight

    val maxOffset = with(LocalDensity.current) { imageHeight.roundToPx() }
    val offset = min(scrollState.firstVisibleItemScrollOffset, maxOffset)
    val offsetProgress = max(0f, offset * 3f - 2f * maxOffset) / maxOffset

    // TopAppBar with title and navigation icons only
    TopAppBar(
        modifier = Modifier
            .height(AppBarExpendedHeight)
            .offset { IntOffset(x = 0, y = -offset) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = White),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        title = {
            Text(
                text = recipe.title ?: "Recipe Title", // Use default if title is null
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = (16 + 28 * offsetProgress).dp)
                    .scale(1f - 0.25f * offsetProgress)
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* Action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = Gray
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_favorite),
                    contentDescription = null,
                    tint = Gray
                )
            }
        }
    )

    // Separate content for the parallax effect and additional layout elements
    Column(
        Modifier
            .height(imageHeight)
            .graphicsLayer { alpha = 1f - offsetProgress }
            .offset { IntOffset(x = 0, y = -offset) } // Parallax effect
    ) {
        Image(
            painter = painterResource(id = R.drawable.strawberry_pie_1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Transparent, White),
                        startY = 0.4f,
                        endY = 1f
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = recipe.category ?: "Category", // Use default if category is null
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clip(Shapes.small)
                    .background(LightGray)
                    .padding(vertical = 6.dp, horizontal = 16.dp)
            )
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
    // Use an empty list if ingredients is null
    EasyGrid(nColumns = 3, items = recipe.ingredients ?: emptyList()) {
        IngredientCard(
            iconResource = R.drawable.food_image, // Provide a default image if null
            title = it.name ?: "Unknown",  // Default title if null
            subtitle = it.categorie ?: "Uncategorized", // Default subtitle if null
            modifier = Modifier
        )
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