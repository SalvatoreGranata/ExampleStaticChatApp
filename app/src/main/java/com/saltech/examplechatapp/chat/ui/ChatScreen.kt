package com.saltech.examplechatapp.chat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.saltech.examplechatapp.R
import com.saltech.examplechatapp.chat.utils.MessageType
import com.saltech.examplechatapp.chat.viewmodel.ChatViewModel
import com.saltech.examplechatapp.home.ui.FullScreenPictureItem
import com.saltech.examplechatapp.utils.theme.ChatAppTheme
import com.saltech.examplechatapp.utils.theme.PrimaryColor
import com.saltech.examplechatapp.utils.theme.SecondaryColor

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel
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
                    text = { Text( text = state.error.toString()) },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.onRetry()
                        }) {
                            Text(text = context.getString(R.string.retry))
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
                                Text(text = viewModel.name, color = SecondaryColor)
                            }
                        },
                        backgroundColor = PrimaryColor,
                        navigationIcon = {
                            Row (horizontalArrangement = Arrangement.SpaceBetween){

                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon( imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back", tint = SecondaryColor )
                                }

                                IconButton(onClick = { navController.popBackStack() }) {
                                    Image(
                                        painter = rememberImagePainter(
                                            imageLoader = ImageLoader.invoke(context = LocalContext.current),
                                            data = viewModel.picture,
                                            builder = {
                                                size(OriginalSize)
                                                scale(Scale.FILL)
                                                transformations(CircleCropTransformation())
                                            }
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clickable {
                                                showFullScreenPicture = viewModel.picture
                                            },
                                        contentScale = ContentScale.Crop,
                                        alignment = Alignment.Center
                                    )
                                }

                            }
                        })

                    Spacer(modifier = Modifier.height(20.dp))

                    Spacer(modifier = Modifier.height(20.dp))

                    LazyColumn {
                        items(state.messageList.size) {
                            when (state.messageList[it].type) {
                                MessageType.TEXT -> {
                                    MessageItem(
                                        message = state.messageList[it],
                                        viewModel.isSender(state.messageList[it])
                                    )
                                }
                                MessageType.LOCATION -> {
                                    MapMessageItem(message = state.messageList[it],
                                        viewModel.isSender(state.messageList[it])
                                    )
                                }
                                MessageType.IMAGE -> {
                                    MessageImageItem(message = state.messageList[it],
                                        viewModel.isSender(state.messageList[it]))
                                }
                            }
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
fun ChatScreenPreview() {

    ChatScreen(
        navController = rememberNavController(),
        viewModel = viewModel()
    )
}