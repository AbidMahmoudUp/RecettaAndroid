package Trnity.ITP.Recetta.View.AuthScreens

sealed class Screen(val route :String ) {
    object  LoginScreen : Screen("login_screen")
    object SignupScreen : Screen("signup_screen")
    object ForgetPasswordScreen : Screen("forgetPassword")
    object VerficationCodeScreen: Screen("newforgetPassword")
    object AboutUsScreen : Screen("aboutus")
    object FQSsScreen : Screen("FAQs")
}