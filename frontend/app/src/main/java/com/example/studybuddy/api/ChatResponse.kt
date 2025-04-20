package com.example.studybuddy.api

data class ChatResponse(
    val id: String,
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)
