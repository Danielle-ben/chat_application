package com.example.chatapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private ArrayList<ChatMessage> messages;
    private FirebaseFirestore db;
    private String currentUser;

    public MessageAdapter(String currentUser) {
        this.currentUser = currentUser;
        messages = new ArrayList<ChatMessage>();
        db = FirebaseFirestore.getInstance();
        db.collection("chat")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        messages = new ArrayList<ChatMessage>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ChatMessage message = new ChatMessage(
                                    document.get("photoURL").toString(),
                                    document.get("displayName").toString(),
                                    document.get("userID").toString(),
                                    document.get("message").toString(),
                                    document.get("postTime").toString());
                            messages.add(message);
                        }
                        Collections.sort(messages, Comparator.comparing(ChatMessage::getPostTime));
                    }
                });
        db.collection("chat").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NewApi")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                messages = new ArrayList<ChatMessage>();
                for (DocumentSnapshot document : value.getDocuments()) {
                    try {
                        ChatMessage message = new ChatMessage(
                                document.get("photoURL").toString(),
                                document.get("displayName").toString(),
                                document.get("userID").toString(),
                                document.get("message").toString(),
                                document.get("postTime").toString());
                        messages.add(message);
                    } catch (Exception e) {
                        return;
                    }
                }
                Collections.sort(messages, Comparator.comparing(ChatMessage::getPostTime));
                notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_message_view, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = this.messages.get(position);
        holder.message.setText(message.message);
        holder.userName.setText(message.userName);
        Glide.with(holder.userImage.getContext()).load(message.userPhoto).into(holder.userImage);
        if (holder.userName.getText().toString().equals(this.currentUser)) {
            holder.itemView.findViewById(R.id.background).setBackgroundColor(Color.DKGRAY);
        } else {
            holder.itemView.findViewById(R.id.background).setBackgroundColor(Color.LTGRAY);
        }
    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    public void addMessage(ChatMessage message) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("displayName", message.userName);
        messageMap.put("photoURL", message.userPhoto);
        messageMap.put("userID", message.userID);
        messageMap.put("message", message.message);
        messageMap.put("postTime", message.postTime.toString());
        db.collection("chat").add(messageMap);
    }

}
