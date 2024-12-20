package Trnity.ITP.Recetta.View.AuthScreens

import Trnity.ITP.Recetta.MainActivity
import Trnity.ITP.Recetta.ui.theme.ComposeLoginScreenTheme
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                ComposeLoginScreenTheme {
                    val context = LocalContext.current
                    val preferences = context.getSharedPreferences("checkbox", MODE_PRIVATE)
                    val checkbox = preferences.getString("remember" , "")
                     if (checkbox == "true")
                     {
                         val intent = Intent(this, MainActivity::class.java)
                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                         startActivity(intent)
                         finish()

                     }else {
                         val navController = rememberNavController()
                         Navigation(navController = navController)
                     }
                }


        }
    }
}
