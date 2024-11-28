import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.Utils.Shadowed

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

import com.google.gson.Gson

@Composable
fun RecipeCardWithImage(navController: NavController, recipe : Recipe) {
    println("RecipeCardWithImage: recipe = $recipe")

    val directImageUrl = recipe.imageRecipe.replace("https://drive.google.com/file/d/", "https://drive.google.com/uc?export=download&id=")
        .replace("/view?usp=drive_link", "")
    fun navigateToDetails(recipeId: String) {
      //  val jsonRecipe = Gson().toJson(recipe)
        navController.navigate("recipeScreen/$recipeId"){
            popUpTo(navController.graph.startDestinationId) { saveState = true }
            //println("currentRoute is : "+item.route)
            launchSingleTop = true
            restoreState = true
        }
    }

    // Outer container with shadow for the card
    Shadowed(
        modifier = Modifier
            .width(270.dp)
            .height(400.dp),
        color = Color.Black.copy(alpha = 0.1f),
        offsetX = -2.dp,
        offsetY = 4.dp,
        blurRadius = 16.dp
    ) {
        Box(
            modifier = Modifier
                .width(270.dp)
                .padding(16.dp)
        ) {
            // Inner container with card content
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .align(Alignment.Center)
                    .padding(top = 40.dp, start = 16.dp) // Offset card within the box
                    .clip(RoundedCornerShape(16.dp)), // Rounded corners for the card
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEC5AA))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(70.dp))

                    Text(
                        text = recipe.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                    Text(
                        text = recipe.description,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 4,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconWithText(icon = R.drawable.ic_time, text = "60 min")
                        IconWithText(icon = R.drawable.ic_weight, text = "1.5 kg")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {


                            navigateToDetails(recipe.id)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF46D42)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "View Recipe", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // "Launch" text positioned on top-left of the card
            val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
            val animatedColor by infiniteTransition.animateColor(
                initialValue = Color(0xFF60DDAD),
                targetValue = Color(0xFF4285F4),
                animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
                label = "color"
            )

            Text(
                text = "Launch",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = animatedColor,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 36.dp, y = 90.dp) // Adjust offset as desired
            )

            // Image positioned in front of the card at the top-end with shadow
            Shadowed(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (0).dp, y = (-0).dp), // Adjusted offset to position the image
                color = Color.Black.copy(alpha = 0.1f),
                offsetX = -2.dp,
                offsetY = 4.dp,
                blurRadius = 16.dp
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color.White, CircleShape)
                ) {
                    AsyncImage(
                       model = directImageUrl,
                        contentDescription = "Recipe Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
fun IconWithText(icon: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = icon), // Replace with actual icon resource
            contentDescription = text,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, color = Color.Gray, fontSize = 12.sp)
    }
}

