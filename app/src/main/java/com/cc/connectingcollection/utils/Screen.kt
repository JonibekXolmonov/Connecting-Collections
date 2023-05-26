package com.cc.connectingcollection.utils

sealed class Screen(
    val route: String
) {
    object Menu : Screen("menu")
    object Settings : Screen("settings")
    object Game : Screen("game")
    object BestScore : Screen("bestScore")
    object Score : Screen("score")
}