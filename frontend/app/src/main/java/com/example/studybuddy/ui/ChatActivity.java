package com.example.studybuddy.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybuddy.BuildConfig;
import com.example.studybuddy.models.MessageModel;
import com.example.studybuddy.R;
import com.example.studybuddy.api.Message;
import com.example.studybuddy.api.OpenAIService;
import com.example.studybuddy.api.ChatRequest;
import com.example.studybuddy.api.ChatResponse;
import com.example.studybuddy.retrofitNetwork.OpenAIClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private EditText inputMessage;
    private Button sendButton;
    private ImageButton backButton;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private final List<MessageModel> messagesList = new ArrayList<>();
    private final List<Message> conversationHistory = new ArrayList<>();
    private final String API_KEY = "Bearer " + BuildConfig.OPENAI_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        inputMessage = findViewById(R.id.message);
        sendButton = findViewById(R.id.send);
        recyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.backButton);

        adapter = new MessageAdapter(messagesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        recyclerView.post(() -> {
            addMessage("🔥 SYSTEM ONLINE 🔥", false);
            addMessage("💬 USER READY 💬", true);
            conversationHistory.add(new Message("system",
                    "You're a friendly, proactive study assistant. Always ask the user what classes they’re taking, and suggest helpful ideas like topic breakdowns, flashcards, review questions, and study planning."));
            conversationHistory.add(new Message("assistant",
                    "Hi! I'm your study assistant 🤓 What classes are you taking this semester? I can help you review, quiz yourself, or break down hard topics!"));
            addMessage("Hi! I'm your study assistant 🤓 What classes are you taking this semester? I can help you review, quiz yourself, or break down hard topics!", false);
        });

        sendButton.setOnClickListener(view -> {
            String userInput = inputMessage.getText().toString().trim();
            if (!userInput.isEmpty()) {
                addMessage(userInput, true);
                conversationHistory.add(new Message("user", userInput));
                inputMessage.setText("");

                addMessage("Typing...", false);
                sendMessageToGPT();
            }
        });

        backButton.setOnClickListener(v -> finish());
    }

    private void addMessage(String content, boolean isUser) {
        Log.d("ChatActivity", "addMessage: " + content + " | user? " + isUser);
        messagesList.add(new MessageModel(content, isUser));
        runOnUiThread(() -> {
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(messagesList.size() - 1);
        });
    }

    private void sendMessageToGPT() {
        ChatRequest request = new ChatRequest("gpt-4o", conversationHistory, 0.7f);
        OpenAIService service = OpenAIClient.INSTANCE.getInstance();

        service.getChatCompletion(API_KEY, request).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                messagesList.remove(messagesList.size() - 1); // remove "Typing..."
                if (response.isSuccessful() && response.body() != null) {
                    String reply = response.body().getChoices().get(0).getMessage().getContent();
                    conversationHistory.add(new Message("assistant", reply));
                    addMessage(reply, false);
                } else {
                    addMessage("Error: " + response.code(), false);
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                messagesList.remove(messagesList.size() - 1); // remove "Typing..."
                addMessage("Error: " + t.getMessage(), false);
            }
        });
    }
}
