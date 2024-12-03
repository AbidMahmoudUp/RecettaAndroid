package Trnity.ITP.Recetta.View
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.ChangePasswordDto
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.UpdateUserDto
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import Trnity.ITP.Recetta.Data.remote.api.ApiInterfaces.RetrofitInstance
import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.View.AuthScreens.LoginTextField
import Trnity.ITP.Recetta.View.AuthScreens.Screen
import Trnity.ITP.Recetta.View.AuthScreens.comparePasswords
import Trnity.ITP.Recetta.View.AuthScreens.passwordValidationFP
import Trnity.ITP.Recetta.ui.theme.dimens
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun EditProfileScreen(navController: NavController , userData : UpdateUserDto) {
   val context = LocalContext.current

    var updatedEmail by remember { mutableStateOf(userData.email) }
    var updatedPhoneNumber by remember { mutableStateOf(userData.phone) }
    var updatedUserName by remember { mutableStateOf(userData.name) }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .padding(16.dp), Arrangement.Top, Alignment.CenterHorizontally
        ) {

            ProfileSection(navController, updatedUserName) { updatedName ->
                updatedUserName = updatedName
            }
            val preferences = context.getSharedPreferences("checkbox", MODE_PRIVATE)
            val accT = preferences.getString("accToken","")
            //  val refT = preferences.getString("RefToken","")
            val user_id = preferences.getString("userId","")

            Spacer(modifier = Modifier.height(20.dp))
            OptionsSection(accT.toString(),userData,newEmailValue = updatedEmail,
                onEmailChange = { updatedEmail = it },
                newPhoneValue = updatedPhoneNumber,
                onPhoneChange = { updatedPhoneNumber = it })
            Spacer(modifier = Modifier.height(20.dp))
            var updatedUser = UpdateUserDto(userId  = user_id.toString() , name = updatedUserName , phone = updatedPhoneNumber , email = if(userData.email == updatedEmail) "" else updatedEmail)
            UpdateSection(context ,navController ,updatedUser , accT.toString())



        }
    }
}




@Composable
fun OptionsSection( accT: String,userData: UpdateUserDto,
    newEmailValue: String,
                    onEmailChange: (String) -> Unit,
                    newPhoneValue: String,
                    onPhoneChange: (String) -> Unit) {
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
            var newEmailTF by remember { mutableStateOf(false) }

            var newPhoneNumberTF by remember { mutableStateOf(false) }
            var changePasswordTF by remember { mutableStateOf(false) }


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
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = "Profile Picture",
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                            .shadow(10.dp)                   )
                }

                Text(
                    text = "Email",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )



                Spacer(modifier = Modifier.width(186.dp))
                Icon(
                    painter = painterResource(id =if (!newEmailTF) R.drawable.arrow_forward_r else R.drawable.arrow_down_r ),
                    contentDescription = "forward arrow",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(if (!newEmailTF)15.dp else 23.dp)
                        .clickable {
                            changePasswordTF = false
                            newEmailTF = newEmailTF.not()
                            newPhoneNumberTF = false
                                   },
                    )

            }

            if (newEmailTF) {
                TextField(modifier = Modifier.width(320.dp)
                    .zIndex(5f)
                    .padding(2.dp),
                    value = newEmailValue,
                    onValueChange = onEmailChange,
                    singleLine = true,
                    label = { Text("Enter Email") })
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
                        painter = painterResource(id = R.drawable.baseline_local_phone_24),
                        contentDescription = "Profile Picture",
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                            .shadow(10.dp)                   )
                }

                Text(
                    text = "Phone Number",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(107.dp))

                Icon(
                    painter = painterResource(id =if (!newPhoneNumberTF) R.drawable.arrow_forward_r else R.drawable.arrow_down_r ),
                    contentDescription = "forward arrow",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(if (!newPhoneNumberTF)15.dp else 23.dp)
                        .clickable {
                            changePasswordTF = false
                            newEmailTF = false
                            newPhoneNumberTF = newPhoneNumberTF.not()
                                   },

                    )

            }
            if (newPhoneNumberTF) {
                TextField(modifier = Modifier.width(320.dp)
                    .zIndex(5f)
                    .padding(2.dp),
                    value = newPhoneValue,
                    onValueChange = onPhoneChange,
                    singleLine = true,
                    label = { Text("+216 ") })
            }

            HorizontalDivider(thickness = 0.4.dp)

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
                    painter = painterResource(id =if (!changePasswordTF) R.drawable.arrow_forward_r else R.drawable.arrow_down_r ),
                    contentDescription = "forward arrow",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(if (!changePasswordTF)15.dp else 23.dp)
                        .clickable {
                            newPhoneNumberTF = false
                            newEmailTF = false
                            changePasswordTF = changePasswordTF.not() },

                    )

            }


            var password by remember { mutableStateOf("") }
            if (changePasswordTF) {
                var oldPasswordConfirmed by remember { mutableStateOf(false) }


                if(oldPasswordConfirmed){

                    var oldPassword by remember { mutableStateOf("") }
                    var errorOldPassword by remember { mutableStateOf("") }
                    var passwordVisibility by remember { mutableStateOf(false) }

                    val icon =
                        if (passwordVisibility) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

                    LoginTextField(
                        error = !errorOldPassword.isEmpty(),
                        label = "Old Password",
                        trailing = icon,
                        passwordVisibility = passwordVisibility,
                        onTrailingClick = {
                            passwordVisibility = !passwordVisibility
                        },

                        value = oldPassword,
                        onValueChange = { newPassword -> oldPassword = newPassword },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
                    Text(errorOldPassword, color = Color.Red)
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
                    Row {

                        Text(
                            modifier = Modifier.padding(start = 270.dp )
                                .clickable {

                                    val Tokena = "Bearer "+accT.toString()
                                    val Creds = ChangePasswordDto(userData.userId.toString() ,  oldPassword ,password)
                                    val call = RetrofitInstance.api.changePassword(Creds ,Tokena)
                                    call.enqueue(object : Callback<String> {
                                        override fun onResponse(call: Call<String>, response1: Response<String>) {

                                            println(response1)
                                            println("Code :::: "+response1.code())
                                            println("messagee ::::::"+response1.message())
                                            println("body ::::::"+response1.body())
                                        }

                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            println(t.message)

                                        }
                                    })



                                },
                            text = "Confirm",
                            color = Color.Blue,
                            fontSize = 18.sp,
                            textDecoration = TextDecoration.Underline

                        )
                    }


                }else{

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
                    Text(errorRepeatPassword, color = Color.Red)
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
                    Row {

                        Text(
                            modifier = Modifier.padding(start = 300.dp )
                                .clickable {
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
                                            oldPasswordConfirmed = true

                                        }                                        }

                            },
                            text = "Next",
                            color = Color.Blue,
                            fontSize = 18.sp,
                            textDecoration = TextDecoration.Underline

                        )
                    }


            }
            }




        }

    }
}

