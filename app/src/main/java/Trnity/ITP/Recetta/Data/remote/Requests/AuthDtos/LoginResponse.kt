package Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos;

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)