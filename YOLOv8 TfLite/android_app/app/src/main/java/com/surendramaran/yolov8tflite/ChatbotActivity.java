package com.surendramaran.yolov8tflite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surendramaran.yolov8tflite.Chatbot;
import com.surendramaran.yolov8tflite.MessageAdapter;
import com.surendramaran.yolov8tflite.MessageModel;
import com.surendramaran.yolov8tflite.R;

import java.util.ArrayList;
import java.util.List;

public class ChatbotActivity extends AppCompatActivity {

    private EditText chatEditText;
    private ImageView sendButton;
    private TextView defaultTextView;
    private RecyclerView recyclerView;
    private List<MessageModel> messageList;
    private MessageAdapter messageAdapter;
    private Chatbot chatbot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        // Initialize UI elements
        chatEditText = findViewById(R.id.promptText);
        sendButton = findViewById(R.id.sendButton);
        defaultTextView = findViewById(R.id.centerTextView);
        recyclerView = findViewById(R.id.recyclerView);

        // Initialize message list and adapter
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Chatbot
        chatbot = new Chatbot(chatEditText, sendButton, defaultTextView, recyclerView, messageList, messageAdapter);

        // Set click listener for the send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get message from EditText
                String message = chatEditText.getText().toString().trim();

                // Add user's message to chat UI
                chatbot.addToChat(message, MessageModel.SENT_BY_USER);

                // Call the API and process the response
                chatbot.callApi(message);

                // Clear EditText
                chatEditText.setText("");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        Intent intent = new Intent(ChatbotActivity.this, startupActivity.class);
        startActivity(intent);
        // Optionally, you can call finish() if you want to finish the ChatbotActivity
        // finish();
        Intent chatintent = new Intent(ChatbotActivity.this, MainActivity.class);
        startActivity(chatintent);
    }



}
