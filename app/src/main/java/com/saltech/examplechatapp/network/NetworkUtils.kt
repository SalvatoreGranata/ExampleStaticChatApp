package com.saltech.examplechatapp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.saltech.examplechatapp.chat.utils.MessageType
import com.saltech.examplechatapp.chat.utils.MessageTypeDeserializer
import com.saltech.examplechatapp.utils.Constants.API_BASE_URL
import com.saltech.examplechatapp.utils.NetworkUnavailableException
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkUtils {


    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(MessageType::class.java, MessageTypeDeserializer())
        .create()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(
        @ApplicationContext context: Context,
        retrofit: Retrofit) : ApiService {
        if (isNetworkAvailable(context)) {
            return retrofit.create(ApiService::class.java)
        }
        else throw NetworkUnavailableException("No connection available.")
    }

    private fun isNetworkAvailable(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

    }
}