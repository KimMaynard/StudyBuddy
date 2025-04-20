package com.example.studybuddy.retrofitNetwork

import com.example.studybuddy.api.OpenAIService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenAIClient {
    private const val BASE_URL = "https://api.openai.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getInstance(): OpenAIService {
        return retrofit.create(OpenAIService::class.java)
    }
}
