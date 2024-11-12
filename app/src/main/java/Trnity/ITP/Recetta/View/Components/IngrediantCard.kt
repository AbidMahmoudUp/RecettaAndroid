package Trnity.ITP.Recetta.View.Components

import Trnity.ITP.Recetta.Model.entities.Ingredient
import Trnity.ITP.Recetta.Model.entities.IngredientInventory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IngrediantCard(ingrediant: IngredientInventory) {

    val directImageUrl = ingrediant.ingrediant.image.replace("https://drive.google.com/file/d/", "https://drive.google.com/uc?export=download&id=")
        .replace("/view?usp=drive_link", "")
    val quantitie : Int = 17
    Row(modifier = Modifier
        .padding(0.dp, 8.dp, 4.dp, 4.dp)
        .fillMaxWidth()
        ){
        AsyncImage(model = directImageUrl , contentDescription = "Test" ,
          Modifier
              .padding(12.dp)
              .size(70.dp))
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            ,verticalArrangement = Arrangement.Bottom ,horizontalAlignment = Alignment.CenterHorizontally ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 16.dp, 4.dp, 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                Text(ingrediant.ingrediant.name ,style = MaterialTheme.typography.titleMedium )
                Text(text = " items :"+ quantitie,style = MaterialTheme.typography.bodySmall )
            }
            Spacer(Modifier.height(20.dp))
            Row (modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                //Text(ingrediant.categorie , style = MaterialTheme.typography.bodySmall)
                Text(DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ENGLISH,).format(LocalDate.now()),style = MaterialTheme.typography.bodySmall )
            }
        }
    }
}

