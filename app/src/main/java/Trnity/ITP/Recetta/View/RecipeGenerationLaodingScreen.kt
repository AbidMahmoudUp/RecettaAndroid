package Trnity.ITP.Recetta.View

import Trnity.ITP.Recetta.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Composable
fun RecipeGenerationLoadingScreen(navController: NavController) {
    val pagerState = rememberPagerState(pageCount = { 4 }) // Assuming you have 4 images

    // Automatically scroll the images every 3 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // Delay for 3 seconds
            val nextPage = (pagerState.currentPage + 1) % 4 // Loop back to the first image
            pagerState.animateScrollToPage(nextPage)
        }
    }
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(key1 = pagerState.currentPage) {
        // Simulating recipe generation completion after 12 seconds (4 pages * 3 seconds)
        while (progress < 1f) {
            delay(100) // Delay for 100ms (this gives a 10fps "tick")
            progress += 0.01f // Increase progress by 1% (0.01)
            if (progress > 1f) progress = 1f // Ensure it doesn't exceed 100%
        }
        navController.navigate("GeneratedList"){
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            //println("currentRoute is : "+item.route)
            launchSingleTop = true
            restoreState = true
        
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Horizontal Pager for images
        HorizontalPager(state = pagerState) { page ->
            Card(
                modifier = Modifier
                    .fillMaxSize() // Full screen size for the images
                    .graphicsLayer {
                        // Calculate the absolute offset for the current page from the scroll position
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                ).absoluteValue

                        // Animate the opacity of the card as it slides
                        alpha = lerp(start = 0.5f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f))
                    }
            ) {
                // Here you load the image for each page
                Image(
                    painter = painterResource(id = getImageResourceForPage(page)),
                    contentDescription = "Image $page",
                    modifier = Modifier.fillMaxSize() // Make sure image fills the entire screen
                )
            }
        }

        // Top Progress Bar (above the pager)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter), // Align it to the top center
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Loading Recipes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Linear progress bar based on the current page
            LinearProgressIndicator(
                progress = progress, // Progress from 0 to 1
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp) // Set the height for the progress bar
                    .padding(top = 8.dp),
                color = Color.Yellow
            )
        }
    }
}

// Helper function to get the image resource for each page
fun getImageResourceForPage(page: Int): Int {
    return when (page) {
        0 -> R.drawable.image1
        1 -> R.drawable.image2
        2 -> R.drawable.image3
        3 -> R.drawable.image4
        else -> R.drawable.image1 // Default case
    }
}