package com.example.studybuddy.api

data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double
)
