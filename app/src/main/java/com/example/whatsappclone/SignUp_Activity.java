package com.example.whatsappclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp_Activity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignUp_Activity.this);
        progressDialog.setTitle("Creating Your Account");
        progressDialog.setMessage("Creating Your Account Please Wait");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.txtUserName.getText().toString().isEmpty()&&!binding.txtEmail.getText().toString().isEmpty()&&!binding.txtPassword.getText().toString().isEmpty()){

                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressDialog.dismiss();


                            if(task.isSuccessful()){

                                Users users = new Users(binding.txtUserName.getText().toString(),binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString());
                                String id = task.getResult().getUser().getUid();
                                database.getReference().child("User").child(id).setValue(users);
                                Toast.makeText(SignUp_Activity.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(SignUp_Activity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
                else{
                    Toast.makeText(SignUp_Activity.this, "Missing Information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.alreadyHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(
                        SignUp_Activity.this,Login.class
                ));
            }
        });

    }


}