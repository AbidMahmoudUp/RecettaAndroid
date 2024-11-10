package Trnity.ITP.Recetta.Data.remote.repository

import Trnity.ITP.Recetta.Data.remote.api.PlatApiService

import Trnity.ITP.Recetta.Model.entities.Plat
import Trnity.ITP.Recetta.Model.repositories.PlatRepository

class PlatRepositoryImplementation(private val platApiService: PlatApiService) : PlatRepository {
 override  suspend fun getPlat(id: String): Plat {
        return platApiService.getPlat(id)
    }

  override  suspend fun getAllPlat(): List<Plat> {
        return platApiService.getAllPlat()
    }
}