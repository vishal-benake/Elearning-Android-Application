package com.git.extc.activities.MoreActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.git.extc.R;
import com.git.extc.databinding.ActivityChatGptSelectionBinding;
import com.git.extc.databinding.ActivityGenerateImagesBinding;

public class GenerateImages extends AppCompatActivity {

    ActivityGenerateImagesBinding binding;
    public Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGenerateImagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        binding.backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatGptSelection.class);
                startActivity(intent);
            }
        });

    }
}