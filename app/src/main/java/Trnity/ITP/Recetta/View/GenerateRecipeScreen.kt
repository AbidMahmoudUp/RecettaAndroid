package Trnity.ITP.Recetta.View

import Trnity.ITP.Recetta.Model.entities.IngredientRecipe
import Trnity.ITP.Recetta.ViewModel.IngredientViewModel
import Trnity.ITP.Recetta.ViewModel.RecipeViewModel
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun GenerateRecipeScreen(navController: NavController,recipeViewModel : RecipeViewModel = hiltViewModel() ,ingredientViewModel: IngredientViewModel = hiltViewModel()) {

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val ingredients by ingredientViewModel.ingredients.collectAsState()
    val categories = listOf("All", "Fruit", "Vegetables", "Meat", "Nuts")
    var selectedCategory by remember { mutableStateOf("All") }
    var searchText by remember { mutableStateOf("") }
    var listIngredientQte by remember { mutableStateOf(mutableSetOf<IngredientRecipe>()) }
    val focusManager = LocalFocusManager.current
    val isSaveButtonVisible by remember {
        derivedStateOf { listIngredientQte.isNotEmpty() }
    }
    fun doesMatchSearchQuery(ingredientName: String, query: String): Boolean {
        return ingredientName.contains(query, ignoreCase = true)
    }
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
            navigationTitle(navController,"Generate Recipe")

            if (isSaveButtonVisible) {
                val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
                val animatedColor by infiniteTransition.animateColor(
                    initialValue = Color(0xFF4A91E7),
                    targetValue = Color(0xFFB26B9C),
                    animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
                    label = "color"
                )


                Text(
                    text = "Generate",
                    modifier = Modifier.clickable {
                        Log.d("Ingredient QTE TEST", listIngredientQte.toString())
                        recipeViewModel.generateRecipes(listIngredientQte)
                    },
                    color = animatedColor ,
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
                onValueChange = { newText -> searchText = newText },

                modifier = Modifier
                    .width(screenWidth - 20.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                label = { Text("Search") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            )

            Text("Categories",modifier=Modifier.align(Alignment.Start),
                style = MaterialTheme.typography.titleMedium )
            LazyRow( modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp) )
            {
                items(categories.size)
                {index->
                    val category = categories[index]

                    CategoryTab(
                        text = category,
                        isSelected = category == selectedCategory,
                        onClick = { selectedCategory = category }
                    )
                }
            }
            val filteredIngredients = ingredients.filter { ingredient ->
                doesMatchSearchQuery(ingredient.name, searchText)
            }
            CardGridExample(
                ingredients = filteredIngredients,
                listOfIngredients = listIngredientQte,
                onIngredientsUpdated = { updatedList ->
                    listIngredientQte = updatedList.toMutableSet()
                })

            Button(modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFFFC610F))
                ,onClick = { /*TODO*/ }) {
                Text(text = "Add Ingredients")
            }
        }

    }
}