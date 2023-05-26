package com.cc.connectingcollection

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cc.connectingcollection.ui.screen.best_score.BestScoreScreen
import com.cc.connectingcollection.ui.screen.game.GameScreen
import com.cc.connectingcollection.ui.screen.menu.MenuScreen
import com.cc.connectingcollection.ui.screen.settings.SettingsScreen
import com.cc.connectingcollection.utils.Screen

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Menu.route,
    onVibrate: () -> Unit,
    onSound: (Boolean) -> Unit
) {
    NavHost(
        navController,
        startDestination = startDestination,
    ) {
        composable(Screen.Menu.route) {
            MenuScreen(
                onGameStart = {
                    onVibrate()
                    navController.navigate(Screen.Game.route)
                },
                onSettingsClick = {
                    onVibrate()
                    navController.navigate(Screen.Settings.route)
                },
                onBestScore = {
                    onVibrate()
                    navController.navigate(Screen.BestScore.route)
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackPressed = {
                    navController.popBackStack()
                    onVibrate()
                },
                onVibrate = onVibrate,
                onSound = onSound
            )
        }

        composable(Screen.Game.route) {
            GameScreen(
                onBack = {
                    onVibrate()
                    navController.popBackStack()
                },
                onVibrate = onVibrate
            )
        }

        composable(route = Screen.BestScore.route) {
            BestScoreScreen {
                navController.popBackStack()
                onVibrate()
            }
        }
    }
}