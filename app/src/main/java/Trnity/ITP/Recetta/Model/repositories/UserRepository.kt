package Trnity.ITP.Recetta.Model.repositories


import Trnity.ITP.Recetta.Model.entities.User
interface UserRepository{

    suspend fun getUser(id:String): User
    suspend fun getAllUsers(): List<User>
}