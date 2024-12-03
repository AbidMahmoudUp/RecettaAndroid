package Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos;

data class ChangePasswordDto(
    val userId : String = "",
    val oldPassword: String = "",
    val newPassword: String = ""
)
