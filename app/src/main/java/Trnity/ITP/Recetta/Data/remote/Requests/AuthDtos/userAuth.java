package Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos;

public class userAuth {
    public userAuth( String email , String password) {

        this.email = email;
        this.password = password;
    }

    String email;
    String password;

}
