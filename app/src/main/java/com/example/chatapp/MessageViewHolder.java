package com.example.chatapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    public final ImageView userImage;
    public final TextView userName;
    public final TextView message;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

        this.userImage = itemView.findViewById(R.id.userImage);
        this.userName = itemView.findViewById(R.id.userName);
        this.message = itemView.findViewById(R.id.messageText);
    }
}
