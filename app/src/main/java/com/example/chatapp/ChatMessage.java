package com.example.chatapp;

import com.google.type.DateTime;

import java.util.Date;

public class ChatMessage {
    public final String userPhoto;
    public final String userName;
    public final String userID;
    public final String message;
    public final String postTime;

    public ChatMessage(String userPhoto, String userName, String userID, String message, String postTime) {
        this.userPhoto = userPhoto;
        this.userName = userName;
        this.userID = userID;
        this.message = message;
        this.postTime = postTime;
    }

    public String getPostTime() {
        return this.postTime;
    }
}
