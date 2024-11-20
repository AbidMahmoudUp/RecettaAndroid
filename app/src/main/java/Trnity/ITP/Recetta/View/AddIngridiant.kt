package Trnity.ITP.Recetta.View

import Trnity.ITP.Recetta.Model.entities.Ingredient
import Trnity.ITP.Recetta.R

import Trnity.ITP.Recetta.ViewModel.IngredientViewModel
import android.annotation.SuppressLint
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun AddIngredient(navController: NavController , ingredientViewModel: IngredientViewModel = hiltViewModel()) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val ingredients by ingredientViewModel.ingredients.collectAsState()
    val categories = listOf("All", "Fruit", "Vegetables", "Meat", "Nuts")
    var selectedCategory by remember { mutableStateOf("All") }

    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

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
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
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
                text = "Add Ingredient",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp)) // Add spacing between elements

        // Center Column: Search field
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
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
            CardGridExample(ingredients)
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
            ) { onClick() } // Handle clicks
    ) {
        // Icon container
        Box(
            modifier = Modifier
                .size(60.dp) // Set container size
                .clip(RoundedCornerShape(8.dp)) // Rounded corners
                .background(if (isSelected) Color(0xFFFC610F) else Color.Transparent), // Highlight if selected
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.vegtableicon),
                contentDescription = null,
                tint = if (isSelected) Color.White else Color(0xFFFC610F), // Change icon tint
                modifier = Modifier.size(32.dp) // Set icon size
            )
        }
        // Text below the container
        Text(
            text = text,
            color = if (isSelected) Color.Black else Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp) // Add spacing between icon and text
        )
    }
}
@Composable
fun FoodCard(ingredient: Ingredient) {
    var count by remember { mutableStateOf(0) }

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
                // Image placeholder
                Spacer(Modifier.height(12.dp))
                AsyncImage(
                    model = "http://192.168.43.232:3000/uploads/"+ingredient.image, // Replace with your drawable
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Item name
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

            // Counter buttons at the bottom-right
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(0.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 8.dp, y = 8.dp)
                    .animateContentSize() // Smoothly handle size changes
            ) {
                // Show "-" button when count > 0
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
                            onClick = { if (count > 0) count-- },
                            modifier = Modifier
                                // .background(Color(0xFFFF5722), RectangleShape)
                                .clip(RoundedCornerShape(8.dp))
                                .size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_back), // Replace with your drawable
                                contentDescription = "Decrease",
                                tint = Color.White
                            )
                        }
                        Spacer(Modifier.height(7.dp))
                        // Show count
                        Text(
                            text = "$count",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 16.sp,
                            color = Color.White,
                         //   modifier = Modifier.padding(vertical = 4.dp).background(Color(0xFFFF5722))
                        )
                    }
                }

                // Always show "+" button
                Column( modifier = Modifier
                    .background(
                        Color(0xFFFF5722),
                        shape = RoundedCornerShape(topStart = 8.dp)
                    )) {


                    IconButton(
                        onClick = { count++ },
                        modifier = Modifier
                            .background(
                                color = Color(0xFFFF5722),
                                shape = if (count <= 0) RoundedCornerShape(8.dp) else RectangleShape
                            )
                            .padding(4.dp)
                            .size(24.dp) // Size of the IconButton
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.favorite), // Replace with your drawable
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
fun CardGridExample(ingredients:List<Ingredient>) {
   // val items = listOf(Ingredient("1","Tomato","","","Fruit")) // Example items

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 columns
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(ingredients.size) { item ->
            FoodCard(ingredient = ingredients[item])
        }
    }
}
