package Trnity.ITP.Recetta.Data.remote.api.ApiInterfaces
import IAuth
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance  private constructor() {

    companion object {
        private const val BASE_URL = "http://192.168.1.17:3000/api/"

        // Lazy initialization of the Retrofit instance
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api: IAuth by lazy {
            retrofit.create(IAuth::class.java)
        }
    }
}