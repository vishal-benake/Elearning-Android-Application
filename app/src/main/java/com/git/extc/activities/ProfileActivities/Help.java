package com.git.extc.activities.ProfileActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.git.extc.R;
import com.git.extc.activities.Home;
import com.git.extc.databinding.ActivityHelpBinding;
import com.git.extc.databinding.ActivityHomeBinding;

public class Help extends AppCompatActivity {

    ActivityHelpBinding binding;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_help);

        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

//         getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

         binding.layout1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(context, ReportProblem.class);
                 startActivity(intent);
             }
         });

        binding.layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MoreFeautres.class);
                startActivity(intent);
            }
        });

        binding.layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotesAccess.class);
                startActivity(intent);
            }
        });

        binding.backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Home.class);
                intent.putExtra("returnToFragment", "Fragment4");
                startActivity(intent);
            }
        });


    }
}