package Trnity.ITP.Recetta.Data.remote.repository

import Trnity.ITP.Recetta.Data.remote.api.ApiInterfaces.RetrofitInstance
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class AuthRepository {
    private val authApi = RetrofitInstance.api

    fun getProfileImage(userId: String): Call<ResponseBody> {
        return authApi.getProfileImage(userId)
    }

    suspend fun uploadProfileImage(file: MultipartBody.Part, userId: RequestBody): Response<ResponseBody> {
        return authApi.uploadProfileImage(file, userId)
    }
}