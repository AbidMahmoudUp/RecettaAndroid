package Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos;

data class RefreshResponseDto(
   var accessToken : String = "",
   var refreshToken : String= ""
)
