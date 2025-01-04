package com.surendramaran.yolov8tflite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class startupActivity extends AppCompatActivity {

    private AppCompatButton CameraButton;
    private AppCompatButton chatBotButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        CameraButton = findViewById(R.id.cameraButton);
        chatBotButton = findViewById(R.id.chatbotbutton);


        CameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(startupActivity.this, MainActivity.class);
                startActivity(cameraIntent);
             }
        });

        chatBotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatBotIntent = new Intent(startupActivity.this, ChatbotActivity.class);
                startActivity(chatBotIntent);
            }
        });
    }
}