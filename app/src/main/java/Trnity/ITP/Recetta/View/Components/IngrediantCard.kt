package Trnity.ITP.Recetta.View.Components

import Trnity.ITP.Recetta.Model.entities.IngredientInventory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.foundation.combinedClickable
@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IngrediantInventoryCard(
    ingredient: IngredientInventory,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onLongClick: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    quantity: Int
) {
    Row(
        modifier = Modifier
            .padding(0.dp, 8.dp, 4.dp, 4.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    if (isSelectionMode) {
                        if (isSelected) {
                            onDecrement()  // Deselect
                        } else {
                            onLongClick()  // Select
                        }
                    }
                },
                onLongClick = { onLongClick() } // Trigger selection mode on long click
            )
    )
 {
        AsyncImage(
            model = "http://192.168.1.17:3000/uploads/" + ingredient.ingredient.image,
            contentDescription = "Ingredient Image",
            modifier = Modifier
                .padding(12.dp)
                .size(70.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 16.dp, 4.dp, 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    ingredient.ingredient.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) Color(0xFFF46D42) else Color.Black
                )

                if (isSelectionMode) {
                    // When in selection mode, show increment and decrement buttons
                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onDecrement) { Text("-") }
                        Text(quantity.toString(), style = MaterialTheme.typography.bodyMedium)
                        IconButton(onClick = onIncrement) { Text("+") }
                    }
                } else {
                    // When not in selection mode, show the item count
                    Text(text = "items: ${ingredient.qte}", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Only display this row when not in selection mode
            if (!isSelectionMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        ingredient.ingredient.categorie,
                        color = Color(0xFF06402B),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ENGLISH).format(LocalDate.now()),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
