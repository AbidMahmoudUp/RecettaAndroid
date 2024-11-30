package Trnity.ITP.Recetta.View.AuthScreens

import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.ForgetPasswordDto
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.ForgetPasswordResponseDto
import Trnity.ITP.Recetta.Data.remote.api.ApiInterfaces.RetrofitInstance
import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.ui.theme.Black
import Trnity.ITP.Recetta.ui.theme.BlueGray
import Trnity.ITP.Recetta.ui.theme.ScreenOrientation
import Trnity.ITP.Recetta.ui.theme.dimens
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun VerficationCodeScreen(navController: NavController) {


    Surface {
        if (ScreenOrientation == Configuration.ORIENTATION_PORTRAIT){
            PortraitLoginScreen(navController )
        }else{
            PortraitLoginScreen(navController )
        }
    }
}

@Composable
private fun PortraitLoginScreen(navController : NavController) {
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
            var emailValidated by remember { mutableStateOf(false) }

            var correctCode by remember { mutableStateOf(ForgetPasswordResponseDto("","")) }
            if (!emailValidated) {
                EmailValidationSection(navController,onEmailValidated = { emailValidated = true }, onCorrectCodeFilled = {s ->  correctCode =s })
            }else if (!(correctCode==ForgetPasswordResponseDto("",""))) {
                CodeValidationSection(navController, correctCode)
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
            }
        }
        Spacer(modifier = Modifier.weight(0.8f))
    }
}


fun VCEmailValidation(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
private fun EmailValidationSection(navController: NavController, onEmailValidated: () -> Unit , onCorrectCodeFilled: (ForgetPasswordResponseDto)-> Unit) {
    var email by remember { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf("") }
    var potentialError1 by remember { mutableStateOf("") }
    var potentialError2 by remember { mutableStateOf("") }

    LoginTextField(

        error = !errorEmail.isEmpty(),
        label = "Email",
        trailing = R.drawable.email,
        passwordVisibility = true,
        onTrailingClick = {
        },
        value = email,
        onValueChange = { Email -> email = Email },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
    Text(errorEmail, color =  Color.Red )
    Text(potentialError1, color =  Color.Red )
    Text(potentialError2, color =  Color.Red )
    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.medium3),
        onClick = {
            errorEmail = ""
            potentialError1 = ""
            potentialError2 = ""

            if (email.isEmpty())
            {
                errorEmail= "You must give us an Email"
            }else if(!VCEmailValidation(email)){
                errorEmail="That's not a real Email"
            }
            else {
                forgetPasswordApi(email) { success ->
                    if (!(success==ForgetPasswordResponseDto("","")) ){
                        onEmailValidated()
                        onCorrectCodeFilled(success)
                    } else {
                        errorEmail = "Something went Wrong"
                        potentialError1 = "Check if you have typed the correct Email"
                        potentialError2 = "Or it could be a Server Problem"
                    }
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
            text = "Send Code",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}
fun forgetPasswordApi(email: String, callback: (ForgetPasswordResponseDto) -> Unit) {
    val forgetCreds = ForgetPasswordDto(email)
    val call = RetrofitInstance.api.forgetPassword(forgetCreds)

    call.enqueue(object : Callback<ForgetPasswordResponseDto> {
        override fun onResponse(
            call: Call<ForgetPasswordResponseDto>,
            response: Response<ForgetPasswordResponseDto>
        ) {
            println("bbbbbbb :" + response.code())
            println("aaaaaa  " + response.body())

            if (response.code() == 201) {
//                val json = Gson().toJson(response.body())
//                println(json)
                val dto = ForgetPasswordResponseDto(response.body()?.userId.toString(),response.body()?.code.toString())
                callback(dto) // Notify success
            } else {
                callback(ForgetPasswordResponseDto("","")) // Notify failure
            }
        }

        override fun onFailure(call: Call<ForgetPasswordResponseDto>, t: Throwable) {
            println(t.message)
            callback(ForgetPasswordResponseDto("","")) // Notify failure
        }
    })
}

@Composable
private fun CodeValidationSection(navController: NavController, correctCode: ForgetPasswordResponseDto) {
    // State to track user-entered OTP
    val textList = List(6) { mutableStateOf(TextFieldValue("")) }
    val requesterList = List(6) { FocusRequester() }
    var errorMessage by remember { mutableStateOf("") }

    Column {
        OtpView(textList, requesterList) // OTP input view

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
        Text(errorMessage, color = Color.Red) // Display error if any

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
        val context = LocalContext.current
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.medium3),
            onClick = {
                // Combine the OTP fields
                val enteredCode = textList.joinToString("") { it.value.text.trim() }

                if (enteredCode == correctCode.code) {
                    errorMessage=""
                val json = Gson().toJson(correctCode)

                    navController.navigate(
                        Screen.ForgetPasswordScreen.route+ "/$json"
                    )
                } else {
                    Toast.makeText(context, "Wrong Code", Toast.LENGTH_SHORT)
                        .show()
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
}

@Composable
fun OtpView(textList: List<MutableState<TextFieldValue>>, requesterList: List<FocusRequester> ){
    val focusManager = LocalFocusManager.current
    val keboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent

    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 0.dp)
                    .padding(top = 32.dp)
                    .align(Alignment.TopCenter)

            ) {
                for (i  in textList.indices){
                    InputView(value =textList[i].value,
                        onValueChange = {
                            newValue ->

                            if (textList[i].value.text != "")
                            {
                                if (newValue.text == ""){
                                    textList[i].value = TextFieldValue(
                                        text = "",
                                        selection = TextRange(0)
                                    )
                                }
                                return@InputView
                            }
                            textList[i].value = TextFieldValue(
                                text = newValue.text,
                                selection = TextRange(newValue.text.length)
                            )
                            nextFocus(textList, requesterList)
                        },
                        focusRequester = requesterList[i]
                    )
                }

            }


        }
    }

    LaunchedEffect(key1 = null, block = {
        delay(300)
        requesterList[0].requestFocus()
        keboardController?.show()
    }
    )

}
fun nextFocus(textList: List<MutableState<TextFieldValue>>, requesterList: List<FocusRequester>){
    for (index in textList.indices){
        if (textList[index].value.text==""){
            if (index < textList.size){
                requesterList[index].requestFocus()
                break
            }
        }

    }
}



@Composable
fun InputView(
    value: TextFieldValue,
    onValueChange: (value : TextFieldValue)->Unit ,
    focusRequester : FocusRequester
){
BasicTextField(
    readOnly = false,
    value = value,
    onValueChange = onValueChange,
    modifier = Modifier
        .padding(horizontal = 8.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(Color.LightGray)
        .wrapContentSize()
        .focusRequester(focusRequester),
    maxLines = 1,
    decorationBox = {
        innerTextField ->
        Box(modifier = Modifier.width(44.dp)
            .height(52.dp),
            contentAlignment = Alignment.Center
            ){
            innerTextField()
        }
    },
    cursorBrush = SolidColor(Color.White),
    textStyle = TextStyle(
        color = Color.White,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    ),
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number , imeAction = ImeAction.Done),
    keyboardActions = KeyboardActions(onDone = null)

)


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
            text = stringResource(id = R.string.forgotpassword),
            style = MaterialTheme.typography.headlineLarge,
            color = uiColor
        )
    }
}