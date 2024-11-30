package Trnity.ITP.Recetta.Data.remote.repository

import Trnity.ITP.Recetta.Data.remote.api.UserApiService
import Trnity.ITP.Recetta.Model.entities.Inventory
import Trnity.ITP.Recetta.Model.entities.User
import Trnity.ITP.Recetta.Model.repositories.UserRepository

class UserRepositoryImplementation(private val userApiService: UserApiService) : UserRepository
{
    override suspend fun getUser(id : String): User {
        return userApiService.getUser(id)
    }

    override suspend fun getAllUsers(): List<User> {
        return userApiService.getAllUsers()    }


}