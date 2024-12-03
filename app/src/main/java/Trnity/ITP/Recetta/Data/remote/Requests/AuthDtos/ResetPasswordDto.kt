package Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos;

data class ResetPasswordDto(
   val userId: String = "",
   val newPassword: String = ""
)
