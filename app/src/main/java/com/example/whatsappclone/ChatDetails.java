package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.whatsappclone.Models.Message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsappclone.Controller.ChatController;
import com.example.whatsappclone.databinding.ActivityChatDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetails extends AppCompatActivity {

    ActivityChatDetailsBinding binding;

    FirebaseDatabase database;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderID = auth.getUid();
        String reciveId = getIntent().getStringExtra("userId");
        String reciveName = getIntent().getStringExtra("userName");
        String recivePP = getIntent().getStringExtra("profilePic");
        binding.Username.setText(reciveName);
        Picasso.get().load(recivePP).placeholder(R.drawable.avatar1).into(binding.profilePic);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatDetails.this, MainActivity.class));
            }
        });

        final ArrayList<Message> messagesModel = new ArrayList<>();
        final ChatController chatController = new ChatController(messagesModel, this, reciveId);

        binding.chat.setAdapter(chatController);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.chat.scrollToPosition(messagesModel.size() - 1);
        binding.chat.setLayoutManager(linearLayoutManager);


        String senderRoom = senderID + reciveId;
        String receiverRoom = reciveId + senderID;

        database.getReference().child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagesModel.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Message message = snapshot1.getValue(Message.class);
                            message.setMessageId(snapshot1.getKey());
                            messagesModel.add(message);
                        }
                        chatController.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        //everything is working here

        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messaging = binding.messageTxt.getText().toString();
                final Message model = new Message(senderID, messaging);
                model.setTime(new Date().getTime());
                binding.messageTxt.setText("");

                database.getReference().child("Chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("Chats")
                                        .child(receiverRoom)
                                        .push()
                                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });
            }
        });

    }
}