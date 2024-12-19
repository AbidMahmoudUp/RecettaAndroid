package Trnity.ITP.Recetta.View.AuthScreens

import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.LoginResponse
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.userAuth
import Trnity.ITP.Recetta.Data.remote.api.ApiInterfaces.RetrofitInstance
import Trnity.ITP.Recetta.MainActivity
import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.ui.theme.Black
import Trnity.ITP.Recetta.ui.theme.BlueGray
import Trnity.ITP.Recetta.ui.theme.Roboto
import Trnity.ITP.Recetta.ui.theme.ScreenOrientation
import Trnity.ITP.Recetta.ui.theme.dimens
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("SuspiciousIndentation")
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current



       Surface {
          if (ScreenOrientation == Configuration.ORIENTATION_PORTRAIT){
               PortraitLoginScreen(navController , context)
          }else{
             PortraitLoginScreen(navController, context)
           }
       }
    }


@Composable
private fun PortraitLoginScreen(navController :NavController , context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
    ) {
        TopSection()
        Spacer(modifier = Modifier.height(0.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.dimens.medium1)
        ) {


            LoginSection(navController , context)
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
            SocialMediaSection()
        }
        Spacer(modifier = Modifier.weight(0.6f))
        CreateAccountSection(navController)
        Spacer(modifier = Modifier.weight(0.8f))
    }
}

@Composable
private fun ColumnScope.CreateAccountSection(navController :NavController) {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Black
  Row (modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
      Text(
          modifier = Modifier,
          text = buildAnnotatedString {
              withStyle(
                  style = SpanStyle(
                      color = Color(0xFF94A3B8),
                      fontSize = MaterialTheme.typography.labelMedium.fontSize,
                      fontFamily = Roboto,
                      fontWeight = FontWeight.Normal
                  )
              ) {
                  append("Don't have account?")
              }
              withStyle(
                  style = SpanStyle(
                      color = uiColor,
                      fontSize = MaterialTheme.typography.labelMedium.fontSize,
                      fontFamily = Roboto,
                      fontWeight = FontWeight.Medium,

                      )
              ) {
                  append(" ")


              }



          }


      )


          var text = "Create now"
          ClickableText(

              text = AnnotatedString(text) ,
              onClick = {
                 onClickCreateNow(navController )
              })



  }
}





fun onClickCreateNow(navController:NavController) {
    navController.navigate(Screen.SignupScreen.route )
}

@Composable
private fun SocialMediaSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Or continue with",
            style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF64748B))
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SocialMediaLogin(
                icon = R.drawable.google,
                text = "Google",
                modifier = Modifier.weight(1f)
            ) {

            }
            Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))
            SocialMediaLogin(
                icon = R.drawable.facebook,
                text = "Facebook",
                modifier = Modifier.weight(1f)
            ) {

            }
        }
    }
}

