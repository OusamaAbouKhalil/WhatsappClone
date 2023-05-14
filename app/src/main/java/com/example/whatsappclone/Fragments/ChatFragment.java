package com.example.whatsappclone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.whatsappclone.Controller.UserController;
import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    public ChatFragment() {
        // Required empty public constructor
    }

    FragmentChatBinding binding;

    ArrayList<Users> list = new ArrayList<>();

    FirebaseDatabase database;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater,container,false);

        database = FirebaseDatabase.getInstance();

        UserController userController = new UserController(list,getContext());
        binding.Conversations.setAdapter(userController);

        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());

        binding.Conversations.setLayoutManager(linearLayout);


        binding.Conversations.addItemDecoration(new DividerItemDecoration(binding.Conversations.getContext(), DividerItemDecoration.VERTICAL));
        database.getReference().child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    if (users != null) {
                        users.setUserId(dataSnapshot.getKey());

                        if (!users.getUserId().equals(FirebaseAuth.getInstance().getUid())) {
                            list.add(users);
                        }
                    }
                }
                userController.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }



}