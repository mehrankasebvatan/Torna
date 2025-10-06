package ir.kasebvatan.torna.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ir.kasebvatan.torna.presentation.screens.home.HomeScreen
import ir.kasebvatan.torna.presentation.theme.TornaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TornaTheme {
                HomeScreen()
            }
        }
    }
}