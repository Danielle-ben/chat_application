package com.example.chatapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {
    Uri DEFAULT_AVATAR_URL = Uri.parse("https://as2.ftcdn.net/v2/jpg/03/32/59/65/1000_F_332596535_lAdLhf6KzbW6PWXBWeIFTovTii1drkbT.jpg");

    private GoogleSignInAccount account;
    private String email;
    private String accountID;
    private String displayName;
    private Uri avatar_url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TEST", "BASIC");
        setContentView(R.layout.chat_activity);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        try {
            account = (GoogleSignInAccount) extras.get("user");
            email = account.getEmail();
            avatar_url = account.getPhotoUrl();
            displayName = account.getDisplayName();
            accountID = account.getId();
        } catch (Exception e) {
            email = extras.get("user").toString();
            avatar_url = DEFAULT_AVATAR_URL;
            displayName = email;
            accountID = email;
        }

        TextView welcome = findViewById(R.id.textView);
        welcome.setText("Welcome " + displayName);

        ImageView userImage = findViewById(R.id.imageView);
        Glide.with(this).load(avatar_url).into(userImage);

        MessageAdapter adapter = new MessageAdapter(displayName);
        RecyclerView recycler = (RecyclerView)findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(false);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(), 1);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);
        Button btn = findViewById(R.id.button_send);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = findViewById(R.id.editText_message);
                ChatMessage message = new ChatMessage(
                        avatar_url.toString(),
                        displayName,
                        accountID,
                        text.getText().toString(),
                        Calendar.getInstance().getTime().toString());
                adapter.addMessage(message);
                adapter.notifyDataSetChanged();
                text.setText("Write your message!");
                recycler.scrollToPosition(adapter.getItemCount());
            }
        });
    }

}
