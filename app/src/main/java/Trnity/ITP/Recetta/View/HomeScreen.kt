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
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun HomeScreen( navController: NavController,viewModel: RecipeViewModel = hiltViewModel()) {

    val focusManager = LocalFocusManager.current
    var selectedCategory by remember { mutableStateOf<CategorieHome?>(null) } // State for selected category

    val preferences = LocalContext.current.getSharedPreferences("checkbox", Context.MODE_PRIVATE)
    val user_id   = preferences.getString("userId","")
    Log.d("User Id Debug " , user_id.toString())
    var categoryList = listOf<CategorieHome>(
        CategorieHome(R.drawable.pizza , "Fast Food" ),
        CategorieHome(R.drawable.main_course , "Main Course" ),
        CategorieHome(R.drawable.drinks , "Drinks" )
    )
    var searchText by remember { mutableStateOf("") }

    val recipes by viewModel.recipes.collectAsState()
    println("----------------------------------TestRecipes -----------------------------------------")
    println(recipes)
    val filteredRecipes = recipes.filter {
        it.title.startsWith(searchText, ignoreCase = true)  &&(selectedCategory == null || it.category == selectedCategory?.text)
    }
    LaunchedEffect(Unit) {
        viewModel.fetchAllRecipes()
    }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        focusManager.clearFocus() // Clear focus when tapping outside
                    },
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp,horizontal = 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                   Text("Recetta" , color = Color.Black , fontWeight = FontWeight.Bold , fontSize = 20.sp)

                    SmokingSkillet(navController)
                }

                Text(
                    text = HighlightLastTwoWords("Simple recipes with your inventory ingredients"),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(8.dp))

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
                            tint = Color.Black // Icon color
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent, // Remove background when focused
                        unfocusedContainerColor = Color.Transparent, // Remove background when unfocused
                        disabledContainerColor = Color.Transparent, // Remove background when disabled
                        focusedTextColor = Color.Black, // Text color when focused
                        unfocusedTextColor = Color.Black, // Text color when unfocused
                        disabledTextColor = Color.Gray, // Text color when disabled
                        focusedLabelColor = Color(0xFFF46D42), // Label color when focused
                        unfocusedLabelColor = Color.Black, // Label color when unfocused
                        focusedIndicatorColor = Color(0xFFF46D42), // Border color when focused
                        unfocusedIndicatorColor = Color.Black, // Border color when unfocused
                        disabledIndicatorColor = Color.Gray, // Border color when disabled
                        cursorColor = Color(0xFFF46D42) // Cursor color
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    items(categoryList){ category ->
                        categorieHomeTab(category = category, isSelected = selectedCategory == category) {
                            selectedCategory = if (selectedCategory == category) null else category // Toggle selection
                        }
                    }
                }


                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if(filteredRecipes.isEmpty())
                    {
                        noSearchResult()
                    }
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,

                        modifier = Modifier
                            .wrapContentWidth()
                    ) {
                        items(filteredRecipes) { recipe ->

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
            .clickable (){
                navController.navigate("GenerateRecipe") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true

                }
            }
    )
}
@Composable
fun categorieHomeTab(
    category: CategorieHome,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                if (isSelected) Color(0xFFF46D42) else Color(0x50F46D42),
                shape = RoundedCornerShape(28.dp)
            )
            .clickable(onClick = onClick) // Trigger onClick
            .padding(8.dp, 8.dp)
            .width(100.dp)
            .height(24.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(color = Color.White, shape = CircleShape)
                .padding(2.dp)
        ) {
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

@Composable
fun noSearchResult() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement= Arrangement.Center,
    ) {

        Column( modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement= Arrangement.Center,) {


            Image(
                painter = painterResource(R.drawable.search_not_found),
                contentDescription = "Something went wrong",
                modifier = Modifier
                    .width(240.dp)
                    .height(293.dp)
                    .offset(y=-70.dp)
            )

            Text(
                "No results match your search",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth().offset(y=-70.dp),
                textAlign = TextAlign.Center
            )



        }
    }
}