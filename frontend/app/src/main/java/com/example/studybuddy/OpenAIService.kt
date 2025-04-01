package com.example.studybuddy

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIService {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer ${BuildConfig.API_KEY}" // Use BuildConfig.API_KEY here
    )
    @POST("v1/chat/completions")
    fun getChatCompletion(@Body request: RequestData): Call<ResponseData>
}
