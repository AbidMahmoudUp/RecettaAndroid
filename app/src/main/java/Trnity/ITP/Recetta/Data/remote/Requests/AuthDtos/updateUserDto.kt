package Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos;

data class UpdateUserDto(
    val userId: String = "",
    var name: String = "",
    var age: Number = 0,
    var email: String = "",
    var phone:String = "",
)
