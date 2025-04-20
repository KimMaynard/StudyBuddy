package com.example.studybuddy

import android.util.Log
import com.example.studybuddy.api.ChatRequest
import com.example.studybuddy.api.ChatResponse
import com.example.studybuddy.api.Message
import com.example.studybuddy.retrofitNetwork.OpenAIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object OpenAIAPI {

    // Force unwrap so it's not nullable
    private val API_KEY: String = "Bearer " + BuildConfig.OPENAI_API_KEY

    fun sendMessage(messages: List<Message>, callback: (String?) -> Unit) {
        Log.d("OpenAIAPI", "Sending request to GPT-4o")

        val request = ChatRequest(
            model = "gpt-4o",
            messages = messages,
            temperature = 0.7
        )

        val service = OpenAIClient.getInstance()
        service.getChatCompletion(API_KEY, request).enqueue(object : Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val reply = response.body()!!.choices[0].message.content
                    callback(reply)
                } else {
                    Log.e("OpenAIAPI", "API Error: ${response.code()} - ${response.errorBody()?.string()}")
                    callback("Error ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                Log.e("OpenAIAPI", "Network Failure: ${t.message}", t)
                callback("Network Error: ${t.message}")
            }
        })
    }
}
