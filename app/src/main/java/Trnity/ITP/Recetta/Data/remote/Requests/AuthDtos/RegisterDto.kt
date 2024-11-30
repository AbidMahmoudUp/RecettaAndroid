package Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos;
data class RegisterDto(
    var name: String = "",
    var email: String = "",
    var password: String = ""
)