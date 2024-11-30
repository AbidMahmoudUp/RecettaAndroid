package Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos;

data class ForgetPasswordResponseDto(
    var userId: String = "",
    var code: String = "",

)
