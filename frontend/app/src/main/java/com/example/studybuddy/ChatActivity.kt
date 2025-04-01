package com.example.studybuddy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat) // Ensure this is activity_chat

        val questionEditText: EditText = findViewById(R.id.questionEditText)
        val submitButton: Button = findViewById(R.id.submitButton)
        val answerTextView: TextView = findViewById(R.id.answerTextView)

        submitButton.setOnClickListener {
            val question = questionEditText.text.toString()
            if (question.isNotEmpty()) {
                val request = RequestData(
                    messages = listOf(Message(role = "user", content = question))
                )

                val call = RetrofitClient.instance.getChatCompletion(request)

                call.enqueue(object : Callback<ResponseData> {
                    override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                        if (response.isSuccessful) {
                            val answer = response.body()?.choices?.get(0)?.message?.content ?: "No answer found."
                            answerTextView.text = answer
                        } else {
                            Toast.makeText(this@ChatActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                        Toast.makeText(this@ChatActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@ChatActivity, "Please enter a question.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
