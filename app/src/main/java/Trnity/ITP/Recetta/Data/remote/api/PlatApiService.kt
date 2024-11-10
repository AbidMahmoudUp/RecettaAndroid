package Trnity.ITP.Recetta.Data.remote.api

import Trnity.ITP.Recetta.Model.entities.Plat
import Trnity.ITP.Recetta.Model.entities.Recipe
import retrofit2.http.GET
import retrofit2.http.Path

interface PlatApiService {

    @GET("Plat/{id}")
    suspend fun getPlat(@Path("id") id: String): Plat

    @GET("Plat/all")
    suspend fun getAllPlat(): List<Plat>
}