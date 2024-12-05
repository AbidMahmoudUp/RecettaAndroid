package Trnity.ITP.Recetta.View

import Trnity.ITP.Recetta.View.Components.CameraPreview
import android.content.Context

import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController

import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


import Trnity.ITP.Recetta.R
import Trnity.ITP.Recetta.ViewModel.ScannerViewModel
import android.Manifest
import android.app.Activity
import android.content.ContextWrapper

import android.net.Uri
import android.os.Build
import android.util.Log

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf


import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

import java.util.concurrent.Executors


fun Context.getActivityOrNull(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }

    return null
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(navController: NavController, scannerViewModel : ScannerViewModel = hiltViewModel())
{



    val executor = remember { Executors.newSingleThreadExecutor() }
    val photoFile = createPhotoFile(LocalContext.current)
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    val capturedImageUri = remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted
        } else {
            // Handle permission denial
        }
    }

    LaunchedEffect(cameraPermissionState) {
        if (!cameraPermissionState.status.isGranted && cameraPermissionState.status.shouldShowRationale) {
            // Show rationale if needed
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    val controller = remember {
        LifecycleCameraController(
            context
        ).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {


        CameraPreview(controller, Modifier.fillMaxSize())


            Button(onClick = {
                controller.takePicture(outputOptions,
                    executor,
                    object : ImageCapture.OnImageSavedCallback {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onImageSaved(outputFile : ImageCapture.OutputFileResults) {
                            // On successful capture, invoke callback with the Uri of the saved file
                            val file = File(photoFile.path)
                            Log.d("File", file.toString())
                            val requestBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
                            Log.d("File", requestBody.contentLength().toString())
                            Log.d("File", requestBody.toString())
                            val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestBody)
                            scannerViewModel.updateInventory("6728036e324c60058bd4ade8", multipartBody)


                        //Uri.fromFile(photoFile)

//                            val bitmap = imageProxy.toBitmap()
//                            val byteArrayOutputStream = ByteArrayOutputStream();
//                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                            var byteArray = byteArrayOutputStream .toByteArray();
//                            var encoded = Base64.getEncoder().encodeToString(byteArray)
//                            var encodedList = mutableListOf<String>()
////                            while(encoded.length > 511)
////                            {
////                                encodedList.add(encoded.substring(IntRange(0, 511)))
////                                encoded = encoded.removeRange(IntRange(0, 511))
////                                Log.d("Encoded Image", encoded)
////                            }
////                            if(encoded.isNotEmpty()) {
////                                encodedList.add(encoded)
////                            }
//                            Log.d("Encoded Image", encodedList.toString())
                            //val scannerUserInventory : ScannerUserInventory = ScannerUserInventory(encodedList)
                            //scannerViewModel.updateInventory("6728036e324c60058bd4ade8", scannerUserInventory)
                        }

                        override fun onError(exception: ImageCaptureException) {
                            // On error, invoke the error callback with the encountered exception
                            onError(exception)
                        }
                    }
                    )
            }) {
                Text("Capture Photo")
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


