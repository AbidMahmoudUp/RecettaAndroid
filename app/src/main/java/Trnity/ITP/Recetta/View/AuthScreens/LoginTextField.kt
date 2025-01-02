package Trnity.ITP.Recetta.View.AuthScreens

import Trnity.ITP.Recetta.ui.theme.Black
import Trnity.ITP.Recetta.ui.theme.focusedTextFieldText
import Trnity.ITP.Recetta.ui.theme.unfocusedTextFieldText
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTextField(
    modifier: Modifier = Modifier,
    label :String,
    trailing : Int,
    passwordVisibility : Boolean,
    value: String,
    error: Boolean,
    onTrailingClick: () -> Unit,
    onValueChange: (String) -> Unit) {
    if ((label =="Password" )||(label =="New Password")||(label =="Confirm Password") ||(label =="Old Password")) {
        passwordTextField(label,trailing,passwordVisibility ,value ,error ,onValueChange , onTrailingClick)
    }else
    {
        normalTextField(label, trailing, value,error, onValueChange)

    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun passwordTextField(label: String, trailing: Int,passwordVisibility :Boolean ,value: String,error : Boolean,
                              onValueChange: (String) -> Unit,onTrailingClick: () -> Unit ) {

    val uiColor = if (isSystemInDarkTheme()) Color.White else Black

    val focusManager = LocalFocusManager.current

    TextField(
        modifier = Modifier.fillMaxWidth()
            .border(
                width = 1.dp,
                color =if(error) Color.Red else Color.Transparent, // Dynamically change color
                shape = RoundedCornerShape(4.dp)
            )
           ,
        value = value,
        maxLines = 1,
     //   keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),

        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = uiColor
            )
        },
        colors = TextFieldDefaults.colors(

            unfocusedPlaceholderColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
            focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFieldText,
        ),
        trailingIcon = {
            IconButton(
                onClick = onTrailingClick) {
                Icon(painter = painterResource(trailing), contentDescription ="" ,tint = Color(0xFFFF5722) )
            }


        },

        visualTransformation = if(passwordVisibility) VisualTransformation.None else
            PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus(true)
            })
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun normalTextField(label: String, trailing: Int ,value: String,error : Boolean,
                            onValueChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current

    val uiColor = if (isSystemInDarkTheme()) Color.White else Black

    TextField(
        modifier = Modifier.fillMaxWidth()
            .border(
                width = 1.dp,
                color =if(error) Color.Red else Color.Transparent, // Dynamically change color
                shape = RoundedCornerShape(4.dp)
            )
        ,
        value = value,
        onValueChange = onValueChange,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        ),
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = uiColor
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
            focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFieldText,
        ),
        trailingIcon = {
         Icon(painter = painterResource(trailing) , contentDescription = "" ,tint = Color(0xFFFF5722))

        }

    )
}