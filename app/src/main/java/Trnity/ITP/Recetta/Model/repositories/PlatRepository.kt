package Trnity.ITP.Recetta.Model.repositories


import Trnity.ITP.Recetta.Model.entities.Plat

interface PlatRepository {
    suspend fun getAllPlat(): List<Plat>
    suspend fun getPlat(id:String): Plat
}