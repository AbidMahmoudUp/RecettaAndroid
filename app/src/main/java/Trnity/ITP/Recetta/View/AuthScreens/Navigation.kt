package Trnity.ITP.Recetta.View.AuthScreens

import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.ForgetPasswordResponseDto
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.LoginResponse
import Trnity.ITP.Recetta.Data.remote.Requests.AuthDtos.UpdateUserDto
import Trnity.ITP.Recetta.View.EditProfileScreen
import Trnity.ITP.Recetta.View.StaticScreens.AboutUsScreen
import Trnity.ITP.Recetta.View.StaticScreens.FAQsScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(
            route = Screen.SignupScreen.route,

        ) { backStackEntry ->
            SignupScreen(name = backStackEntry.arguments?.getString("name"),navController)
        }
        composable(
            route = Screen.ForgetPasswordScreen.route +"/{responseJson}",
            arguments = listOf(
                navArgument("responseJson") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val responseJson = backStackEntry.arguments?.getString("responseJson")
            val response = Gson().fromJson(responseJson, ForgetPasswordResponseDto::class.java)
            ForgetPasswordScreen(navController , response)
        }
        composable(
            route = Screen.VerficationCodeScreen.route  ,
            arguments = listOf(
                navArgument("responseJson") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
          //  val responseJson = backStackEntry.arguments?.getString("responseJson")
         //   val response = Gson().fromJson(responseJson, ForgetPasswordResponseDto::class.java)
            VerficationCodeScreen(navController)
        }
//        composable(
//            route = Screen.EditProfileScreen.route +"/{responseJson}",
//            arguments = listOf(
//                navArgument("responseJson") {
//                    type = NavType.StringType
//                    defaultValue = ""
//                    nullable = true
//                }
//            )
//        ) { backStackEntry ->
//              val responseJson = backStackEntry.arguments?.getString("responseJson")
//               val response = Gson().fromJson(responseJson, UpdateUserDto::class.java)
//            EditProfileScreen(navController , response)
//        }

        composable(
            route = Screen.AboutUsScreen.route,

            ) { backStackEntry ->
            AboutUsScreen(navController)
        }

        composable(
            route = Screen.FQSsScreen.route,

            ) { backStackEntry ->
            FAQsScreen(navController)
        }
    }
}

