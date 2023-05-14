package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.Controller.ChatController;
import com.example.whatsappclone.Models.Message;
import com.example.whatsappclone.databinding.ActivityChatDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class Group_Chat extends AppCompatActivity {

    ActivityChatDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Group_Chat.this, MainActivity.class));
            }
        });

        final FirebaseDatabase database;

        database = FirebaseDatabase.getInstance();

        final ArrayList<Message> messagesModel = new ArrayList<>();

        final ChatController chatController = new ChatController(messagesModel,this);

        final String SENDING_ID = FirebaseAuth.getInstance().getUid();

        binding.Username.setText("Group Chat");



        binding.chat.setAdapter(chatController);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.chat.scrollToPosition(messagesModel.size() - 1);
        binding.chat.setLayoutManager(linearLayoutManager);

        database.getReference().child("group_Chat")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messagesModel.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    Message message = dataSnapshot.getValue(Message.class);
                                    messagesModel.add(message);
                                }
                                chatController.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Group_Chat.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String MESSAGE_SENT = binding.messageTxt.getText().toString();
                final Message MESSAGE = new Message(SENDING_ID,MESSAGE_SENT);
                MESSAGE.setTime(new Date().getTime());
                binding.messageTxt.setText("");
                database.getReference().child("group_Chat")
                        .push()
                        .setValue(MESSAGE)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
            }
        });


    }



}