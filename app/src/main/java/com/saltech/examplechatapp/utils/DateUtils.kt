package com.saltech.examplechatapp.utils

import android.util.Log
import com.saltech.examplechatapp.utils.Constants.INPUT_DATE_PATTERN
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateUtils {

    companion object {
        fun String.formatDate(outputPattern: String) : String {
            return try {

                val inputFormatter = DateTimeFormatter.ofPattern(INPUT_DATE_PATTERN)
                val dateTime = LocalDateTime.parse(this, inputFormatter)
                val outputFormatter = DateTimeFormatter.ofPattern(outputPattern)

                dateTime.format(outputFormatter)
            }catch (e: Exception){
                Log.i(DateUtils::class.java.name, "Could not parse the date. \n $e")
                this
            }
        }
    }

}