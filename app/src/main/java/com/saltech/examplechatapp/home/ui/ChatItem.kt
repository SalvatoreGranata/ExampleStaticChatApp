package com.saltech.examplechatapp.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.saltech.examplechatapp.R
import com.saltech.examplechatapp.home.model.Chat
import com.saltech.examplechatapp.utils.Constants.OUTPUT_CHAT_DATE_PATTERN
import com.saltech.examplechatapp.utils.DateUtils.Companion.formatDate
import com.saltech.examplechatapp.utils.theme.PrimaryColor
import com.saltech.examplechatapp.utils.theme.SecondaryColor
import java.util.Calendar

@Composable
fun ChatItem (
    chat: Chat,
    onClick: () -> Unit,
    pictureClick: ( picture: String ) -> Unit ) {

    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(SecondaryColor),
        modifier = Modifier
            .padding(5.dp)
            .height(100.dp)
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .clickable { onClick() }
        ) {

            Image(
                painter = rememberImagePainter(
                    imageLoader = ImageLoader.invoke(context = LocalContext.current),
                    data = chat.avatarUrl,
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
                        pictureClick( chat.avatarUrl )
                    },
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = chat.userName,
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryColor
                    )

                    if(chat.unseenCount > 0) {
                        UnseenMessagesItem(number = chat.unseenCount)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = chat.lastMessageContent,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = PrimaryColor
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = chat.lastMessageDate.formatDate(OUTPUT_CHAT_DATE_PATTERN),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatItemPreview() {

    val context = LocalContext.current

    ChatItem(
        Chat(
            1,
            "John Doe",
            "",
            context.getString(R.string.lorem_ipsum),
            Calendar.getInstance().time.toString(),
            5
        ),
        onClick = {},
        pictureClick = {}
    )
}