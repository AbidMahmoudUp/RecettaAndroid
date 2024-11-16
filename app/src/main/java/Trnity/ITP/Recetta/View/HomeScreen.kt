import Trnity.ITP.Recetta.View.Components.AppDrawer
import Trnity.ITP.Recetta.View.Components.Items.sampleRecipes
import Trnity.ITP.Recetta.ViewModel.DrawerViewModel
import Trnity.ITP.Recetta.ViewModel.RecipeViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen( navController: NavController,viewModel: RecipeViewModel = hiltViewModel()) {




    //val navController = rememberNavController()
    val recipes by viewModel.recipes.collectAsState()
    println("----------------------------------TestRecipes -----------------------------------------")
    println(recipes)
    LaunchedEffect(Unit) {
        viewModel.fetchAllRecipes()
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val drawerViewModel: DrawerViewModel = DrawerViewModel()
    val coroutineScope = rememberCoroutineScope()
    var bottomBarHeight by remember { mutableStateOf(0f) }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Transparent,
        drawerContent = {
            val calculatedDrawerHeight = screenHeight.value - bottomBarHeight / 3
            AppDrawer(
                drawerState = drawerState,
                items = drawerViewModel.drawerItems,
                onItemClick = { item -> navigateTo(navController, item.destination) },
                scope = coroutineScope,
                drawerHeight = calculatedDrawerHeight
            )
        },
        content = { // Main content when the drawer is closed
            Column(
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally // Center align all content in Column
            ) {
                
                

                // Top-left menu icon
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        },
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu Icon")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = HighlightLastTwoWords("Simple recipes with your inventory ingredients"),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(17.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) {
                        Button(
                            onClick = { /* TODO */ },
                            modifier = Modifier
                                .height(50.dp)
                                .width(50.dp).background(Color.White)
                                .shadow(8.dp, shape = RoundedCornerShape(0.dp)),
                            shape = RoundedCornerShape(0),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                        ) {
                            // Button content
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Centering LazyRow in Column
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center // Center content within Box
                ) {
                    LazyRow(


                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(0.dp, 0.dp, 0.dp, 0.dp) // Ensure LazyRow doesn't take full width
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
    )
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


