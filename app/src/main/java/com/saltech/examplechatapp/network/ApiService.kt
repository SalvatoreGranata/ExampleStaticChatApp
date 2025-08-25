package com.saltech.examplechatapp.network

import com.saltech.examplechatapp.chat.model.MessageRecord
import com.saltech.examplechatapp.home.model.ChatRecord
import com.saltech.examplechatapp.utils.Constants.MESSAGES_ENDPOINT
import com.saltech.examplechatapp.utils.Constants.CHAT_ENDPOINT
import com.saltech.examplechatapp.utils.Constants.HEADER_ACCESS_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {

    @GET(CHAT_ENDPOINT)
    @Headers(HEADER_ACCESS_KEY)
    suspend fun getChats(): Response<ChatRecord>

    @GET(MESSAGES_ENDPOINT)
    @Headers(HEADER_ACCESS_KEY)
    suspend fun getMessages(): Response<MessageRecord>

}