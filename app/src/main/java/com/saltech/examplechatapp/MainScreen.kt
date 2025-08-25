package com.saltech.examplechatapp

sealed class MainScreen(val route: String) {
    object Home : MainScreen("home")
    object Chat : MainScreen("chat")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }
}