@Composable
fun confirmUpdateAlert(
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
fun UpdateSection(context: Context , navController: NavController , updatedUser : UpdateUserDto , accToken :String) {
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
        confirmUpdateAlert("Update Changes ","Are you sure you want to make those Changes ",
            showDialog = showDialog,
            onConfirm = {
                println(updatedUser)
                val Tokena = "Bearer "+ accToken
                val call = RetrofitInstance.api.updateProfile(updatedUser , Tokena)
                call.enqueue(object : Callback<UpdateUserDto> {
                    override fun onResponse(call: Call<UpdateUserDto>, response: Response<UpdateUserDto>) {
                        println("/////***************"+response.code()+"*****************//////")
                        println("/////***************"+response.body()+"*****************//////")
                        println("/////***************"+response.message()+"*****************//////")
                        val json = Gson().toJson(updatedUser)
                        println("jjsoon" + json)
                        navController.navigate("profile")

                    }

                    override fun onFailure(call: Call<UpdateUserDto>, t: Throwable) {
                        println(t.message)
                    }

                })

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
                        .background(Color.Green, CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.change_circle),
                        contentDescription = "Profile Picture",
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                            .shadow(10.dp)                   )
                }

                Text(
                    text = "Update Profile",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }


        }

    }
}

@Composable
fun ProfileSection( navController: NavController , newUserName: String ,onUserNameChange: (String) -> Unit) {

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
            }
        }
    )
    Row(modifier = Modifier.fillMaxWidth()) {
        Icon(
            tint = Color.Blue,
            painter = painterResource(id = R.drawable.arrow_back_r),
            contentDescription = "go Back",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable {
                    navController.popBackStack()
                }
            )
    }
    Column(
        horizontalAlignment =  Alignment.CenterHorizontally,
        verticalArrangement =  Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(150.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(Color.Gray, CircleShape)
                    .shadow(10.dp, shape = CircleShape)
            ) {if (imageUri != null) {

                Image(
                    painter = rememberAsyncImagePainter(imageUri), // Use selected image
                    contentDescription = "Selected Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {

                Image(
                    painter = painterResource(id = R.drawable.profilepicexample),
                    contentDescription = "Default Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.BottomEnd)
                    .background(Color.Transparent)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                    contentDescription = "Camera Icon",
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(10.dp, CircleShape)
                        .padding(8.dp)
                        .zIndex(3f)
                        .clickable {
                            galleryLauncher.launch("image/*")
                        }

                    ,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }

        Row {
            var editUserNameTF by remember { mutableStateOf(false) }

            if(editUserNameTF){
                TextField(modifier = Modifier.width(180.dp)
                    .zIndex(5f)
                    .padding(2.dp),
                    value = newUserName,
                    onValueChange = onUserNameChange,
                    singleLine = true,
                    label = { Text("Enter UserName") } )
            }else {
                Text(
                    text = newUserName,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(2.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.baseline_mode_edit_24),
                contentDescription = "Edit Username",
                colorFilter = ColorFilter.tint(Color.DarkGray),
                modifier = Modifier.clickable {
                    editUserNameTF = editUserNameTF.not()
                }
            )
        }


    }
}
