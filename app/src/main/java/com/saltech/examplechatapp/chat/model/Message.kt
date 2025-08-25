package com.saltech.examplechatapp.chat.model
import com.saltech.examplechatapp.chat.utils.MessageType
import com.google.gson.annotations.SerializedName

data class MessageRecord(
    @SerializedName("record")
    val list: List<Message>
)

data class Message(
    @SerializedName("message_id")
    val id: Long,
    @SerializedName("message_type")
    val type: MessageType,
    @SerializedName("message_content")
    val content: String,
    @SerializedName("sent_at")
    val sentAt: String? = null,
    @SerializedName("received_at")
    val receivedAt: String? = null
)