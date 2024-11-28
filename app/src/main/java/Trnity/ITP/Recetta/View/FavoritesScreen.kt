package Trnity.ITP.Recetta.View


import Trnity.ITP.Recetta.R
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }



@Composable
fun FavoriteScreen(navController: NavController)
{
    CardsList()
}



@Composable
fun CardsList()
{

    val categories = arrayOf("Meals","Drinks","Breakfast","Launch","Dinner")

    Column(modifier = Modifier.safeDrawingPadding().padding(bottom = 84.dp )) {
        Row (verticalAlignment = Alignment.CenterVertically){
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF039be5)
                )
            }
            Text(text = "Favorite List")
        }
        LazyRow(modifier = Modifier.wrapContentSize().padding(8.dp)) {
            items(categories.size)
            {
                Button(onClick = {}, modifier = Modifier.padding(horizontal = 4.dp), colors = ButtonDefaults.buttonColors(Color(0xFFEDE7F3)) , shape = RoundedCornerShape(8.dp)) {
                    Text(text = categories[it], color = Color.Black, fontWeight = FontWeight.Light, fontSize = 10.sp)
                }
            }
        }

        LazyVerticalGrid(columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(6) {
                CardItemFavorite()
            }
        }
    }
}

@Composable
fun CardItemFavorite()
{

    ElevatedCard(shape = RoundedCornerShape(16.dp),elevation = CardDefaults.cardElevation(8.dp),) {
        SingleItemFavCard()
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun SingleItemFavCard() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton({}, modifier = Modifier.align(Alignment.End)) {
            Icon(
                Icons.Outlined.FavoriteBorder,
                "Favorite",

                tint = Color(0xFFF96115)
            )
        }


        Image(
            painter = painterResource(R.drawable.spaghetti),
            "",
            modifier = Modifier.padding(8.dp)
        )


        Text(text = "Spaghetti", fontWeight = FontWeight.SemiBold)

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            TextWithIcons(R.drawable.clock, "20 min", 0xFF06402B)
            TextWithIcons(R.drawable.star_unfilled, "5.0", 0xFFF96115)
        }

        Text(
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 4
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            TextWithIcons(R.drawable.kcal, "630 Kcal", 0xFFF96115)
            IconButton({}, modifier = Modifier.align(alignment = Alignment.CenterVertically).padding(0.dp).height(16.dp).clickable(interactionSource = MutableInteractionSource(),indication = null) {

            }) {
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

