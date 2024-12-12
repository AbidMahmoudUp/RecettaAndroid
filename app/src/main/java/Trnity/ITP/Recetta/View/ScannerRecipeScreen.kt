package Trnity.ITP.Recetta.View

import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.View.Components.CameraPreview
import Trnity.ITP.Recetta.ViewModel.ScannerViewModel
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.internal.wait
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors

@Composable
fun ScannerRecipeScreen(  navController: NavController,
                          scannerViewModel: ScannerViewModel = hiltViewModel()
) {
    val updateSuccessRecipe by scannerViewModel.updateSuccessRecipe.observeAsState(false)
    val progressTriggerRecipe by scannerViewModel.progressTriggerRecipe.observeAsState(false)

    var progress by remember { mutableStateOf(0f) }
    var showLoadingScreen by remember { mutableStateOf(false) }
    val recipes by scannerViewModel.recipes.observeAsState(emptyList())

    val context = LocalContext.current
    val executor = remember { Executors.newSingleThreadExecutor() }
    val photoFile = createPhotoFile(context)
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }

    // Observe updateSuccess to navigate upon successful update
    LaunchedEffect(updateSuccessRecipe, showLoadingScreen) {
        if (showLoadingScreen && !scannerViewModel.isLoadingRecipe.value!!) {
            val list = recipes.toList()
            Log.d("List:", list.toString())
            Log.d("Generated List:", recipes.toString())

            // Encode the recipes list to JSON
            val recipesJson = Uri.encode(Gson().toJson(list))
            Log.d("JSON RECIPE", recipesJson)

            // Reset the recipes in the ViewModel
            scannerViewModel.resetRecipes()

            // Log the list after reset
            Log.d("List after:", list.toString())
            Log.d("Generated List after:", recipes.toString())

            // Navigate to the GeneratedRecipeListScreen with the recipesJson as an argument
            navController.navigate("GeneratedList/$recipesJson") {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = false
                }
                launchSingleTop = true
                restoreState = false
            }
        }
    }

    // Handle progress animation
    LaunchedEffect(progressTriggerRecipe) {
        if (progressTriggerRecipe) {
            showLoadingScreen = true
            while (progress < 1f) {
                progress += 0.01f
                delay(5000L)
            }
            progress = 0f
            showLoadingScreen = false
            scannerViewModel.resetProgressTriggerRecipe()
        }
    }
    if (showLoadingScreen) {
        RecipeGenerationLoadingContent(
            progress = progress,
            onProgressComplete = { showLoadingScreen = false },
            incrementProgress = {
                progress = (progress + 0.01f).coerceAtMost(1f)
            }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp)
        ) {


            CameraPreview(controller, Modifier.fillMaxSize())

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 75.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = {
                        controller.takePicture(
                            outputOptions,
                            executor,
                            object : ImageCapture.OnImageSavedCallback {
                                @RequiresApi(Build.VERSION_CODES.O)
                                override fun onImageSaved(outputFile: ImageCapture.OutputFileResults) {
                                    Log.d("CAMERA RECIPE SCREEN", "IMAGE SAVED")
                                    val file = File(photoFile.path)
                                    Log.d("FILE CAMERA SCREEN", file.toString())
                                    if (file.exists()) {
                                        val requestBody =
                                            file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
                                        val multipartBody =
                                            MultipartBody.Part.createFormData(
                                                "fileImage",
                                                file.name,
                                                requestBody
                                            )
                                            Log.d("AFTER REQUEST BODY", multipartBody.body.toString())
                                        var d = scannerViewModel.scanRecipe(multipartBody)
                                        Log.d("THE VARIABLE", d.toString())

                                    }
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    Log.e(
                                        "Camera",
                                        "Error capturing image: ${exception.message}",
                                        exception
                                    )
                                    Toast.makeText(
                                        context,
                                        "Failed to capture image. Please try again.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        )
                    },
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(Color.Gray.copy(alpha = 0.4f))
                    )
                }
            }
        }
    }

}

private fun createPhotoFile(context: Context): File {
    // Obtain the directory for saving the photo
    val outputDirectory = getOutputDirectory(context)
    // Create a new file in the output directory with a unique name
    return File(outputDirectory, photoFileName()).apply {
        // Ensure the file's parent directory exists
        parentFile?.mkdirs()
    }
}

private fun photoFileName() =
    SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
        .format(System.currentTimeMillis()) + ".jpg"


private fun getOutputDirectory(context: Context): File {
    // Attempt to use the app-specific external storage directory which does not require permissions
    val mediaDir = context.getExternalFilesDir(null)?.let {
        File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    // Fallback to internal storage if the external directory is not available
    return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
}


