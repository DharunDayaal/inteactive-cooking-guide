package com.surendramaran.yolov8tflite;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.*;

public class Chatbot {

    private EditText chatEditText;
    private ImageView sendButton;
    private TextView defaultTextView;
    private RecyclerView recyclerView;
    private List<MessageModel> messageList;
    private MessageAdapter messageAdapter;

    private static final MediaType JSON = MediaType.get("application/json");
    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS).build();

    // Zero-argument constructor
    public Chatbot(EditText chatEditText, ImageView sendButton, TextView defaultTextView, RecyclerView recyclerView, List<MessageModel> messageList, MessageAdapter messageAdapter) {
        // This constructor is added to meet the requirements of Android for instantiation
        this.chatEditText = chatEditText;
        this.sendButton = sendButton;
        this.defaultTextView = defaultTextView;
        this.recyclerView = recyclerView;
        this.messageList = messageList;
        this.messageAdapter = messageAdapter;

        // Initialize messageList if it's null
        if (this.messageList == null) {
            this.messageList = new ArrayList<>();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addToChat(String message, String sentBy) {
        messageList.add(new MessageModel(message, sentBy));
        messageAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
    }

    public void sendResponse(String response) {
        
        //addToChat(response, MessageModel.SENT_BY_BOT);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addToChat(response, MessageModel.SENT_BY_BOT);
            }
        });
    }

    private void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public void callApi(String message) {
        messageList.add(new MessageModel("typing..", MessageModel.SENT_BY_BOT));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model", "gpt-3.5-turbo");

            JSONArray messageArray = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("role", "user");
            obj.put("content", message);
            messageArray.put(obj);
            jsonObject.put("messages", messageArray);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody requestBody = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer YOUR-API-KEY")
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                sendResponse("Fail to load due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String res = jsonArray.getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");
                        sendResponse(res.trim());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    sendResponse("Fail to load due to " + response.body().string());
                }
            }
        });
    }
}
