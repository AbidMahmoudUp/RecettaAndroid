package Trnity.ITP.Recetta.View

import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.LoginResponse
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.UpdateUserDto
import Trnity.ITP.Recetta.Data.remote.api.ApiInterfaces.RetrofitInstance
import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.View.AuthScreens.LoginTextField
import Trnity.ITP.Recetta.View.AuthScreens.MainActivity
import Trnity.ITP.Recetta.View.AuthScreens.Screen
import Trnity.ITP.Recetta.ViewModel.AuthViewModel
import Trnity.ITP.Recetta.ui.theme.dimens
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val preferences = context.getSharedPreferences("checkbox", MODE_PRIVATE)
    val userid = preferences.getString("userId","")
    var dataLoaded by remember { mutableStateOf(false) }

    if (!dataLoaded)
    {
        emptyScreen(context , navController)
    }
    // val call = RetrofitInstance.api.getUserData(Creds ,Tokena)
    val call = RetrofitInstance.api.getUserData(UpdateUserDto(userid.toString()))

    var userData by remember { mutableStateOf(UpdateUserDto()) }


    call.enqueue(object : Callback<UpdateUserDto> {
        override fun onResponse(call: Call<UpdateUserDto>, response: Response<UpdateUserDto>) {
            println("response ::::: "+ response.body())
            if (response.code() == 201) {
                userData = response.body()!!
                dataLoaded = true
                println("response ::::: "+ userData.name)
                Log.d("azeazeazea" , response.body().toString())
            }
        }

        override fun onFailure(call: Call<UpdateUserDto>, t: Throwable) {
        }

    })
    if (dataLoaded){
        profileSettingsSection(context, navController, userData , userid.toString())
    }


}

@Composable
private fun emptyScreen(context: Context, navController: NavController) {

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
            , Arrangement.Center, Alignment.CenterHorizontally
        ) {



            // Loading Text
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                ),
                color = Color.Black
            )


            Text(
                modifier = Modifier.clickable {

                    val preferences = context.getSharedPreferences("checkbox", MODE_PRIVATE)
                    val editor = preferences.edit()
                    editor.putString("remember" , "false")
                    editor.apply()
                    context.startActivity(Intent(context, MainActivity::class.java))
                },
                text = "go to login Screen",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                ),
                color = Color.Black
            )
        }
    }
}


@Composable
private fun profileSettingsSection(
    context: Context,
    navController: NavController,
    userData  : UpdateUserDto,
    data: String
) {
    var changePassword by remember { mutableStateOf(false) }
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .padding(16.dp), Arrangement.Top, Alignment.CenterHorizontally
        ) {

            ProfileSection(userData , navController)
            Spacer(modifier = Modifier.height(40.dp))
            OptionsSection1(navController)
            Spacer(modifier = Modifier.height(40.dp))
            LogoutSection(context, navController)
            Spacer(modifier = Modifier.height(20.dp))
            DeleteAccountSection(data , navController , context)


//            Text(data?.userId.toString())
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "App version 1.0.0 (release)",
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                modifier = Modifier ,
                fontSize = 15.sp

            )

        }
    }
}

@Composable
private fun DeleteAccountSection(data: String, navController: NavController, context: Context) {
    var showDialog by remember { mutableStateOf(false) }

    AlertWithButtons("Delete Account ", "Are you sure you want to Delete your account ?",
        showDialog = showDialog,
        onConfirm = {

            val preferences = context.getSharedPreferences("checkbox", MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("remember" , "false")
            editor.apply()

            val call = RetrofitInstance.api.deleteAccount(UpdateUserDto(data))

            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(response.code() == 201){
                        navController.navigate(Screen.LoginScreen.route)
                        showDialog = false

                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {

                }


            })

            navController.navigate(Screen.LoginScreen.route)
            showDialog = false

        },
        onCancel = {
            showDialog = false
        }
    )

    Row(modifier = Modifier.fillMaxWidth(), Arrangement.Center) {
        Text(
            modifier = Modifier.clickable {
                showDialog = true
            },
            text = "Delete Account",
            color = Color.Red,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
        )
    }
}

