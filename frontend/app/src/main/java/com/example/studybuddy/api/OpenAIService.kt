package com.example.studybuddy.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAIService {
    @POST("v1/chat/completions")
    fun getChatCompletion(
        @Header("Authorization") authHeader: String,
        @Body request: ChatRequest
    ): Call<ChatResponse>
}
