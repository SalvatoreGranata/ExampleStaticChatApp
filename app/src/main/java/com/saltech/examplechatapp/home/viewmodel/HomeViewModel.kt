package com.saltech.examplechatapp.home.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saltech.examplechatapp.R
import com.saltech.examplechatapp.home.model.Chat
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
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@Keep
data class HomeState(
    val error: String? = null,
    val loading: Boolean = false,
    val emptyStateAvailable: Boolean = false,
    val chatList: List<Chat> = emptyList()
)

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class HomeViewModel  @Inject constructor (
    @ApplicationContext val context: Context,
    private val apiService: ApiService
) : ViewModel() {

    val state = MutableStateFlow(HomeState())

    init {

        viewModelScope.launch{
            withContext(Dispatchers.IO){
                getChats()
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

    fun onRetry(){
        error(null)

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                getChats()
            }
        }
    }

    private suspend fun getChats() {

        loading(true)

        try {

            val response = apiService.getChats()

            if (response.isSuccessful) {
                loading(false)

                state.update {
                    it.copy(
                        chatList = response.body()!!.list.sortChats()
                    )
                }
            } else {
                error(response.message())
            }
        } catch (nue: NetworkUnavailableException){
            // Due to very few lines of code, the app needed to be slowed down to let the user understand that the app is retrying.
            delay(1000)
            error(nue.message.toString())
        } catch (e: Exception){
            // Due to very few lines of code, the app needed to be slowed down to let the user understand that the app is retrying.
            delay(1000)
            error(context.getString(R.string.generic_error))
        }
    }

    private fun List<Chat>.sortChats(): List<Chat>{
        return this.sortedBy { item ->
            val dateString = item.lastMessageDate
            val inputFormatter = DateTimeFormatter.ofPattern(Constants.INPUT_DATE_PATTERN)
            val dateTime = LocalDateTime.parse(dateString, inputFormatter)
            dateTime
        }.reversed()
    }

    fun urlEncode(value: String):String = URLEncoder.encode(value, STANDARD_UTF_8)

}