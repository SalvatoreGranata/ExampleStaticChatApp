package com.saltech.examplechatapp.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saltech.examplechatapp.utils.theme.PrimaryColor
import com.saltech.examplechatapp.utils.theme.SecondaryColor

@Composable
fun UnseenMessagesItem(number: Int) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(PrimaryColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = SecondaryColor
        )
    }
}

@Preview
@Composable
fun UnseenMessagesPreview() {
    UnseenMessagesItem(number = 1)
}