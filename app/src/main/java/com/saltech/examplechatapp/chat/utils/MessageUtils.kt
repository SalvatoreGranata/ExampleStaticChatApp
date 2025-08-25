package com.saltech.examplechatapp.chat.utils

import android.util.Log
import com.saltech.examplechatapp.chat.utils.MessageUtils.Companion.getLatitude
import com.saltech.examplechatapp.utils.Constants
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MessageUtils {

    companion object {
        fun String.getLatitude() : Double {
            return try {
                this.split(",")[0].toDouble()
            }catch (e: Exception){
                Log.i(MessageUtils::class.java.name, "Could not get the latitude. \n $e")
                0.0
            }
        }
        fun String.getLongitude() : Double {
            return try {
                this.split(",")[1].toDouble()
            }catch (e: Exception){
                Log.i(MessageUtils::class.java.name, "Could not get the longitude. \n $e")
                0.0
            }
        }
    }
}