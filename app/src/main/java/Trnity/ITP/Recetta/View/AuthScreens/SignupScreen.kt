package Trnity.ITP.Recetta.View.AuthScreens

import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.RegisterDto
import Trnity.ITP.Recetta.Data.remote.api.ApiInterfaces.RetrofitInstance
import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.ui.theme.Black
import Trnity.ITP.Recetta.ui.theme.BlueGray
import Trnity.ITP.Recetta.ui.theme.Roboto
import Trnity.ITP.Recetta.ui.theme.ScreenOrientation
import Trnity.ITP.Recetta.ui.theme.dimens
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SignupScreen(name: String?, navController: NavController){
        Surface {
            if (ScreenOrientation == Configuration.ORIENTATION_PORTRAIT){
                PortraitLoginScreen(navController)
            }else{
                PortraitLoginScreen(navController)
            }
        }
    }

    @Composable
    private fun PortraitLoginScreen(navController:NavController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
        ) {
            TopSection()
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.dimens.medium1)
            ) {
                LoginSection(navController)
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
                SocialMediaSection()
            }
            Spacer(modifier = Modifier.weight(0.6f))
            CreateAccountSection(navController)
            Spacer(modifier = Modifier.weight(0.8f))
        }
    }



    @Composable
    private fun SocialMediaSection() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Or Sign Up with",
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
                    append("You have an account?")
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
        var text = "Log in"
        ClickableText(
            text = AnnotatedString(text) ,
            onClick = {
                navController.navigate(Screen.LoginScreen.route)
            }
        )
    }
}


    @Composable
    private fun LoginSection(navController: NavController) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var name by remember { mutableStateOf("") }
        var emailError by remember { mutableStateOf("") }
        var passwordError by remember { mutableStateOf("") }
        var nameError by remember { mutableStateOf("") }

        LoginTextField(

            error = !nameError.isEmpty(),
            label = "Name",
            passwordVisibility = true,
            trailing = R.drawable.profile,
            onTrailingClick = {},
            value = name,
            onValueChange =  { newName -> name = newName },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
        Text(nameError, color = Color.Red)
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
        LoginTextField(
            error = !emailError.isEmpty(),
            label = "Email",
            passwordVisibility = true,
            trailing = R.drawable.email,
            onTrailingClick = {},
            value = email,

            onValueChange =  { newEmail -> email = newEmail },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
        Text(emailError, color = Color.Red)
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
        var passworVisibility by remember { mutableStateOf(false) }
        val icon = if (passworVisibility) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24

        LoginTextField(
            error = !passwordError.isEmpty(),
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

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
        Text(passwordError, color = Color.Red)
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.medium3),
            onClick = {
                emailError =""
                passwordError=""
                nameError =""

                if (email.isEmpty())
                {
                    emailError = "You have to type your Email"
                }
                if (name.isEmpty())
                {
                    nameError = "You have to type a User name"
                }
                if (password.isEmpty())
                {
                    passwordError = "password can't be empty "
                }
                when {
                    !EmailValidation(email) -> {
                        emailError = "That is not a correct Email"
                    }
                    !passwordValidation(password) -> {
                      if(password.isNotEmpty()){
                        passwordError = if (password.length < 8) {
                            "Your Password must contain at least 8 characters"
                        } else {
                            "Your Password must contain at least one number"
                        }
                      }else
                      {
                          passwordError = "password can't be empty "
                      }
                    }
                    !name.isNotEmpty() ->{
                        nameError = "You have to type a User name"
                    }
                    else -> {
                        val loginCreds = RegisterDto(name ,email, password)
                        val call = RetrofitInstance.api.getSignupResponse(loginCreds)
                        call.enqueue(object : Callback<RegisterDto> {

                            override fun onResponse(
                                call: Call<RegisterDto>,
                                response: Response<RegisterDto>
                            ) {
                                if(response.code()==201){
                                    navController.navigate(Screen.LoginScreen.route)
                                }else if(response.code()==400)
                                {
                                    emailError = "this email already in use"
                                }


                            }

                            override fun onFailure(call: Call<RegisterDto>, t: Throwable) {
                                println(t.message)
                            }
                        })




                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSystemInDarkTheme()) BlueGray else Black,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(size = 4.dp)
        ) {
            Text(
                text = "Signup",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }

fun EmailValidation(email: String): Boolean {


    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun passwordValidation(password: String): Boolean {
    val passwordRegex = "^(?=.*[0-9]).{8,}$"
    return password.matches(passwordRegex.toRegex())
}

@Composable
private fun TopSection() {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Black
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height((screenHeight / 3.12).dp),
            painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )


        Row(
            modifier = Modifier.padding(top = (screenHeight / 14).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(MaterialTheme.dimens.logoSize),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.app_logo),
                tint = uiColor
            )
            Spacer(modifier = Modifier.width(MaterialTheme.dimens.small2))
            Column {
                Text(
                    text = stringResource(id = R.string.recetta),
                    style = MaterialTheme.typography.headlineMedium,
                    color = uiColor
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(bottom = 5.dp)
                .align(alignment = Alignment.BottomCenter),
            text = stringResource(id = R.string.signup),
            style = MaterialTheme.typography.headlineLarge,
            color = uiColor
        )
    }
}