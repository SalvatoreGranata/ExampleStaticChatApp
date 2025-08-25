package com.saltech.examplechatapp

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saltech.examplechatapp.chat.ui.ChatScreen
import com.saltech.examplechatapp.chat.viewmodel.ChatViewModel
import com.saltech.examplechatapp.home.ui.HomeScreen
import com.saltech.examplechatapp.home.viewmodel.HomeViewModel
import com.saltech.examplechatapp.MainScreen


@Composable
fun MainNavHost() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainScreen.Home.route
    ) {

        composable(route = MainScreen.Home.route) {

            val viewModel = hiltViewModel<HomeViewModel>()

            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            route = MainScreen.Chat.route + "/{name}"+"/{picture}",
            arguments = listOf(
                navArgument("name"){
                    type = NavType.StringType
                },
                navArgument("picture") {
                    type = NavType.StringType
                })
            )
         { entry ->

            val viewmodel = hiltViewModel<ChatViewModel>()
            val name = entry.arguments?.getString("name")?:""
            val picture = entry.arguments?.getString("picture")?:""

            viewmodel.name = name
            viewmodel.picture = picture

            ChatScreen(
                navController = navController,
                viewModel = viewmodel
            )

        }
    }
}