package Trnity.ITP.Recetta.ViewModel

import Trnity.ITP.Recetta.Data.remote.repository.AuthRepository
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    // State to hold the image URL
    val imageUrl = mutableStateOf("")
    private val _uploadState = MutableLiveData<String>()
    val uploadState: LiveData<String> get() = _uploadState

    fun getProfileImage(userId: String, context: Context) {
        authRepository.getProfileImage(userId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val imagePath = saveImage(context, response.body()!!)
                    imageUrl.value = imagePath // Set the image path when successful
                } else {
                    println("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("Error fetching profile image: ${t.message}")
            }
        })
    }

    fun saveImage(context: Context, responseBody: ResponseBody): String {
        val file = File(context.cacheDir, "profile_image.jpg") // Save to cache directory
        try {
            val inputStream = responseBody.byteStream()
            val outputStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var byteCount: Int
            while (inputStream.read(buffer).also { byteCount = it } != -1) {
                outputStream.write(buffer, 0, byteCount)
            }
            outputStream.flush()
            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }
    }
    suspend fun uploadImage(file: File, userId: String) {
        try {
            // Create RequestBody for the file
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

            // Create MultipartBody.Part for the file
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            // Create RequestBody for userId
            val userIdBody = userId.toRequestBody("text/plain".toMediaTypeOrNull())

            // Make the network request within the IO dispatcher to avoid blocking the main thread
            val response = withContext(Dispatchers.IO) {
                authRepository.uploadProfileImage(body, userIdBody)
            }

            // Handling the response
            if (response.isSuccessful) {
                println("Upload Success: ")
            } else {
                println("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            println("Error uploading image: ${e.localizedMessage}")
        }
    }

}
