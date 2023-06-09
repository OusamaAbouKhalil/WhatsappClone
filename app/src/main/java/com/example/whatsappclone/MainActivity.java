package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whatsappclone.Controller.PageController;
import com.example.whatsappclone.databinding.ActivityLoginBinding;
import com.example.whatsappclone.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


        ActivityMainBinding binding;

        FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        String lastMessage = getIntent().getStringExtra("lastMessage");
        binding.viewPage.setAdapter(new PageController(getSupportFragmentManager()));
        binding.tabs.setupWithViewPager(binding.viewPage);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.settings:
                // Toast.makeText(this, "settings Selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.GroupChat:
            //Toast.makeText(this, "Group Chat is selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Group_Chat.class));
                break;
            case R.id.Logout:
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this,Login.class));
                break;
        }



        return super.onOptionsItemSelected(item);
    }

}