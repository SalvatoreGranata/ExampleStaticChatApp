package com.saltech.examplechatapp.home.model

import com.google.gson.annotations.SerializedName

data class ChatRecord(
    @SerializedName("record")
    val list: List<Chat>,
)


data class Chat(
    @SerializedName("id")
    val id: Long,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("last_message_content")
    val lastMessageContent: String,
    @SerializedName("last_message_date")
    val lastMessageDate: String,
    @SerializedName("unseen_count")
    val unseenCount: Int = 0
)