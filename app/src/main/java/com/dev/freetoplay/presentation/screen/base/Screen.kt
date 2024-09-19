package com.dev.freetoplay.presentation.screen.base

sealed class Screen(
    val route: String
) {
    data object HomeScreen : Screen(route = "home")

    data object GameDetailScreen : Screen(route = "gameDetail/{id}")

    data object SearchScreen : Screen(
        route = "search?mode={mode}&filter={filter}"
    )
}