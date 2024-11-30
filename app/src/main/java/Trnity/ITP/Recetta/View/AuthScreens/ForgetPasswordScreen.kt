package Trnity.ITP.Recetta.View.AuthScreens
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.ForgetPasswordResponseDto
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.ResetPasswordDto
import Trnity.ITP.Recetta.Data.remote.api.ApiInterfaces.RetrofitInstance
import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.ui.theme.Black
import Trnity.ITP.Recetta.ui.theme.BlueGray
import Trnity.ITP.Recetta.ui.theme.ScreenOrientation
import Trnity.ITP.Recetta.ui.theme.dimens
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ForgetPasswordScreen(navController: NavController, response: ForgetPasswordResponseDto) {
    Surface {
        if (ScreenOrientation == Configuration.ORIENTATION_PORTRAIT){
            PortraitLoginScreen(navController ,response)
        }else{
            PortraitLoginScreen(navController , response)
        }
    }
}

@Composable
private fun PortraitLoginScreen(navController :NavController  , response: ForgetPasswordResponseDto) {
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
            LoginSection(navController , response)
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

        }
        Spacer(modifier = Modifier.weight(0.8f))
    }
}








@Composable
private fun LoginSection(navController: NavController , response: ForgetPasswordResponseDto) {
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    var errorPassword by remember { mutableStateOf("") }
    var errorRepeatPassword by remember { mutableStateOf("") }
    var passworVisibility by remember { mutableStateOf(false) }
    val icon = if (passworVisibility) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24

    LoginTextField(
        error = !errorPassword.isEmpty(),
        label = "New Password",
        trailing = icon,
        passwordVisibility = passworVisibility,
        onTrailingClick = {
            passworVisibility = !passworVisibility
        },

        value = password,
        onValueChange = { newPassword -> password = newPassword },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
    Text(errorPassword, color =  Color.Red )
    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

    var repeatPassworVisibility by remember { mutableStateOf(false) }
    val icon1 = if (repeatPassworVisibility) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24

    LoginTextField(
        error = !errorRepeatPassword.isEmpty(),
        label = "Confirm Password",
        trailing = icon1,
        passwordVisibility = repeatPassworVisibility,
        onTrailingClick = {
            repeatPassworVisibility = !repeatPassworVisibility

        },
        value = repeatPassword,
        onValueChange = { newPassword -> repeatPassword = newPassword },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
    Text(errorRepeatPassword, color =  Color.Red )
    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.medium3),
        onClick = {
            errorPassword=""
            errorRepeatPassword=""

            if (password.isEmpty())
            {
                errorPassword = "Type your New Password"
            }
            if (repeatPassword.isEmpty())
            {
                errorRepeatPassword = "You have to Retype your Password"
            }
            when {
                password.isEmpty() || repeatPassword.isEmpty() -> {
                }
                !passwordValidationFP(password) -> {
                    errorPassword = if (password.length < 8) {
                        "Your Password must contain at least 8 characters"
                    } else {
                        "Your Password must contain at least one number"
                    }
                }
                !comparePasswords(password,repeatPassword) ->{
                    errorRepeatPassword="Password mismatch. Ensure both fields are identical."
                }
                else -> {
                    val forgetCreds = ResetPasswordDto(response.userId ,password)
                    val call = RetrofitInstance.api.resetPassword(forgetCreds)
                    call.enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response1: Response<String>) {
                                navController.navigate(Screen.LoginScreen.route)
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            println(t.message)
                            navController.navigate(Screen.LoginScreen.route)
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
            text = "Confirm",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

fun comparePasswords(password: String, repeatPassword: String): Boolean {
    return password == repeatPassword
}


fun passwordValidationFP(password: String): Boolean {
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
                .height((screenHeight / 2.12).dp),
            painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )


        Row(
            modifier = Modifier.padding(top = (screenHeight / 9).dp),
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
                .padding(bottom = 10.dp)
                .align(alignment = Alignment.BottomCenter),
            text = stringResource(id = R.string.writecode),
            style = MaterialTheme.typography.headlineLarge,
            color = uiColor
        )
    }
}