package com.saltech.examplechatapp.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.saltech.examplechatapp.MainScreen
import com.saltech.examplechatapp.R
import com.saltech.examplechatapp.home.viewmodel.HomeViewModel
import com.saltech.examplechatapp.utils.theme.ChatAppTheme
import com.saltech.examplechatapp.utils.theme.PrimaryColor
import com.saltech.examplechatapp.utils.theme.SecondaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {

    val state by viewModel.state.collectAsState()
    var showFullScreenPicture by remember { mutableStateOf("") }
    val context = LocalContext.current

    ChatAppTheme {
        // A surface container using the 'background' color from the theme
        Box(modifier = Modifier.fillMaxSize()) {

            if (state.loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null) {
                AlertDialog(
                    onDismissRequest = { navController.popBackStack() },
                    title = { Text(text = context.getString(R.string.error)) },
                    text = { Text(text = state.error.toString()) },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.onRetry()
                        }) {
                            Text(text = "Retry")
                        }
                    }
                )
            } else {
                Column {

                    TopAppBar(
                        title = {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 10.dp)) {
                                Text(text = context.getString(R.string.app_name), color = SecondaryColor)
                            }

                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryColor)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (state.chatList.isNotEmpty()) {
                        LazyColumn {
                            items(state.chatList.size) {
                                ChatItem(
                                    chat = state.chatList[it],
                                    onClick = {
                                        navController.navigate(
                                            MainScreen.Chat.withArgs(
                                                "${state.chatList[it].userName}/${viewModel.urlEncode(state.chatList[it].avatarUrl)}")
                                        )
                                    },
                                    pictureClick = { picture->
                                        showFullScreenPicture = picture
                                    }
                                )
                            }
                        }
                    } else {
                        Box(modifier = Modifier
                            .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = context.getString(R.string.no_chats_available),
                                fontSize = 20.sp,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                        }
                    }
                }
            }

            if (showFullScreenPicture != ""){
                FullScreenPictureItem(
                    picture = showFullScreenPicture
                ) {
                    showFullScreenPicture = ""
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navController = rememberNavController(),
        viewModel = viewModel()
    )
}