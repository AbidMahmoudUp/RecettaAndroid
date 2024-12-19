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
import Trnity.ITP.Recetta.ViewModel.AuthViewModel
import Trnity.ITP.Recetta.ui.theme.dimens
import android.content.ContentResolver
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


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
            var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

            EditedProfileSection(navController ,updatedUserName , prof_img = userData.profileImage.toString(),onImageChange = { uri -> selectedImageUri = uri }, onUserNameChange = { updatedName ->
                updatedUserName = updatedName
            })
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

            UpdateSection(context ,navController ,profile_Img = selectedImageUri ,updatedUser , accT.toString())



        }
    }
}




@Composable
fun OptionsSection( accT: String,userData: UpdateUserDto,
                    newEmailValue: String,
                    onEmailChange: (String) -> Unit,
                    newPhoneValue: String,
                    onPhoneChange: (String) -> Unit) {
    val context = LocalContext.current
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

// State variables
            var password by remember { mutableStateOf("") }
            var repeatPassword by remember { mutableStateOf("") }
            var oldPassword by remember { mutableStateOf("") }
            var oldPasswordConfirmed by remember { mutableStateOf(false) }
            var errorPassword by remember { mutableStateOf("") }
            var errorRepeatPassword by remember { mutableStateOf("") }
            var errorOldPassword by remember { mutableStateOf("") }
            var passwordVisibility by remember { mutableStateOf(false) }
            var repeatPasswordVisibility by remember { mutableStateOf(false) }
            var oldPasswordVisibility by remember { mutableStateOf(false) }

            if (changePasswordTF) {
                if (oldPasswordConfirmed) {
                    // Icon for old password visibility
                    val oldPasswordIcon = if (oldPasswordVisibility) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

                    LoginTextField(
                        error = errorOldPassword.isNotEmpty(),
                        label = "Old Password",
                        trailing = oldPasswordIcon,
                        passwordVisibility = oldPasswordVisibility,
                        onTrailingClick = {
                            oldPasswordVisibility = !oldPasswordVisibility
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
                            modifier = Modifier.padding(start = 270.dp)
                                .clickable {
                                    val token = "Bearer $accT"
                                    val preferences = context.getSharedPreferences("checkbox", MODE_PRIVATE)
                                    val userId = preferences.getString("userId", "") ?: ""

                                    val creds = ChangePasswordDto(userId, oldPassword, password)
                                    println("Variables for changePassword: $creds")

                                    val call = RetrofitInstance.api.changePassword(creds, token)
                                    call.enqueue(object : Callback<String> {
                                        override fun onResponse(call: Call<String>, response1: Response<String>) {
                                            changePasswordTF = !changePasswordTF
                                            println("Response: ${response1.body()}")
                                            println("Code: ${response1.code()}, Message: ${response1.message()}")
                                        }

                                        override fun onFailure(call: Call<String>, t: Throwable) {
                                            println("Failure: ${t.message}")
                                        }
                                    })
                                },
                            text = "Confirm",
                            color = Color.Blue,
                            fontSize = 18.sp,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                } else {
                    // Icon for new password visibility
                    val newPasswordIcon = if (passwordVisibility) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
                    val repeatPasswordIcon = if (repeatPasswordVisibility) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24

                    LoginTextField(
                        error = errorPassword.isNotEmpty(),
                        label = "New Password",
                        trailing = newPasswordIcon,
                        passwordVisibility = passwordVisibility,
                        onTrailingClick = {
                            passwordVisibility = !passwordVisibility
                        },
                        value = password,
                        onValueChange = { newPassword -> password = newPassword },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
                    Text(errorPassword, color = Color.Red)
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

                    LoginTextField(
                        error = errorRepeatPassword.isNotEmpty(),
                        label = "Confirm Password",
                        trailing = repeatPasswordIcon,
                        passwordVisibility = repeatPasswordVisibility,
                        onTrailingClick = {
                            repeatPasswordVisibility = !repeatPasswordVisibility
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
                            modifier = Modifier.padding(start = 300.dp)
                                .clickable {
                                    errorPassword = ""
                                    errorRepeatPassword = ""

                                    if (password.isEmpty()) {
                                        errorPassword = "Type your New Password"
                                    }
                                    if (repeatPassword.isEmpty()) {
                                        errorRepeatPassword = "You have to Retype your Password"
                                    }
                                    when {
                                        password.isEmpty() || repeatPassword.isEmpty() -> {
                                            // Errors are already set above
                                        }
                                        !passwordValidationFP(password) -> {
                                            errorPassword = if (password.length < 8) {
                                                "Your Password must contain at least 8 characters"
                                            } else {
                                                "Your Password must contain at least one number"
                                            }
                                        }
                                        !comparePasswords(password, repeatPassword) -> {
                                            errorRepeatPassword = "Password mismatch. Ensure both fields are identical."
                                        }
                                        else -> {
                                            oldPasswordConfirmed = true
                                        }
                                    }
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

fun getFileFromUri(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        file.outputStream().use { output ->
            inputStream?.copyTo(output)
        }
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


@Composable
fun UpdateSection(context: Context , navController: NavController ,profile_Img : Uri? ,updatedUser : UpdateUserDto , accToken :String) {
    var showDialog by remember { mutableStateOf(false) }

    val authViewModel = remember { AuthViewModel() }
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
                val preferences = context.getSharedPreferences("checkbox", MODE_PRIVATE)
                val user_id = preferences.getString("userId","")

                val file = profile_Img?.let { uri ->
                    getFileFromUri(context, uri) // Convert content URI to File
                }

                println("Selected file path: ${file?.absolutePath}") // Debugging line

                if (file != null && file.exists()) {
                    println("File exists, proceeding with upload")
                    // Call uploadImage inside a coroutine
                    authViewModel.viewModelScope.launch {
                        authViewModel.uploadImage(file, user_id.toString())
                    }
                } else {
                    println("File does not exist or could not be resolved")
                }

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
fun EditedProfileSection( navController: NavController , newUserName: String ,prof_img : String,onUserNameChange: (String) -> Unit ,onImageChange: (Uri?) -> Unit ) {

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
                onImageChange(it)
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


                if (prof_img != null) {
                    AsyncImage(
                        model = ("http://10.0.2.2:3000/uploads/"+prof_img),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize().align(Alignment.Center),
                        contentScale = ContentScale.Crop,
                    )
                    Log.d("message String " , "http://10.0.2.2:3000/uploads/"+prof_img)
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
