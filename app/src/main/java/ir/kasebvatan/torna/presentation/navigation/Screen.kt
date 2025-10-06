package ir.kasebvatan.torna.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Builder : Screen("builder", "Builder", Icons.Default.Build)
    object Parser : Screen("parser", "Parser", Icons.Default.Info)
    object Contact : Screen("contact", "Contact", Icons.Default.Call)
}