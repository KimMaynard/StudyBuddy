package com.example.studybuddy

import android.util.Log

object OpenAIAPI {
    const val BASE_URL = "https://api.openai.com/"

    init {
        val bearerToken = "Bearer ${BuildConfig.API_KEY}" // Use BuildConfig.API_KEY
        Log.d("OpenAIAPI", "Bearer Token: $bearerToken")
    }
}
