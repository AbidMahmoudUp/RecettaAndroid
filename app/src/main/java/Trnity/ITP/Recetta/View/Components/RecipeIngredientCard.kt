package Trnity.ITP.Recetta.View.Components

import Trnity.ITP.Recetta.Model.entities.IngredientRecipe
import Trnity.ITP.Recetta.ui.theme.DarkGray
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun RecipeIngredientCard ( recipeIngredient: IngredientRecipe){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .background(Color.Transparent)
    ) {
        Card(
            //shape = Shapes.large,
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            colors = CardDefaults.cardColors(Color.Transparent)
        ) {
            var imageFile = recipeIngredient.ingredient?.image?: ""
            var image=""
            if(imageFile.contains(" "))
            {

                image = imageFile.replace(" " , "+")
            }
            AsyncImage("http://192.168.1.17:3000/uploads/"+ image , contentDescription = "Test" ,
                Modifier
                    .padding(0.dp)
                    .size(70.dp))
        }
        Text(
            text = recipeIngredient.ingredient?.name ?: "Unknown Ingredient", // Default title if null
            modifier = Modifier.width(100.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = recipeIngredient.qte.toString() ?: "No category", // Default subtitle if null
            color = DarkGray,
            modifier = Modifier.width(100.dp).align(Alignment.CenterHorizontally),
            fontSize = 14.sp
        )
    }
}
