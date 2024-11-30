package Trnity.ITP.Recetta.View.StaticScreens

import Trnity.ITP.Recetta.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AboutUsScreen(navController: NavController) {
    Surface {
        Spacer(modifier = Modifier)
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                tint = Color.Blue,
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "go Back",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable {
                        navController.popBackStack()
                    }
            )
        }
        Column(
            horizontalAlignment =  Alignment.CenterHorizontally,
            verticalArrangement =  Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {






        }
    }
}