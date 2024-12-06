package Trnity.ITP.Recetta.View


import Trnity.ITP.Recetta.Data.Local.RecipeEntity
import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.ViewModel.RecipeViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }



@Composable
fun FavoriteScreen(navController: NavController, recipeViewModel: RecipeViewModel = hiltViewModel()) {
    val favorites by recipeViewModel.favoriteRecipes.collectAsState()

    LaunchedEffect(Unit) {
        // Load favorites from Room database when the screen is first loaded
        recipeViewModel.loadFavorites()
    }

    CardsList(favorites, recipeViewModel) // Pass recipeViewModel to CardsList
}



@Composable
fun CardsList(favorites: List<RecipeEntity>, recipeViewModel: RecipeViewModel) {
    val categories = arrayOf("Meals", "Drinks", "Breakfast", "Launch", "Dinner")

    Column(modifier = Modifier
        .safeDrawingPadding()
        .padding(bottom = 84.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF039be5)
                )
            }
            Text(text = "Favorite List")
        }

        // Category buttons (you can keep this logic as it is)
        LazyRow(modifier = Modifier
            .wrapContentSize()
            .padding(8.dp)) {
            items(categories.size) {
                Button(onClick = {}, modifier = Modifier.padding(horizontal = 4.dp), colors = ButtonDefaults.buttonColors(Color(0xFFEDE7F3)), shape = RoundedCornerShape(8.dp)) {
                    Text(text = categories[it], color = Color.Black, fontWeight = FontWeight.Light, fontSize = 10.sp)
                }
            }
        }

        // Displaying favorites from Room database
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(favorites.size) { index ->
                CardItemFavorite(favorites[index], recipeViewModel) // Pass recipeViewModel here
            }
        }
    }
}

@Composable
fun CardItemFavorite(favorite: RecipeEntity, recipeViewModel: RecipeViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    ElevatedCard(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(8.dp)) {
        SingleItemFavCard(
            favorite = favorite,
            onHeartClicked = {
                // Show confirmation dialog on heart icon toggle
                showDialog = true
            }
        )
    }

    // Confirmation Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Remove from favorites?") },
            text = { Text("Are you sure you want to remove this recipe from your favorites?") },
            confirmButton = {
                Button(onClick = {
                    // Call the onFavoriteToggle function to remove the recipe from favorites
                    recipeViewModel.toggleFavoriteWithConfirmation(favorite)
                    showDialog = false

                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
fun SingleItemFavCard(favorite: RecipeEntity, onHeartClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onHeartClicked, modifier = Modifier.align(Alignment.End)) {
            Icon(
                Icons.Outlined.FavoriteBorder,
                "Favorite",
                tint = Color(0xFFF96115)
            )
        }

        Image(
            painter = painterResource(R.drawable.spaghetti), // Replace with dynamic image URL if needed
            contentDescription = "Recipe Image",
            modifier = Modifier.padding(8.dp)
        )

        Text(text = favorite.title, fontWeight = FontWeight.SemiBold)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextWithIcons(R.drawable.clock, "20 min", 0xFF06402B)
            TextWithIcons(R.drawable.star_unfilled, "5.0", 0xFFF96115)
        }

        Text(
            text = favorite.description,
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 4
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextWithIcons(R.drawable.kcal, "630 Kcal", 0xFFF96115)
            IconButton(onClick = onHeartClicked) {
                Icon(Icons.Outlined.KeyboardArrowRight, "")
            }
        }
    }
}


@Composable
fun TextWithIcons(imageRes : Int, textContent: String = "", iconTint: Long = 0xFF000000)
{
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(imageRes), contentDescription = "", tint = Color(iconTint), modifier = Modifier.size(18.dp))
        Text(text = textContent, fontSize = 10.sp)
    }
}

