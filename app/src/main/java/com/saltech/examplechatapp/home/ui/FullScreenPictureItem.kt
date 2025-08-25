package com.saltech.examplechatapp.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import com.saltech.examplechatapp.R

@Composable
fun FullScreenPictureItem(
    picture: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = rememberImagePainter(
                imageLoader = ImageLoader.invoke(context = context),
                data = picture,
                builder = {
                    size(OriginalSize)
                    scale(Scale.FILL)
                }
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
                .background(Color.Black),
            contentScale = ContentScale.Fit
        )

        Button(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Text(text = context.getString(R.string.close))
        }
    }
}

@Preview
@Composable
fun FullScreenPicturePreview() {

    FullScreenPictureItem(
        picture = "",
        onDismiss = {}
    )
}




