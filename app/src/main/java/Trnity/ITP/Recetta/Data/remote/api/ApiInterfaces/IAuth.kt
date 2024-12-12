
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.ChangePasswordDto
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.ForgetPasswordDto
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.ForgetPasswordResponseDto
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.userAuth

import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.RegisterDto
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.LoginResponse
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.RefreshResponseDto
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.RefreshTokenDto
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.ResetPasswordDto
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.UpdateUserDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import  retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
interface IAuth {
    @POST("auth/login")
    fun getLoginResponse(@Body userAuth: userAuth): Call<LoginResponse>
    @POST("auth/signup")
    fun getSignupResponse(@Body registerDto: RegisterDto): Call<RegisterDto>
    @POST("auth/forget-password")
    fun forgetPassword(@Body forgetPasswordDto: ForgetPasswordDto): Call<ForgetPasswordResponseDto>

    @PUT("auth/reset-password")
    fun resetPassword(@Body resetPasswordDto: ResetPasswordDto): Call<String>

    @PUT("auth/change-password")
    fun changePassword(@Body changePasswordDto: ChangePasswordDto, @Header("Authorization")token: String): Call<String>

    @POST("auth/GetUser")
    fun getUserData(@Body updateUserDto: UpdateUserDto): Call<UpdateUserDto>

    @PUT("auth/update-profile")
    fun updateProfile(@Body updateUserDto: UpdateUserDto, @Header("Authorization")token: String): Call<UpdateUserDto>

    @POST("auth/refresh")
    fun getAnotherToken(@Body refreshToken: RefreshTokenDto): Call<RefreshResponseDto>


    @POST("auth/DeleteUser")
    fun deleteAccount(@Body user: UpdateUserDto): Call<String>

    @GET("auth/profile-image/{userId}")
    fun getProfileImage(@Path("userId") userId: String): Call<ResponseBody>

    @Multipart
    @POST("auth/upload_profile_image")
    suspend fun uploadProfileImage(
        @Part file: MultipartBody.Part,
        @Part("userId") userId: RequestBody
    ): Response<ResponseBody>
}