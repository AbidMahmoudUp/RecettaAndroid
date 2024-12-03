


import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.ViewModel.RecipeViewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@Composable
fun GeneratedRecipeListScreen(navController: NavController , recipes : List<Recipe>)
{

    Log.d("Recipes Gotten From the Ai" , recipes.toString())
    Surface {
        LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(4.dp), verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)){
            items(recipes.size) {
                    item ->
                ElevatedCard(modifier = Modifier.padding(8.dp), elevation = CardDefaults.cardElevation(8.dp)) {
                    Image(painter = painterResource(R.drawable.hamburger),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(
                                RoundedCornerShape(8.dp)
                            ))
                    Column(modifier = Modifier.padding(4.dp)) {
                        Text(
                            text = recipes[item].title,
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextWithIcons(R.drawable.clock, "1h 15 min", 0xFF06402B)
                            TextWithIcons(R.drawable.kcal, "600 kcal", 0xFFF96115)
                        }
                    }
                    Button(onClick = {},
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFC610F)),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                            .padding(start = 8.dp, bottom = 8.dp, end = 8.dp)) {
                        Text(text = "Details", fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                    }
                }
            }
        }
    }
}


@Composable
fun TextWithIcons(imageRes : Int, textContent: String, iconTint: Long = 0xFF000000)
{
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(imageRes), contentDescription = "", tint = Color(iconTint), modifier = Modifier.size(18.dp))
        Text(text = textContent, fontSize = 10.sp)
    }
}