package ir.kasebvatan.torna.presentation.screens.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ir.kasebvatan.torna.presentation.navigation.Screen
import ir.kasebvatan.torna.presentation.screens.builder.BuilderScreen
import ir.kasebvatan.torna.presentation.screens.contact.ContactScreen
import ir.kasebvatan.torna.presentation.screens.parser.ParserScreen
import ir.kasebvatan.torna.presentation.theme.TornaTheme


val items = listOf(
    Screen.Builder,
    Screen.Parser,
    Screen.Contact,
)

@Composable
fun HomeScreen() {

    val context = LocalContext.current
    val activity = LocalActivity.current
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Builder.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Builder.route) {
                BuilderScreen(backHandler = {
                    activity?.finishAffinity()
                })
            }
            composable(Screen.Parser.route) {
                ParserScreen(backHandler = {
                    navController.navigate(
                        Screen.Builder.route
                    )
                })
            }
            composable(Screen.Contact.route) {
                ContactScreen(backHandler = {
                    navController.navigate(
                        Screen.Builder.route
                    )
                })
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
    showBackground = true
)
@Preview(
    name = "Light",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun HomeScreenPreview() {
    TornaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            HomeScreen()
        }
    }
}