@Composable
fun AlertWithButtons(
    title : String,
    message : String,
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onCancel()
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(message)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onCancel()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun OptionsSection1(navController: NavController) {
    Column(
        horizontalAlignment =  Alignment.CenterHorizontally,
        verticalArrangement =  Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .shadow(5.dp)
            .fillMaxWidth()
            .background(Color(0xFFF2ECF4), RoundedCornerShape(12.dp))
            .padding(16.dp)

    ) {

        Column {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 10.dp ,bottom = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF2CD6F1), CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.lock),
                        contentDescription = "Profile Picture",
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                            .shadow(10.dp)
                    )
                }

                Text(
                    text = "Change Password",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(80.dp))

                Icon(
                    painter = painterResource(id = R.drawable.arrow_forward_r),
                    contentDescription = "forward arrow",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(15.dp)
                    ,

                    )

            }

            HorizontalDivider(thickness = 0.4.dp)

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 10.dp , bottom = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFF473A2), CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.info),
                        contentDescription = "Profile Picture",
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                            .shadow(10.dp)                   )
                }

                Text(
                    text = "About Us",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.width(155.dp))
                Icon(
                    painter = painterResource(id = R.drawable.arrow_forward_r),
                    contentDescription = "forward arrow",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(15.dp)
                        .clickable { navController.navigate(Screen.AboutUsScreen.route) }
                    ,

                    )

            }

            HorizontalDivider(thickness = 0.4.dp)

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 10.dp , bottom = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF5A21A7), CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.question_mark),
                        contentDescription = "Profile Picture",
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                            .shadow(10.dp)                   )
                }

                Text(
                    text = "FAQs",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(190.dp))

                Icon(
                    painter = painterResource(id = R.drawable.arrow_forward_r),
                    contentDescription = "forward arrow",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(15.dp)
                        .clickable { navController.navigate(Screen.FQSsScreen.route) },

                    )


            }


        }

    }
}


@Composable
fun LogoutSection(context: Context, navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment =  Alignment.CenterHorizontally,
        verticalArrangement =  Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .shadow(5.dp)
            .fillMaxWidth()
            .background(Color(0xFFF2ECF4), RoundedCornerShape(12.dp))
            .padding(16.dp)
            .clickable {
                showDialog = true

            }

    ) {
        AlertWithButtons("Sing Out","Are you sure you want to Logout?",
            showDialog = showDialog,
            onConfirm = {
                val preferences = context.getSharedPreferences("checkbox", MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putString("remember" , "false")
                editor.apply()
                context.startActivity(Intent(context , MainActivity::class.java))
                showDialog = false
            },
            onCancel = {
                showDialog = false
            }
        )
        Column {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 10.dp ,bottom = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Red, CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.power_button),
                        contentDescription = "Profile Picture",
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                            .shadow(10.dp)                   )
                }

                Text(
                    text = "Log Out",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }


        }

    }
}


@Composable
fun ProfileSection(userData: UpdateUserDto, navController: NavController) {

    Column(
        horizontalAlignment =  Alignment.CenterHorizontally,
        verticalArrangement =  Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        val context = LocalContext.current
        val preferences = context.getSharedPreferences("checkbox", MODE_PRIVATE)
        val userId = preferences.getString("userId", "") ?: ""

        // Create the AuthViewModel instance
        val authViewModel = remember { AuthViewModel() }

        // Trigger the image fetch when the Composable is first composed
        LaunchedEffect(userId) {
            if (userId.isNotEmpty()) {
                authViewModel.getProfileImage(userId, context)
            }
        }

        // Observe the imageUrl state from the ViewModel
        val imageUrl = authViewModel.imageUrl.value

        Box(
            modifier = Modifier
                .size(150.dp)
                .background(Color.Gray, CircleShape)
                .shadow(10.dp, shape = RoundedCornerShape(74.dp))
        ) {
            if (imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = ("http://10.0.2.2:3000/uploads/"+userData.profileImage),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize().align(Alignment.Center),
                    contentScale = ContentScale.Crop,
                )
                Log.d("message String " , "http://10.0.2.2:3000/uploads/"+imageUrl)
                Log.d("message String " , "http://10.0.2.2:3000/uploads/"+userData.profileImage)
            } else {
                // Placeholder image when no profile image is found
                Image(
                    painter = painterResource(id = R.drawable.profilepicexample),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize().align(Alignment.Center),
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Column {
            Text(
                text = userData.name.toString(),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Button(
            modifier = Modifier.shadow(10.dp , shape = RoundedCornerShape(20.dp)),
            onClick = {
                val json = Gson().toJson(userData)
                navController.navigate("editProfile" +"/$json")
                {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = ButtonDefaults.buttonColors(  Color(0xFFFF6F00)),
            shape = RoundedCornerShape(20.dp),
        ) {
            Text(text = "Edit Profile", color = Color.White, fontSize = 12.sp)
        }
    }
}


@Composable
private fun changePasswordTextField(navController: NavController, data: LoginResponse?, context: Context) {
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var errorPassword by remember { mutableStateOf("") }
    var errorRepeatPassword by remember { mutableStateOf("") }
    var passworVisibility by remember { mutableStateOf(false) }
    val icon =
        if (passworVisibility) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24

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
    Text(errorPassword, color = Color.Red)
    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

    var repeatPassworVisibility by remember { mutableStateOf(false) }
    val icon1 =
        if (repeatPassworVisibility) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24

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

    Button(onClick = {
        val Tokena = "Bearer "+data?.accessToken.toString()
        println("aaaaaaaaaa"+ data?.accessToken.toString())





        }
        ,
        colors = ButtonColors(
            Color(0xFFBF360C) ,
            Color.White ,
            Color(0xFFBF360C),
            Color(0xFFBF360C)
        )
    ){
        Text(
            text = "Confirm",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}