package com.saltech.examplechatapp.chat.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.Keep
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saltech.examplechatapp.R
import com.saltech.examplechatapp.chat.model.Message
import com.saltech.examplechatapp.network.ApiService
import com.saltech.examplechatapp.utils.Constants
import com.saltech.examplechatapp.utils.Constants.STANDARD_UTF_8
import com.saltech.examplechatapp.utils.NetworkUnavailableException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLDecoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@Keep
data class ChatState(
    val error: String? = null,
    val loading: Boolean = false,
    val emptyStateAvailable: Boolean = false,
    val messageList: List<Message> = emptyList()
)

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class ChatViewModel  @Inject constructor (
    @ApplicationContext private val context: Context,
    private val apiService: ApiService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val state = MutableStateFlow(ChatState())

    var name: String = ""
    var picture: String = ""

    init {
        name = savedStateHandle.get<String>("name") ?: "Unknown"
        picture = savedStateHandle.get<String>("picture")?.let { urlDecode(it) } ?: ""

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getMessages()
            }
        }
    }

    @Synchronized
    private fun loading(value: Boolean) {
        state.update {
            it.copy(
                loading = value
            )
        }
    }

    @Synchronized
    private fun error(message: String?) {
        loading(false)
        state.update {
            it.copy(
                error = message
            )
        }
    }

    fun onRetry() {
        error(null)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getMessages()
            }
        }
    }

    private suspend fun getMessages() {

        loading(true)

        try {

            val response = apiService.getMessages()

            if (response.isSuccessful) {
                loading(false)

                state.update {
                    it.copy(
                        messageList = response.body()!!.list.sortMessages()
                    )
                }
            } else {
                error(response.message())
            }
        } catch (nue: NetworkUnavailableException){
            // Due to very few lines of code, the app needed to be slowed down to let the user understand that the app is retrying.
            delay(1000)
            error(nue.message.toString())
        } catch (e: Exception) {
            // Due to very few lines of code, the app needed to be slowed down to let the user understand that the app is retrying.
            delay(1000)
            error(context.getString(R.string.generic_error))
        }
    }

    fun isSender(message: Message): Boolean {
        return message.sentAt != null
    }


    // I could've sorted the list by Id, but the I was asked in the test to sort the messages "chronologically".
    private fun List<Message>.sortMessages(): List<Message> {

        return try {
                val sortedList = this.sortedBy { item ->
                val dateString = item.sentAt ?: item.receivedAt
                val inputFormatter = DateTimeFormatter.ofPattern(Constants.INPUT_DATE_PATTERN)
                val dateTime = LocalDateTime.parse(dateString, inputFormatter)
                dateTime
            }
            sortedList
        }catch (e: Exception){
            Log.i(ChatViewModel::class.java.name, "Error sorting the list \n $e")
            this
        }
    }

    private fun urlDecode(encodedValue: String): String{
        return URLDecoder.decode(encodedValue, STANDARD_UTF_8)
    }

}