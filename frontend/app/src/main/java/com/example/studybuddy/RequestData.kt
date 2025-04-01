
package com.example.studybuddy


data class RequestData(
    val model: String = "gpt-4o", // Or "gpt-4"
    val messages: List<Message>
)
