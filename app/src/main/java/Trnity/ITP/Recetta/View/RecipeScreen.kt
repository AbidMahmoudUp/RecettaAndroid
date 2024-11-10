package Trnity.ITP.Recetta.View

import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.View.Components.Items.HomeCardItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.lang.Math.ceil
import java.lang.reflect.Array.set


@Composable
fun RecipeScreen(recipe: HomeCardItem) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            //  .background(Color.Transparent)

            .background(Color(0xFFFEC5AA))
    ) {
        // Main content background
        Column(
            modifier = Modifier
                .width(screenWidth)
                .height(screenHeight)
                .padding(top = 70.dp) // Adjust padding to leave space for the image and top bar
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)

        ) {
            Spacer(modifier = Modifier.height(64.dp))
            RecipeTitleSection(recipe.title)
            Spacer(modifier = Modifier.height(16.dp))
            RecipeDescriptionSection(description = recipe.description)
            Spacer(modifier = Modifier.height(16.dp))
            RecipeTags()
            Spacer(modifier = Modifier.height(16.dp))
            IngredientsSection()
            Spacer(modifier = Modifier.height(16.dp))
            RecipeInstructions()
            Spacer(modifier = Modifier.height(16.dp))
            ActionButtons()
        }

        // Top Bar with Back Button on the Top Left - overlayed
        TopBar()

        // Recipe Image positioned at the Top Right - overlayed
        RecipeImage(
            recipe.imageRes,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding() // Adjust padding if needed
        )
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
