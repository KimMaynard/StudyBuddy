package com.example.studybuddy

data class ResponseData(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)
