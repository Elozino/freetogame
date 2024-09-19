package com.dev.freetoplay.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalUriHandler
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.dev.freetoplay.presentation.screen.base.Index
import com.dev.freetoplay.presentation.screen.base.Screen
import com.dev.freetoplay.presentation.theme.FreeToPlayTheme
import com.dev.freetoplay.util.ALL_GAMES_KEY
import com.dev.freetoplay.util.BROWSER_GAMES
import com.dev.freetoplay.util.FILTER_MODE_KEY
import com.dev.freetoplay.util.LATEST_GAMES
import com.dev.freetoplay.util.PC_GAMES
import com.dev.freetoplay.util.SEARCH_MODE_KEY
import com.dev.freetoplay.util.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.splashScreenVisible.value
            }
        }
        setContent {
            val scaffoldState = rememberScaffoldState()

            val navController = rememberNavController()

            val availableGames by mainViewModel.games
                .collectAsState()

            val scope = rememberCoroutineScope()

            val uriHandler = LocalUriHandler.current

            val route by mainViewModel.currentRoute
                .collectAsState()

            if (route.isNotEmpty()) {
                LaunchedEffect(key1 = route) {
                    navController.navigate(route = route) {
                        launchSingleTop = true
                    }
                }
            }

            FreeToPlayTheme(
                darkTheme = isSystemInDarkTheme()
            ) {
                Index(
                    scaffoldState = scaffoldState,
                    navController = navController,
                    availableGames = availableGames,
                    onOpenDrawer = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    },
                    onSearchButtonClick = { games ->
                        val path = "search?mode=$SEARCH_MODE_KEY"
                        navController.navigate(
                            route = path,
                            onNavigate = {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    key = ALL_GAMES_KEY,
                                    value = games
                                )
                                mainViewModel.setRoute(route = path)
                            }
                        )
                    },
                    onGameClick = { gameId ->
                        val path = "gameDetail/$gameId"
                        navController.navigate(
                            route = path,
                            onNavigate = {
                                mainViewModel.setRoute(route = path)
                            }
                        )
                    },
                    onPlayTheGameClicked = { gameUrl ->
                        uriHandler.openUri(uri = gameUrl)
                    },
                    onHomeMenuClick = {
                        scope.launch {
                            val path = Screen.HomeScreen.route
                            navController.navigate(
                                route = path,
                                onNavigate = {
                                    mainViewModel.setRoute(route = path)
                                    scaffoldState.drawerState.close()
                                }
                            )
                        }
                    },
                    onPCGamesClick = {
                        scope.launch {
                            val path = "search?mode=$FILTER_MODE_KEY&filter=$PC_GAMES"
                            navController.navigate(
                                route = path,
                                onNavigate = {
                                    mainViewModel.setRoute(route = path)
                                    scaffoldState.drawerState.close()
                                }
                            )
                        }
                    },
                    onWebGamesClick = {
                        scope.launch {
                            val path = "search?mode=$FILTER_MODE_KEY&filter=$BROWSER_GAMES"
                            navController.navigate(
                                route = path,
                                onNavigate = {
                                    mainViewModel.setRoute(route = path)
                                    scaffoldState.drawerState.close()
                                }
                            )
                        }
                    },
                    onLatestGamesClick = {
                        scope.launch {
                            val path = "search?mode=$FILTER_MODE_KEY&filter=$LATEST_GAMES"
                            navController.navigate(
                                route = path,
                                onNavigate = {
                                    mainViewModel.setRoute(route = path)
                                    scaffoldState.drawerState.close()
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}