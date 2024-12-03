package Trnity.ITP.Recetta.View

import Trnity.ITP.Recetta.Model.entities.Ingredient
import Trnity.ITP.Recetta.Model.entities.IngredientRecipe
import Trnity.ITP.Recetta.R

import Trnity.ITP.Recetta.ViewModel.IngredientViewModel
import Trnity.ITP.Recetta.ViewModel.InventoryViewModel
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun navigationTitle(navController : NavController , title : String ){
    Row( horizontalArrangement = Arrangement.Center , verticalAlignment =Alignment.CenterVertically ){IconButton(
        onClick = {
            navController.popBackStack() // Navigate back
        }
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color(0xFF039BE5)
        )
    }
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(start = 8.dp)
    )
}}
@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun AddIngredient(navController: NavController ,inventoryViewModel: InventoryViewModel = hiltViewModel() ,ingredientViewModel: IngredientViewModel = hiltViewModel()) {
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
    val preferences = LocalContext.current.getSharedPreferences("checkbox", Context.MODE_PRIVATE)

    val userId = preferences.getString("userId","").toString()
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
            navigationTitle(navController,"Add Ingredient")

            if (isSaveButtonVisible) {
                Text(
                    text = "Save",
                    modifier = Modifier.clickable {
                        Log.d("Ingredient QTE TEST", listIngredientQte.toString())
                        inventoryViewModel.updateInventory(userId,listIngredientQte)
                    },
                    color = MaterialTheme.colorScheme.primary
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
@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun CategoryTab(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) { onClick() }
    ) {

        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) Color(0xFFFC610F) else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.vegtableicon),
                contentDescription = null,
                tint = if (isSelected) Color.White else Color(0xFFFC610F),
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = text,
            color = if (isSelected) Color.Black else Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
@SuppressLint("SuspiciousIndentation")
@Composable
fun FoodCard(ingredient: Ingredient , listOfIngredients :MutableSet<IngredientRecipe> = mutableSetOf<IngredientRecipe>() ,  onIngredientsUpdated: (MutableSet<IngredientRecipe>) -> Unit) {
    val currentIngredientRecipe = listOfIngredients.find { it.ingredient == ingredient }
    var count by remember { mutableStateOf(currentIngredientRecipe?.qte ?: 0) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .border(1.dp, Color.White, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {

                Spacer(Modifier.height(12.dp))
                AsyncImage(
                    model = "http://192.168.43.232:3000/uploads/"+ingredient.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = ingredient.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = ingredient.categorie,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 16.sp,
                    color = Color(0xFF06402B)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(0.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 8.dp, y = 8.dp)
                    .animateContentSize()
            ) {
                AnimatedVisibility(visible = count > 0) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(
                                Color(0xFFFF5722),
                                shape = RoundedCornerShape(topStart = 8.dp)
                            )

                            .padding(4.dp)
                    ) {
                        IconButton(
                            onClick = {
                                if (count > 0) {
                                    count--
                                    val updatedList = listOfIngredients.toMutableSet()
                                    if (count > 0) {
                                        updatedList.add(IngredientRecipe(ingredient, count))
                                    } else {
                                        updatedList.removeIf { it.ingredient == ingredient }
                                    }
                                    onIngredientsUpdated(updatedList)



                                }  },
                            modifier = Modifier

                                .clip(RoundedCornerShape(8.dp))
                                .size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_minus),
                                contentDescription = "Decrease",
                                tint = Color.White
                            )
                        }
                        Spacer(Modifier.height(7.dp))
                        Text(
                            text = "$count",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 16.sp,
                            color = Color.White,
                        )
                    }
                }

                Column( modifier = Modifier
                    .background(
                        Color(0xFFFF5722),
                        shape = RoundedCornerShape(topStart = 8.dp)
                    )) {


                    IconButton(
                        onClick = { count++

                            val updatedList = listOfIngredients.toMutableSet()
                            var index = updatedList.indexOf(IngredientRecipe(ingredient, count))
                            if(index != -1)
                            {
                                updatedList.elementAt(index).qte = count
                            }
                            else{
                                updatedList.add(IngredientRecipe(ingredient, count))
                                onIngredientsUpdated(updatedList)

                            }

                                  },
                        modifier = Modifier
                            .background(
                                color = Color(0xFFFF5722),
                                shape = if (count <= 0) RoundedCornerShape(8.dp) else RectangleShape
                            )
                            .padding(4.dp)
                            .size(24.dp)
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.ic_plus), // Replace with your drawable
                            contentDescription = "Increase",
                            tint = Color.White
                        )
                    }
                }


            }
        }
    }
}

@Composable
fun CardGridExample(ingredients:List<Ingredient> , listOfIngredients: MutableSet<IngredientRecipe>,onIngredientsUpdated: (MutableSet<IngredientRecipe>) -> Unit) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 64.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(ingredients.size) { item ->
            FoodCard(ingredient = ingredients[item] ,
                listOfIngredients,
                onIngredientsUpdated)
        }
    }
}
