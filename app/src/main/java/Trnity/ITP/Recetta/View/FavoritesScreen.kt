package Trnity.ITP.Recetta.View

import Trnity.ITP.Recetta.View.Components.CardItemFavorite

import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }



@Composable
fun FavoriteScreen(navController: NavController)
{
    CardsList(6)
}

@Composable
fun CardsList(size : Int)
{
    LazyVerticalGrid(columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(size) {
            CardItemFavorite()
        }
    }
}


