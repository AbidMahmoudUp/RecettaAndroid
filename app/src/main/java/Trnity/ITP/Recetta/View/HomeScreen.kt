import Trnity.ITP.Recetta.Model.entities.IngredientRecipe
import Trnity.ITP.Recetta.View.Components.AppDrawer
import Trnity.ITP.Recetta.View.Components.Items.sampleRecipes
import Trnity.ITP.Recetta.ViewModel.DrawerViewModel
import Trnity.ITP.Recetta.ViewModel.RecipeViewModel
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import Trnity.ITP.Recetta.R
import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch

data class CategorieHome(val image : Int, val text: String)
@Composable
fun HomeScreen( navController: NavController,viewModel: RecipeViewModel = hiltViewModel()) {

    val preferences = LocalContext.current.getSharedPreferences("checkbox", Context.MODE_PRIVATE)
    val user_id   = preferences.getString("userId","")
    Log.d("User Id Debug " , user_id.toString())
    var categoryList = listOf<CategorieHome>(
        CategorieHome(R.drawable.pizza , "Fast Food" ),
        CategorieHome(R.drawable.stroberry , "Fruits" ),
        CategorieHome(R.drawable.drinks , "Drinks" )
    )

    val recipes by viewModel.recipes.collectAsState()
    println("----------------------------------TestRecipes -----------------------------------------")
    println(recipes)
    LaunchedEffect(Unit) {
        viewModel.fetchAllRecipes()
    }
            Column(
                modifier = Modifier
                    .padding( horizontal = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                   Text("Recetta" , color = Color.Black , fontWeight = FontWeight.Bold , fontSize = 20.sp)

                    SmokingSkillet(navController)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = HighlightLastTwoWords("Simple recipes with your inventory ingredients"),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    items(categoryList){
                                        tab->
                        categorieHomeTab(category = tab, isSelected = true ) {
                            
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LazyRow(


                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    ) {
                        items(recipes) { recipe ->

                            RecipeCardWithImage(navController,
                               recipe = recipe
                            )
                        }
                    }
                }
            }
        }

@Composable

fun HighlightLastTwoWords(text: String): AnnotatedString {
    // Split the text into words
    val words = text.split(" ")
    val lastTwoWords = if (words.size > 2) words.takeLast(2).joinToString(" ") else text
    val remainingText = if (words.size > 2) words.dropLast(2).joinToString(" ") else ""

    // Build the annotated string with color for the last two words
    return buildAnnotatedString {
        append(remainingText)
        if (remainingText.isNotEmpty()) append(" ")
        withStyle(style = SpanStyle(color = Color(0xFFF46D42).copy(alpha = 0.5f))) { // Change to desired color
            append(lastTwoWords)
        }
    }
}

@Composable
fun SmokingSkillet(navController:NavController) {
    val context = LocalContext.current
    val animatedDrawable = remember {
        AppCompatResources.getDrawable(context, R.drawable.animated_skillet) as? AnimatedVectorDrawable
            ?: throw IllegalArgumentException("Drawable not found or not an AnimatedVectorDrawable")
    }

    // Start the animation
    LaunchedEffect(Unit) {
        animatedDrawable.start()
    }

    // Render the Animated Vector Drawable
    AndroidView(
        factory = { ctx ->
            ImageView(ctx).apply {
                setImageDrawable(animatedDrawable)
            }
        },
        modifier = Modifier
            .size(48.dp)
            .padding(end = 8.dp)
            .clickable {
                navController.navigate("GenerateRecipe") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true

                }
            }
    )
}
@Composable
fun categorieHomeTab(category : CategorieHome,isSelected: Boolean,  onClick: () -> Unit  ) {
    Row(horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color(0xFFF46D42), shape = RoundedCornerShape(28.dp))
            .padding(8.dp, 8.dp).width(100.dp).height(24.dp)) {

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(color = Color.White ,shape = CircleShape).padding(2.dp)
        )
           {
                Image(
                    painter = painterResource(id = category.image),
                    contentDescription = "image Categorie Home",

                )

            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = category.text,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 12.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

}