@Composable
private fun LoginSection(navController: NavController , context: Context) {
    var email by remember { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorPassword by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }


    Text(errorEmail, color =  Color.Red)
    LoginTextField(
        error = !errorEmail.isEmpty(),
        label = "Email",
        trailing = R.drawable.email,
        passwordVisibility = true,
        onTrailingClick = {},
        value = email,
        onValueChange = { newEmail -> email = newEmail },
        modifier = Modifier.fillMaxWidth())

    var passworVisibility by remember { mutableStateOf(false) }
    val icon = if (passworVisibility) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24

    Spacer(modifier = Modifier.height(8.dp))
    Text(errorPassword, color =  Color.Red )
    LoginTextField(
        error = !errorPassword.isEmpty(),
        label = "Password",
        trailing = icon,
        passwordVisibility = passworVisibility,
        onTrailingClick = {
                       passworVisibility = !passworVisibility
        },
        value = password,
        onValueChange = { newPassword -> password = newPassword },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(15.dp))
    var text = "Forgot your password ?"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp),
        horizontalArrangement = Arrangement.End // Align to the end
    ) {
        Text(
            text = text,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
            modifier = Modifier
                .clickable {
                    navController.navigate(Screen.VerficationCodeScreen.route)
                }
        )
    }


    Row (modifier = Modifier.fillMaxSize(),
      verticalAlignment =   Alignment.CenterVertically) {

        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = it
                oncheckClicked(checked , context)
            }
        )
        Text(
            "Remember me"
        )

      }

    Spacer(modifier = Modifier.height(15.dp))
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment =   Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
    Button(
        modifier = Modifier
            .width(350.dp)
            .height(50.dp),
        onClick = {
            errorPassword=""
            errorEmail=""


            if(password.isEmpty()){
                errorPassword = "password can't be empty"
            }
            if(email==""){
                errorEmail = "email can't be empty"
            }
            else if(((!EmailValidationLogin(email))&&(password.isEmpty()))||(!EmailValidationLogin(email))){
                errorEmail = "That's not a real email !"
            }else if (password.isEmpty()){
                errorPassword = "password can't be empty"
            }
            else{
            val loginCreds = userAuth(email, password)
            val call = RetrofitInstance.api.getLoginResponse(loginCreds)
            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {

                    println(response.message())
                    println(response.code())
                    if(response.code()==201) {
                        val loginData = LoginResponse(
                            response.body()?.accessToken.toString(),
                            response.body()?.refreshToken.toString(),
                            response.body()?.userId.toString())
                        val json = Gson().toJson(loginData)
                        val preferences = context.getSharedPreferences("checkbox", MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putString("accToken" ,response.body()?.accessToken.toString())
                        editor.putString("refToken" ,response.body()?.refreshToken.toString())
                        editor.putString("userId" ,response.body()?.userId.toString())
                        editor.apply()

                         context.startActivity(Intent(context, MainActivity::class.java))

                    }
                    else {
                        println(response.code())
                        errorPassword=" "
                        errorEmail="Wrong credentials"
                    }

                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    errorEmail= "something has gone wrong !"
                    println(t.message)

                }
            })}
         },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSystemInDarkTheme()) BlueGray else Color(0xFFFF5722),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(size = 35.dp)
    ) {
        Text(
            text = "Log in",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
        }
}


fun oncheckClicked(checked: Boolean, context: Context) {

    if(checked)
    {
        val preferences = context.getSharedPreferences("checkbox", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("remember" , "true")
        editor.apply()
        Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show()
    }else if (!checked)
    {
        val preferences = context.getSharedPreferences("checkbox", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("remember" , "false")
        editor.apply()
        Toast.makeText(context, "UnChecked", Toast.LENGTH_SHORT).show()
    }

}

fun EmailValidationLogin(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
private fun TopSection() {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Black
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val logoHeight = (screenHeight / 4.5).dp // Adjust the logo size proportionally

    Box(
        modifier = Modifier
            .fillMaxWidth()
             // Adjust top padding for spacing
    ) {
        // Top-right corner shape
        Image(
            modifier = Modifier
                .size(180.dp) // Adjust size as necessary
                .align(Alignment.TopEnd)
                .padding(top = 0.dp, start = 28.dp), // Add padding to position the shape
            painter = painterResource(id = R.drawable.shape), // Replace with your shape resource ID
            contentDescription = null,
            contentScale = ContentScale.Fit
        )

        // Logo in the center
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp), // Adjust spacing below the shape
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Restrict the width for better proportions
                    .height(logoHeight) // Set logo height
                    .padding(bottom = 16.dp), // Add bottom padding for better spacing
                painter = painterResource(id = R.drawable.newlogo),
                contentDescription = null,
                contentScale = ContentScale.Fit // Ensure the image retains proportions
            )

            Text(
                text = stringResource(id = R.string.sigintoyouraccount),
                style = MaterialTheme.typography.headlineLarge,
                color = uiColor,
                modifier = Modifier.padding(top = 16.dp) // Adjust top padding
            )
        }
    }
}
