package com.saltech.examplechatapp.chat.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class MessageTypeDeserializer : JsonDeserializer<MessageType> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MessageType {
        return when (val messageTypeString = json?.asString) {
            "text" -> MessageType.TEXT
            "location" -> MessageType.LOCATION
            "image" -> MessageType.IMAGE
            else -> throw JsonParseException("Message Type not valid: $messageTypeString")
        }
    }
}