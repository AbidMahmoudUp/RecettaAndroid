package Trnity.ITP.Recetta.Data.remote.api

import Trnity.ITP.Recetta.Model.entities.User
import retrofit2.http.GET
import retrofit2.http.Path


interface UserApiService {
        @GET("user/{id}")
        suspend fun getUser(@Path("id") id: String): User

        @GET("user/all")
        suspend fun getAllUsers(): List<User>
    }
