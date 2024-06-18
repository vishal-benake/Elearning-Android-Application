package com.git.extc.activities.MoreActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.git.extc.activities.Home;
import com.git.extc.databinding.ActivityChatGptSelectionBinding;

public class ChatGptSelection extends AppCompatActivity {
    ActivityChatGptSelectionBinding binding;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatGptSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        binding.chatwithbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatwithBot.class);
                startActivity(intent);
            }
        });

        binding.generateimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GenerateImages.class);
                startActivity(intent);
            }
        });

        binding.backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Home.class);
                intent.putExtra("returnToFragment", "Fragment3");
                startActivity(intent);
            }
        });

    }
}