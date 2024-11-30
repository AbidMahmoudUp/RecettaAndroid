package Trnity.ITP.Recetta.View.Components

import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.View.pxToDp
import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun CardItemFavorite()
{
    var scale = 4;
    var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }

    val configuration = LocalConfiguration.current

// If our configuration changes then this will launch a new coroutine scope for it
    LaunchedEffect(configuration) {
        // Save any changes to the orientation value on the configuration object
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }

    when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            scale = 2
        }
        else -> {
            scale = 4
        }
    }

    var width by remember {
        mutableIntStateOf(0)
    }
    ElevatedCard(shape = RoundedCornerShape(16.dp),elevation = CardDefaults.cardElevation(0.3.dp),
        modifier = Modifier.onGloballyPositioned { cordinates ->
            width = cordinates.size.width
            println(width / 440)
            val height = cordinates.size.height.toFloat()
        }) {
        SingleItemFavCard(width = (width / scale) - 20)
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun SingleItemFavCard(width: Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton({}, modifier = Modifier.align(Alignment.End).padding(8.dp),) {
            Icon(
                Icons.Outlined.FavoriteBorder,
                "Favorite",

                tint = Color(0xFFF96115)
            )
        }


        Image(
            painter = painterResource(R.drawable.spaghetti),
            ""
        )


        Text("Spaghetti", modifier = Modifier.padding(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(painter = painterResource(R.drawable.clock), "", tint = Color(0xFF06402B))
            Text("20 min")
            Icon(painter = painterResource(R.drawable.star_unfilled), "", tint = Color(0xFFF96115))
            Text("5.0")
        }

        Text(
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
            modifier = Modifier.padding(8.dp),

        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(painter = painterResource(R.drawable.kcal), "", tint = Color(0xFFF96115))
            Text("630 Kcal")
            Spacer(Modifier.width(width = width.pxToDp()))
            IconButton({}, modifier = Modifier.align(alignment = Alignment.CenterVertically).padding(0.dp).height(16.dp).clickable(interactionSource = MutableInteractionSource(),indication = null) {

            }) {
                Icon(Icons.Outlined.KeyboardArrowRight, "")
                println(width.pxToDp())
            }

        }


    }
}

